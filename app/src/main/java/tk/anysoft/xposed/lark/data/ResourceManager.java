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

package tk.anysoft.xposed.lark.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;

import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XResourceManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XVersionManager;

/**
 * Created by sky on 2018/12/25.
 *
 * 主要针对访问插件资源的管理类
 */
public class ResourceManager implements XResourceManager {

    private Context mHookContext;
    private Context mPluginContext;
    private XVersionManager mVersionManager;
    private Theme mTheme;

    private ResourceManager(Build build) {
        mHookContext = build.mXPluginManager.getContext();
        mVersionManager = build.mXPluginManager.getVersionManager();
        initResourceManager();
    }

    /**
     * 初始化
     */
    private void initResourceManager() {

        // 暂时不处理
//        try {
//            mPluginContext = mWechatContext.createPackageContext(BuildConfig.APPLICATION_ID,
//                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
//        } catch (Throwable tr) {
//            Alog.e("加载插件资源异常", tr);
//        }
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
    public String getString(int resId) {
        return mPluginContext.getString(resId);
    }

    @Override
    public String getString(int resId, Object... formatArgs) {
        return mPluginContext.getString(resId, formatArgs);
    }

    @Override
    public Theme getTheme() {
        return mTheme;
    }

    public abstract class WeTheme implements Theme {

        private SparseArray<Object> mConfig = new SparseArray<>();

        public WeTheme() {
            // 加载配置
            loadConfig();
        }

        public abstract void loadConfig();

        void add(int key, int value) {
            mConfig.append(key, value);
        }

        @Override
        public int getColor(int id) {
            return (int) mConfig.get(id);
        }

        @Override
        public int getImage(int id) {
            return (int) mConfig.get(id);
        }
    }

    public static class Build {

        private XPluginManager mXPluginManager;

        public Build(XPluginManager xPluginManager) {
            mXPluginManager = xPluginManager;
        }

        public XResourceManager build() {
            return new ResourceManager(this);
        }
    }
}
