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

package com.sky.xposed.rimet.data;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.PackageUtil;
import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.cache.ICacheManager;
import com.sky.xposed.rimet.data.cache.IRimetCache;
import com.sky.xposed.rimet.data.cache.RimetCache;
import com.sky.xposed.rimet.data.config.CacheRimetConfig;
import com.sky.xposed.rimet.data.config.RimetConfig;
import com.sky.xposed.rimet.data.config.RimetConfig4617;
import com.sky.xposed.rimet.data.config.RimetConfig4618;
import com.sky.xposed.rimet.data.config.RimetConfig4621;
import com.sky.xposed.rimet.data.config.RimetConfig4625;
import com.sky.xposed.rimet.data.config.RimetConfig4629;
import com.sky.xposed.rimet.data.config.RimetConfig4630;
import com.sky.xposed.rimet.data.config.RimetConfig4633;
import com.sky.xposed.rimet.data.config.RimetConfig4636;
import com.sky.xposed.rimet.data.config.RimetConfig4637;
import com.sky.xposed.rimet.data.config.RimetConfig4700;
import com.sky.xposed.rimet.data.config.RimetConfig4707;
import com.sky.xposed.rimet.data.config.RimetConfig4711;
import com.sky.xposed.rimet.data.config.RimetConfig4716;
import com.sky.xposed.rimet.data.config.RimetConfig4725;
import com.sky.xposed.rimet.data.config.RimetConfig4730;
import com.sky.xposed.rimet.data.model.ConfigModel;
import com.sky.xposed.rimet.data.model.VersionModel;
import com.sky.xposed.rimet.plugin.interfaces.XConfig;
import com.sky.xposed.rimet.plugin.interfaces.XConfigManager;
import com.sky.xposed.rimet.plugin.interfaces.XVersionManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by sky on 2018/9/24.
 *
 * Hook应用相关版本变量管理类
 */
public class VersionManager implements XVersionManager {

    private XConfig mVersionConfig;
    private XVersionManager mInternalVersionManager;
    private XVersionManager mCacheVersionManager;

    private VersionManager(Build build) {

        PackageInfo packageInfo = PackageUtil
                .getPackageInfo(build.mContext, Constant.Rimet.PACKAGE_NAME, 0);

        VersionInfo versionInfo = null;

        if (packageInfo != null) {
            // 保存包版本信息
            versionInfo = new VersionInfo();
            versionInfo.versionName = packageInfo.versionName;
            versionInfo.versionCode = packageInfo.versionCode;
        }

        // 创建内部版本配置管理
        mInternalVersionManager = new InternalVersionManager(versionInfo);
        // 创建缓存版本配置管理
        mCacheVersionManager = new CacheVersionManager(
                versionInfo, new RimetCache(build.mConfigManager, build.mCacheManager));
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
        return mInternalVersionManager.isSupportVersion() || mCacheVersionManager.isSupportVersion();
    }

    /**
     * 获取支持版本的配置信息,如果没有适配到返回Null
     * @return
     */
    @Override
    public XConfig getSupportConfig() {
        if (mVersionConfig == null) {
            mVersionConfig = mInternalVersionManager.getSupportConfig();
        }
        if (mVersionConfig == null) {
            mVersionConfig = mCacheVersionManager.getSupportConfig();
        }
        return mVersionConfig;
    }

    @Override
    public Set<String> getSupportVersion() {

        Set<String> version = new LinkedHashSet<>();
        version.addAll(mInternalVersionManager.getSupportVersion());
        version.addAll(mCacheVersionManager.getSupportVersion());

        return version;
    }

    @Override
    public void clearVersionConfig() {
        mInternalVersionManager.clearVersionConfig();
        mCacheVersionManager.clearVersionConfig();
    }


    /**
     * 程序内置的版本管理
     */
    private static final class InternalVersionManager implements XVersionManager {

        private final static Map<String, Class<? extends RimetConfig>> CONFIG_MAP = new LinkedHashMap<>();

