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

package com.sky.xposed.core.component;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.sky.xposed.annotations.AConfig;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.PackageUtil;
import com.sky.xposed.core.base.AbstractComponent;
import com.sky.xposed.core.info.AConfigInfo;
import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XVersionManager;
import com.sky.xposed.core.util.FilterUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sky on 2020-01-13.
 */
public class VersionManager extends AbstractComponent implements XVersionManager {

    private final Context mContext;
    private final String mHookPackageName;
    private final XVersionManager.Factory mFactory;

    private XConfig mSupportConfig;
    private XVersionManager mInternalVersionManager;

    private VersionManager(Build build) {
        mContext = build.mContext;
        mHookPackageName = build.mHookPackageName;
        mFactory = build.mFactory;
    }

    @Override
    public void initialize() {
        super.initialize();

        PackageInfo packageInfo = PackageUtil
                .getPackageInfo(mContext, mHookPackageName, 0);

        VersionInfo versionInfo = null;

        if (packageInfo != null) {
            // 保存包版本信息
            versionInfo = new VersionInfo();
            versionInfo.packageName = packageInfo.packageName;
            versionInfo.versionName = packageInfo.versionName;
            versionInfo.versionCode = packageInfo.versionCode;
        }

        // 创建内部版本配置管理
        mInternalVersionManager = new InternalVersionManager(versionInfo, mFactory);
        mInternalVersionManager.initialize();
    }

    @Override
    public void release() {
        super.release();

        // 释放
        mSupportConfig = null;
        mInternalVersionManager.release();
    }

    /**
     * 获取当前版本名
     * @return
     */
    @Override
    public String getVersionName() {
        return mInternalVersionManager.getVersionName();
    }

    /**
     * 获取当前版本号
     * @return
     */
    @Override
    public int getVersionCode() {
        return mInternalVersionManager.getVersionCode();
    }

    /**
     * 判断Hook是否支持当前版本
     * @return
     */
    @Override
    public boolean isSupportVersion() {
        return mInternalVersionManager.isSupportVersion();
    }

    /**
     * 获取支持版本的配置信息,如果没有适配到返回Null
     * @return
     */
    @Override
    public XConfig getSupportConfig() {
        if (mSupportConfig == null) {
            mSupportConfig = mInternalVersionManager.getSupportConfig();
        }
        return mSupportConfig;
    }

    @Override
    public Set<String> getSupportVersion() {
        return mInternalVersionManager.getSupportVersion();
    }

    @Override
    public void clearVersionConfig() {
        mInternalVersionManager.clearVersionConfig();
    }

    /**
     * 程序内置的版本管理
     */
    private static final class InternalVersionManager extends AbstractComponent implements XVersionManager {

        private final Map<String, Class<? extends XConfig>> mStringClassMap = new LinkedHashMap<>();

        private final VersionInfo mVersionInfo;
        private final XVersionManager.Factory mFactory;

        private InternalVersionManager(VersionInfo versionInfo, XVersionManager.Factory factory) {
            mVersionInfo = versionInfo;
            mFactory = factory;
        }

        @Override
        public void initialize() {
            super.initialize();

            List<AConfigInfo> configInfos = FilterUtil
                    .filterList(getConfigInfos(mFactory.supportVersion()), data -> {
                String packageName = data.getPackageName();
                return TextUtils.isEmpty(packageName)
                        || TextUtils.equals(mVersionInfo.packageName, packageName);
            });

            for (AConfigInfo info : configInfos) {
                // 开始处理版本信息
                handlerConfigInfo(info);
            }
        }

        private List<AConfigInfo> getConfigInfos(List<Class<? extends XConfig>> list) {

            List<AConfigInfo> configInfos = new ArrayList<>();

            for (Class<? extends XConfig> tClass: list) {

                AConfigInfo info = getConfigInfo(tClass);

                if (info != null) configInfos.add(info);
            }
            return configInfos;
        }

        private AConfigInfo getConfigInfo(Class<? extends XConfig> tClass) {

            AConfig config = tClass.getAnnotation(AConfig.class);

            return config != null ? new AConfigInfo(
                    config.packageName(),
                    config.versionName(),
                    config.versionCode(),
                    tClass)
                    : null;
        }

        private void handlerConfigInfo(AConfigInfo info) {

            final String versionName = info.getVersionName();
            final int versionCode = info.getVersionCode();

            if (TextUtils.isEmpty(versionName) && versionCode <= 0) {
                // 没有版本信息,使用当前版本信息
                String key = mVersionInfo.versionName + "_" + mVersionInfo.versionCode;
                mStringClassMap.put(key, info.getConfigClass());
                return;
            }

            String key = versionCode > 0 ? versionName + "_" + versionCode : versionName;
            mStringClassMap.put(key, info.getConfigClass());
        }

        @Override
        public void release() {
            super.release();
            mStringClassMap.clear();
        }

        @Override
        public String getVersionName() {
            return mVersionInfo != null ? mVersionInfo.versionName : "";
        }

        @Override
        public int getVersionCode() {
            return mVersionInfo != null ? mVersionInfo.versionCode : 0;
        }

        @Override
        public boolean isSupportVersion() {
            return isSupportVersion(getVersionFullName()) || isSupportVersion(getVersionName());
        }

        @Override
        public XConfig getSupportConfig() {

            final String name = getVersionFullName();

            if (mStringClassMap.containsKey(name)) {
                // 优先使用版本名+版本号适配
                return getSupportConfig(mStringClassMap.get(name));
            }
            // 其后使用版本名来匹配
            return getSupportConfig(mStringClassMap.get(getVersionName()));
        }

        @Override
        public Set<String> getSupportVersion() {
            return mStringClassMap.keySet();
        }

        @Override
        public void clearVersionConfig() {
            // 不需要清除配置
        }

        /**
         * 判断Hook是否支持当前版本
         * @return
         */
        private boolean isSupportVersion(String versionName) {
            return mStringClassMap.containsKey(versionName);
        }

        /**
         * 获取版本号加版本号的完整名称
         * @return
         */
        private String getVersionFullName() {
            return mVersionInfo.versionName + "_" + mVersionInfo.versionCode;
        }

        /**
         * 创建指定的配置类
         * @param vClass
         * @return
         */
        private XConfig getSupportConfig(Class<? extends XConfig> vClass) {

            if (vClass == null) return null;

            try {
                // 创建实例
                return vClass.newInstance().reload();
            } catch (Throwable tr) {
                Alog.e("创建版本配置异常", tr);
            }
            return null;
        }
    }

    /**
     * 版本信息
     */
    private final class VersionInfo {

        String packageName;
        String versionName;
        int versionCode;
    }


    public static final class Build {

        private Context mContext;
        private String mHookPackageName;
        private XVersionManager.Factory mFactory;

        public Build(Context context) {
            mContext = context;
        }

        public Build setHookPackageName(String hookPackageName) {
            mHookPackageName = hookPackageName;
            return this;
        }

        public Build setFactory(Factory factory) {
            mFactory = factory;
            return this;
        }

        public XVersionManager build() {

            if (mFactory == null) {
                throw new IllegalArgumentException("参数不能为空");
            }

            if (TextUtils.isEmpty(mHookPackageName)) {
                mHookPackageName = mContext.getPackageName();
            }
            return new VersionManager(this);
        }
    }
}
