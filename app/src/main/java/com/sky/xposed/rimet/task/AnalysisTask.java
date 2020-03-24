/*
 * Copyright (c) 2020 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.rimet.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ClassUtil;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by sky on 2020-03-22.
 */
public class AnalysisTask extends AbstractTask<String, String, Map<Integer, String>> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private ClassLoader mClassLoader;
    private Class mDatabaseClass;

    private OnPreCallback mOnPreCallback;
    private OnProgressCallback mOnProgressCallback;

    private Map<Integer, String> mStringMap = new HashMap<>();

    public AnalysisTask(Context context) {
        mContext = context;
    }

    public OnPreCallback getOnPreCallback() {
        return mOnPreCallback;
    }

    public void setOnPreCallback(OnPreCallback onPreCallback) {
        mOnPreCallback = onPreCallback;
    }

    public OnProgressCallback getOnProgressCallback() {
        return mOnProgressCallback;
    }

    public void setOnProgressCallback(OnProgressCallback onProgressCallback) {
        mOnProgressCallback = onProgressCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onProgressUpdate("准备开始分析...");
        if (mOnPreCallback != null) mOnPreCallback.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (mOnProgressCallback != null) mOnProgressCallback.onProgressUpdate(values[0]);
    }

    @Override
    protected Map<Integer, String> onHandler(String... strings) throws Exception {

        if (strings == null || strings.length != 1 || TextUtils.isEmpty(strings[0])) {
            publishProgress("参数异常!");
            return null;
        }

        String sourcePath = getSourcePath(strings[0]);

        long startTime = System.currentTimeMillis();

        List<String> classNames = getDefaultClassName(sourcePath);
        mClassLoader = new PathClassLoader(sourcePath, ClassLoader.getSystemClassLoader());

        mDatabaseClass = loadClass("com.alibaba.wukong.im.base.IMDatabase");

        for (String className : classNames) {

            handlerClass(className);

            if (mStringMap.size() >= 3) {
                // 不需要处理了
                Alog.d(">>>>>>>>>>>>>>> 不需要处理了");
                break;
            }
        }

        // 保存当前MD5信息
        mStringMap.put(M.sky.rimet_package_md5, FileUtil.getFileMD5(new File(sourcePath)));

        publishProgress("分析完成!");
        Alog.d(">>>>>>>>>>>>>>>>>> " + (System.currentTimeMillis() - startTime));
        publishProgress("总共耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return mStringMap;
    }

    private String getSourcePath(String packageName) throws PackageManager.NameNotFoundException {
        ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(packageName, 0);
        return info.sourceDir;
    }

    private void handlerClass(String className) {

        Class tClass = loadClass(className);

        if (tClass == null) return;

        int modifier = tClass.getModifiers();

        if (!Modifier.isPublic(modifier) || !Modifier.isFinal(modifier)
                || tClass.isInterface() || tClass.isAnnotation() || tClass.isEnum()) {
            // 不需要处理
            return;
        }

        publishProgress("正在分析:" + tClass.getName() + "\n");

        Class superClass = tClass.getSuperclass();

        if (Object.class.equals(superClass) && handlerServiceFactoryClass(tClass)) {
            Alog.d(">>>>>>>>>>>>>>>>>>>> ServiceFactory " + tClass);
            mStringMap.put(M.classz.class_defpackage_ServiceFactory, tClass.getName());
        } else if (Object.class.equals(superClass) && handlerRedPacketsClass(tClass)) {
            Alog.d(">>>>>>>>>>>>>>>>>>>> RedPackets " + tClass);
            mStringMap.put(M.classz.class_defpackage_RedPacketsRpc, tClass.getName());
        } else if (superClass == mDatabaseClass && handlerDatabaseClass(tClass)) {
            Alog.d(">>>>>>>>>>>>>>>>>>>> Database " + tClass);
            mStringMap.put(M.classz.class_defpackage_MessageDs, tClass.getName());
        }
    }

    private Class loadClass(String className) {
        try {
            return mClassLoader.loadClass(className);
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    private boolean handlerDatabaseClass(Class tClass) {

        try {
            Method method = ClassUtil.findMethod(tClass, value -> {
                Class[] classes = value.getParameterTypes();
                return Map.class.equals(value.getReturnType()) && classes.length == 3;
            });
            return method != null;
        } catch (Throwable ignored) {
        }
        return false;
    }

    private boolean handlerServiceFactoryClass(Class tClass) {
        try {
            Field[] fields = tClass.getDeclaredFields();
            Method[] methods = tClass.getDeclaredMethods();

            return fields.length == 1 && methods.length == 1
                    && ConcurrentMap.class.equals(fields[0].getType());
        } catch (Throwable ignored) {
        }
        return false;
    }

    private boolean handlerRedPacketsClass(Class tClass) {
        try {
            Field[] fields = tClass.getDeclaredFields();

            if (fields.length == 1
                    && tClass.equals(fields[0].getType())
                    && tClass.getDeclaredMethods().length > 10) {
                // 获取方法参数
                Method method = ClassUtil.findMethod(
                        tClass, value -> value.getParameterTypes().length > 13);
                return method != null;
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private Method getMethod(Class<?> tClass, String name, Class<?>... parameterTypes) {
        try {
            return tClass.getDeclaredMethod(name, parameterTypes);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * 获取默认的包
     * @param sourcePath
     * @return
     * @throws IOException
     */
    private List<String> getDefaultClassName(String sourcePath) throws IOException {

        DexFile dexFile = new DexFile(sourcePath);
        Enumeration<String> enumeration = dexFile.entries();

        List<String> classNames = new ArrayList<>();

        while (enumeration.hasMoreElements()) {

            String className = enumeration.nextElement();

            if (!className.contains(".") && !className.contains("$")) {
                // 默认包
                classNames.add(className);
            }
        }
        return classNames;
    }

    public interface OnPreCallback {

        void onPreExecute();
    }

    public interface OnProgressCallback {

        void onProgressUpdate(String text);
    }
}
