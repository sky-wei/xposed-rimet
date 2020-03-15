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
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.core.base.AbstractComponent;
import com.sky.xposed.core.interfaces.XResourceManager;

/**
 * Created by sky on 2020-03-11.
 */
public class ResourceManager extends AbstractComponent implements XResourceManager {

    private Context mHookContext;
    private Context mPluginContext;
    private String mPluginPackageName;

    private ResourceManager(Build build) {
        mHookContext = build.mContext;
        mPluginPackageName = build.mPluginPackageName;
    }

    @Override
    public void initialize() {
        super.initialize();

        if (TextUtils.isEmpty(mPluginPackageName)) {
            // 为空时不处理
            return;
        }

        try {
            mPluginContext = mHookContext.createPackageContext(mPluginPackageName,
                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        } catch (Throwable tr) {
            Alog.e("加载插件资源异常", tr);
        }
    }

    @Override
    public Context getHookContext() {
        return mHookContext;
    }

    @Override
    public Context getPluginContext() {
        return mPluginContext;
    }

    @Override
    public Resources getHookResources() {
        return mHookContext.getResources();
    }

    @Override
    public Resources getPluginResources() {
        return mPluginContext.getResources();
    }

    @Override
    public Uri getPluginResource(int resId) {
        return ResourceUtil.resourceIdToUri(mPluginPackageName, resId);
    }

    @Override
    public String getString(int resId) {
        return mPluginContext.getString(resId);
    }

    @Override
    public String getString(int resId, Object... formatArgs) {
        return mPluginContext.getString(resId, formatArgs);
    }

    @Override
    public int getColor(int id) {
        return mPluginContext.getResources().getColor(id);
    }

    public static class Build {

        private Context mContext;
        private String mPluginPackageName;

        public Build(Context context) {
            mContext = context;
        }

        public Build setPluginPackageName(String pluginPackageName) {
            mPluginPackageName = pluginPackageName;
            return this;
        }

        public XResourceManager build() {

            if (TextUtils.isEmpty(mPluginPackageName)) {
                mPluginPackageName = mContext.getPackageName();
            }

            return new ResourceManager(this);
        }
    }
}
