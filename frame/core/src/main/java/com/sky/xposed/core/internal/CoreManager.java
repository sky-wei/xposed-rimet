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

package com.sky.xposed.core.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.core.BuildConfig;
import com.sky.xposed.core.info.LoadPackage;
import com.sky.xposed.core.info.PluginPackage;
import com.sky.xposed.core.interfaces.XComponent;
import com.sky.xposed.core.interfaces.XComponentManager;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPluginManager;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.core.interfaces.XResourceManager;
import com.sky.xposed.core.interfaces.XVersionManager;

/**
 * Created by sky on 2020-01-11.
 */
public final class CoreManager implements XCoreManager {

    private final Context mContext;
    private final LoadPackage mLoadPackage;
    private final PluginPackage mPluginPackage;
    private final XComponentManager mComponentManager;

    private XPreferences mPreferences;
    private XPluginManager mPluginManager;
    private XVersionManager mVersionManager;

    private CoreManager(Build build) {

        Alog.setDebug(BuildConfig.DEBUG);

        mContext = build.mContext;

        mLoadPackage = new LoadPackage.Build(build.mContext)
                .setHandler(new CoreHandler())
                .setPackageName(build.mContext.getPackageName())
                .setProcessName(build.mProcessName)
                .setClassLoader(build.mClassLoader)
                .build();

        mPluginPackage = new PluginPackage.Build()
                .setPackageName(build.mPluginPackageName)
                .build();

        mComponentManager = new ComponentManager
                .Build(this)
                .setFactory(build.mComponentFactory)
                .build();

        mPreferences = getComponent(XPreferences.class);
        mVersionManager = getComponent(XVersionManager.class);
        mPluginManager = getComponent(XPluginManager.class);

        if (build.mXCoreListener != null) {
            build.mXCoreListener.onInitComplete(this);
        }
    }

    @Override
    public LoadPackage getLoadPackage() {
        return mLoadPackage;
    }

    @Override
    public PluginPackage getPluginPackage() {
        return mPluginPackage;
    }

    @Override
    public XVersionManager getVersionManager() {
        return mVersionManager;
    }

    @Override
    public XPluginManager getPluginManager() {
        return mPluginManager;
    }

    @Override
    public XResourceManager getResourceManager() {
        return getComponent(XResourceManager.class);
    }

    @Override
    public XPreferences getDefaultPreferences() {
        return mPreferences;
    }

    @Override
    public XPreferences getPreferencesByName(String name) {
        return mPreferences.getPreferences(name);
    }

    @Override
    public XComponentManager getComponentManager() {
        return mComponentManager;
    }

    @Override
    public <T extends XComponent> T getComponent(Class<T> name) {
        return mComponentManager.get(name);
    }

    @Override
    public void loadPlugins() {
        mPluginManager.loadPlugins();
    }

    @Override
    public void release() {
        mComponentManager.release();
        mPreferences = null;
        mPluginManager = null;
    }

    @SuppressLint("HandlerLeak")
    private static final class CoreHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


    public static final class Build {

        private Context mContext;
        private String mProcessName;
        private String mPluginPackageName;
        private ClassLoader mClassLoader;
        private XComponentManager.Factory mComponentFactory;
        private XCoreListener mXCoreListener;

        public Build(Context context) {
            mContext = context;
        }

        public Build setProcessName(String processName) {
            mProcessName = processName;
            return this;
        }

        public Build setPluginPackageName(String pluginPackageName) {
            mPluginPackageName = pluginPackageName;
            return this;
        }

        public Build setClassLoader(ClassLoader classLoader) {
            mClassLoader = classLoader;
            return this;
        }

        public Build setComponentFactory(XComponentManager.Factory factory) {
            mComponentFactory = factory;
            return this;
        }

        public Build setCoreListener(XCoreListener listener) {
            mXCoreListener = listener;
            return this;
        }

        public XCoreManager build() {

            if (mComponentFactory == null) {
                throw new IllegalArgumentException("参数不能为空");
            }
            return new CoreManager(this);
        }
    }
}
