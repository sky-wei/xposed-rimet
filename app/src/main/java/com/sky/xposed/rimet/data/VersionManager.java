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
import com.sky.xposed.rimet.data.config.RimetConfig4617;
import com.sky.xposed.rimet.data.config.RimetConfig4618;
import com.sky.xposed.rimet.data.config.RimetConfig4621;
import com.sky.xposed.rimet.data.config.RimetConfig4625;
import com.sky.xposed.rimet.plugin.interfaces.XConfig;
import com.sky.xposed.rimet.plugin.interfaces.XVersionManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sky on 2018/9/24.
 *
 * Hook应用相关版本变量管理类
 */
public class VersionManager implements XVersionManager {

    private final static Map<String, Class<? extends XConfig>> CONFIG_MAP = new LinkedHashMap<>();

    static {
        // 微信版本配置
        CONFIG_MAP.put("4.6.17", RimetConfig4617.class);
        CONFIG_MAP.put("4.6.18", RimetConfig4618.class);
        CONFIG_MAP.put("4.6.20", RimetConfig4618.class);
        CONFIG_MAP.put("4.6.21", RimetConfig4621.class);
        CONFIG_MAP.put("4.6.25", RimetConfig4625.class);
    }

    private XConfig mVersionConfig;
    private VersionInfo mVersionInfo;

    private VersionManager(Build build) {

        PackageInfo packageInfo = PackageUtil
                .getPackageInfo(build.mContext, Constant.Rimet.PACKAGE_NAME, 0);

        if (packageInfo != null) {
            // 保存包版本信息
            mVersionInfo = new VersionInfo();
            mVersionInfo.versionName = packageInfo.versionName;
            mVersionInfo.versionCode = packageInfo.versionCode;
        }
    }

    /**
     * 获取当前版本名
     * @return
     */
    @Override
    public String getVersionName() {
        return mVersionInfo != null ? mVersionInfo.versionName : "";
    }

    /**
     * 获取当前版本号
     * @return
     */
    @Override
    public int getVersionCode() {
        return mVersionInfo != null ? mVersionInfo.versionCode : 0;
    }

    /**
     * 判断Hook是否支持当前版本
     * @return
     */
    @Override
    public boolean isSupportVersion() {
        return isSupportVersion(getVersionName());
    }

    /**
     * 获取支持版本的配置信息,如果没有适配到返回Null
     * @return
     */
    @Override
    public XConfig getSupportConfig() {
        if (mVersionConfig == null) {
            mVersionConfig = getSupportConfig(CONFIG_MAP.get(getVersionName()));
        }
        return mVersionConfig;
    }

    @Override
    public List<String> getSupportVersion() {
        return new ArrayList<>(CONFIG_MAP.keySet());
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
    private XConfig getSupportConfig(Class<? extends XConfig> vClass) {

        if (vClass == null) return null;

        try {
            // 创建实例
            return vClass.newInstance();
        } catch (Throwable tr) {
            Alog.e("创建版本配置异常", tr);
        }
        return null;
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

        public Build(Context context) {
            mContext = context;
        }

        public XVersionManager build() {
            return new VersionManager(this);
        }
    }
}
