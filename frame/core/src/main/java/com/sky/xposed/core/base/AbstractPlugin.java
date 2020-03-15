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

package com.sky.xposed.core.base;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ToastUtil;
import com.sky.xposed.core.info.LoadPackage;
import com.sky.xposed.core.interfaces.XComponentManager;
import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPlugin;
import com.sky.xposed.core.interfaces.XPluginManager;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.core.interfaces.XVersionManager;
import com.sky.xposed.javax.MethodHook;
import com.sky.xposed.javax.XposedUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020-03-11.
 */
public abstract class AbstractPlugin implements XPlugin {

    private XCoreManager mCoreManager;
    private LoadPackage mLoadPackage;
    private XPreferences mPreferences;
    private XVersionManager mVersionManager;
    private XPluginManager mPluginManager;
    private XComponentManager mComponentManager;
    private XConfig mConfig;

    public AbstractPlugin(XCoreManager coreManager) {
        mCoreManager = coreManager;
        mLoadPackage = mCoreManager.getLoadPackage();
        mPreferences = mCoreManager.getDefaultPreferences();
        mVersionManager = mCoreManager.getVersionManager();
        mPluginManager = mCoreManager.getPluginManager();
        mComponentManager = mCoreManager.getComponentManager();
        mConfig = mVersionManager.getSupportConfig();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void release() {

    }

    public XCoreManager getCoreManager() {
        return mCoreManager;
    }

    public LoadPackage getLoadPackage() {
        return mLoadPackage;
    }

    public XPreferences getPreferences() {
        return mPreferences;
    }

    public XVersionManager getVersionManager() {
        return mVersionManager;
    }

    public XPluginManager getPluginManager() {
        return mPluginManager;
    }

    public XComponentManager getComponentManager() {
        return mComponentManager;
    }

    public ClassLoader getClassLoader() {
        return mLoadPackage.getClassLoader();
    }

    public void showMessage(String message) {
        ToastUtil.show(message);
    }

    public String getXString(int key) {
        return mConfig.get(key);
    }

    public XPlugin getPlugin(Class<? extends XPlugin> tClass) {
        return mPluginManager.getPlugin(tClass);
    }

    public boolean hasPlugin(Class<? extends XPlugin> tClass) {
        return mPluginManager.hasPlugin(tClass);
    }

    public boolean isEnable(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    public boolean isEnable(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public String getPString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public String getPString(String key) {
        return mPreferences.getString(key);
    }

    public int getPInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    public int getPInt(String key) {
        return mPreferences.getInt(key);
    }

    public float getPFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    public float getPFloat(String key) {
        return mPreferences.getFloat(key);
    }

    public long getPLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    public long getPLong(String key) {
        return mPreferences.getLong(key);
    }

    public double getPDouble(String key, double defValue) {
        return mPreferences.getDouble(key, defValue);
    }

    public Class findClass(String className) {
        return XposedUtil.findClass(className);
    }

    public Class findClass(int className) {
        return findClass(getXString(className));
    }

    public Class findClass(ClassLoader classLoader, String className) {
        return XposedHelpers.findClass(className, classLoader);
    }

    public Class findClass(ClassLoader classLoader, int className) {
        return findClass(classLoader, getXString(className));
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

    public Object callMethod(Object obj, String methodName, Object... args) {
        return XposedHelpers.callMethod(obj, methodName, args);
    }

    public Object callMethod(Object obj, int methodName, Object... args) {
        return callMethod(obj, getXString(methodName), args);
    }

    public Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        return XposedHelpers.callStaticMethod(clazz, methodName, args);
    }

    public Object callStaticMethod(Class<?> clazz, int methodName, Object... args) {
        return callStaticMethod(clazz, getXString(methodName), args);
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
}