        static {
            // 钉钉版本配置
            CONFIG_MAP.put("4.6.17", RimetConfig4617.class);
            CONFIG_MAP.put("4.6.18", RimetConfig4618.class);
            CONFIG_MAP.put("4.6.20", RimetConfig4618.class);
            CONFIG_MAP.put("4.6.21", RimetConfig4621.class);
            CONFIG_MAP.put("4.6.25", RimetConfig4625.class);
            CONFIG_MAP.put("4.6.29", RimetConfig4629.class);
            CONFIG_MAP.put("4.6.30", RimetConfig4630.class);
            CONFIG_MAP.put("4.6.33", RimetConfig4633.class);
            CONFIG_MAP.put("4.6.36", RimetConfig4636.class);
            CONFIG_MAP.put("4.6.37", RimetConfig4637.class);
            CONFIG_MAP.put("4.7.0", RimetConfig4700.class);
            CONFIG_MAP.put("4.7.7", RimetConfig4707.class);
            CONFIG_MAP.put("4.7.11", RimetConfig4711.class);
            CONFIG_MAP.put("4.7.16", RimetConfig4716.class);
            CONFIG_MAP.put("4.7.25", RimetConfig4725.class);
            CONFIG_MAP.put("4.7.30", RimetConfig4730.class);
        }

        private VersionInfo mVersionInfo;

        public InternalVersionManager(VersionInfo versionInfo) {
            mVersionInfo = versionInfo;
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
            return isSupportVersion(getVersionName());
        }

        @Override
        public XConfig getSupportConfig() {
            return getSupportConfig(CONFIG_MAP.get(getVersionName()));
        }

        @Override
        public Set<String> getSupportVersion() {
            return CONFIG_MAP.keySet();
        }

        @Override
        public void clearVersionConfig() {
            // 不需要清除配置
        }

        /**
         * 判断Hook是否支持当前版本
         * @return
         */
        public boolean isSupportVersion(String versionName) {
            return CONFIG_MAP.containsKey(versionName);
        }

        /**
         * 创建指定的配置类
         * @param vClass
         * @return
         */
        private XConfig getSupportConfig(Class<? extends RimetConfig> vClass) {

            if (vClass == null) return null;

            try {
                // 创建实例
                return vClass.newInstance().loadConfig();
            } catch (Throwable tr) {
                Alog.e("创建版本配置异常", tr);
            }
            return null;
        }
    }


    /**
     * 本地缓存的版本配置管理
     */
    private static final class CacheVersionManager implements XVersionManager {

        private VersionInfo mVersionInfo;
        private IRimetCache mRimetCache;

        public CacheVersionManager(VersionInfo versionInfo, IRimetCache iRimetCache) {
            mVersionInfo = versionInfo;
            mRimetCache = iRimetCache;
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
            return getSupportVersion().contains(getVersionName());
        }

        @Override
        public XConfig getSupportConfig() {

            Map<String, String> version = getSupportVersionMap();

            if (!version.containsKey(getVersionName())) {
                // 本地配置无效
                return null;
            }

            // 获取版本号(这个版本号不是钉钉的版本号)
            return getSupportConfig(version.get(getVersionName()));
        }

        @Override
        public Set<String> getSupportVersion() {
            return getSupportVersionMap().keySet();
        }

        @Override
        public void clearVersionConfig() {
            mRimetCache.clearVersionConfig();
        }

        private Map<String, String> getSupportVersionMap() {

            // 获取本地版本
            VersionModel model = mRimetCache.getSupportVersion();

            if (model == null || model.getSupportConfig() == null) {
                // 本地没有配置
                return new HashMap<>();
            }
            return model.getSupportConfig();
        }

        private XConfig getSupportConfig(String versionCode) {

            // 加载本地版本配置
            ConfigModel model = mRimetCache.getVersionConfig(versionCode);

            if (model == null || model.getVersionConfig() == null) {
                // 本地配置无效
                return null;
            }
            return new CacheRimetConfig(model).loadConfig();
        }
    }

    /**
     * 版本信息
     */
    private final class VersionInfo {

        protected String versionName;
        protected int versionCode;
    }

    public static final class Build {

        private Context mContext;
        private XConfigManager mConfigManager;
        private ICacheManager mCacheManager;

        public Build(Context context) {
            mContext = context;
        }

        public Build setConfigManager(XConfigManager xConfigManager) {
            mConfigManager = xConfigManager;
            return this;
        }

        public Build setCacheManager(ICacheManager iCacheManager) {
            mCacheManager = iCacheManager;
            return this;
        }

        public XVersionManager build() {
            return new VersionManager(this);
        }
    }
}
