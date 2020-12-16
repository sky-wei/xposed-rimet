/*
 * Copyright (c) 2019 The sky Authors.
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

package tk.anysoft.xposed.lark.plugin.base;

import android.app.ActivityThread;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import tk.anysoft.xposed.lark.plugin.interfaces.XConfig;
import tk.anysoft.xposed.lark.plugin.interfaces.XConfigManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XPlugin;
import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XVersionManager;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.javax.MethodHook;
import com.sky.xposed.javax.XposedUtil;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by sky on 2018/9/24.
 */
public abstract class BasePlugin implements XPlugin {

    private final XPluginManager mPluginManager;
    private final Context mContext;
    private final XC_LoadPackage.LoadPackageParam mParam;
    private final XConfig mXConfig;
    private final XConfigManager mConfigManager;

    public BasePlugin(XPluginManager pluginManager) {
        mPluginManager = pluginManager;
        mContext = pluginManager.getContext();
        mParam = pluginManager.getLoadPackageParam();
        mXConfig = mPluginManager.getVersionManager().getSupportConfig();
        mConfigManager = pluginManager.getConfigManager();
    }

    @Override
    public boolean isHandler() {
        return true;
    }

    @Override
    public void initialization() {

    }

    @Override
    public void release() {

    }

    @Override
    public boolean isEnable(int flag, boolean defValue) {
        return mConfigManager.getBoolean(flag, defValue);
    }

    @Override
    public void setEnable(int flag, boolean enable) {
        mConfigManager.putBoolean(flag, enable);
    }

    public Context getSystemContext() {
        return ActivityThread.currentActivityThread().getSystemContext();
    }

    public Context getContext() {
        return mContext;
    }

    public XPluginManager getPluginManager() {
        return mPluginManager;
    }

    public XVersionManager getVersionManager() {
        return mPluginManager.getVersionManager();
    }

    public XConfig getXConfig() {
        return mXConfig;
    }

    public XConfigManager getConfigManager() {
        return mConfigManager;
    }

    public XC_LoadPackage.LoadPackageParam getLoadPackageParam() {
        return mParam;
    }

    public String getProcessName() {
        return mParam.processName;
    }

    public String getXString(int key) {
        return getXConfig().get(key);
    }

    public boolean hasXString(int key) {
        return getXConfig().has(key);
    }

    public Class findClass(String className) {
        return XposedUtil.findClass(className);
    }

    public Class findClass(int className) {
        return findClass(getXString(className));
    }

    public Method findMatcherMethod(int className, int methodName, Object... parameterTypes) {
        return findMatcherMethod(getXString(className), getXString(methodName), parameterTypes);
    }

    public Class findClass(String className, ClassLoader classLoader) {
        return XposedHelpers.findClass(className, classLoader);
    }

    public Method findMatcherMethod(String classNameList, String methodNameList, Object... parameterTypes) {

        if (TextUtils.isEmpty(classNameList)) return null;

        // 多个类名
        String[] classNames = classNameList.split(",");
        String[] methodNames = methodNameList.split(",");

        for (int i = 0; i < classNames.length; i++) {

            Method method = findMethodExactIfExists(
                    classNames[i], methodNames[i], parameterTypes);

            if (method != null) return method;
        }
        return null;
    }

    public Method findMethodExactIfExists(String className, String methodName, Object... parameterTypes) {
        return XposedHelpers.findMethodExactIfExists(className, mParam.classLoader, methodName, parameterTypes);
    }

    public MethodHook findMethod(String className, String methodName, Object... parameterTypes) {
        return XposedUtil.findMethod(className, methodName, parameterTypes);
    }

    public MethodHook findMethod(int className, int methodName, Object... parameterTypes) {
        return findMethod(getXString(className), getXString(methodName), parameterTypes);
    }

    public MethodHook findMethod(Class clazz, String methodName, Object... parameterTypes) {
        return XposedUtil.findMethod(clazz, methodName, parameterTypes);
    }

    public MethodHook findMethod(Class clazz, int methodName, Object... parameterTypes) {
        return findMethod(clazz, getXString(methodName), parameterTypes);
    }

    public MethodHook findConstructor(String className, Object... parameterTypes) {
        return XposedUtil.findConstructor(className, parameterTypes);
    }

    public MethodHook findConstructor(int className, Object... parameterTypes) {
        return findConstructor(getXString(className), parameterTypes);
    }

    public MethodHook findConstructor(Class clazz, Object... parameterTypes) {
        return XposedUtil.findConstructor(clazz, parameterTypes);
    }

    public Object getObjectField(Object obj, String fieldName) {
        return XposedHelpers.getObjectField(obj, fieldName);
    }

    public Object getObjectField(Object obj, int fieldName) {
        return getObjectField(obj, getXString(fieldName));
    }

    public Object invokeOriginalMethod(XC_MethodHook.MethodHookParam param) {
        try {
            return XposedBridge.invokeOriginalMethod(
                    param.method, param.thisObject, param.args);
        } catch (Throwable e) {
            Alog.e("异常了", e);
        }
        return null;
    }

    public void showMessage(final String msg) {
        mPluginManager.getHandler().post(() -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());
    }
}
