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

import android.text.TextUtils;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.core.BuildConfig;
import com.sky.xposed.core.base.AbstractComponent;
import com.sky.xposed.core.info.APluginInfo;
import com.sky.xposed.core.info.LoadPackage;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPlugin;
import com.sky.xposed.core.interfaces.XPluginManager;
import com.sky.xposed.core.util.FilterUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020-01-13.
 */
public class PluginManager extends AbstractComponent implements XPluginManager {

    private Map<Class<? extends XPlugin>, XPlugin> mXPlugins = new HashMap<>();

    private XCoreManager mCoreManager;
    private XPluginManager.Factory mFactory;
    private XPluginManager.Loader mLoader;

    private PluginManager(Build build) {
        mCoreManager = build.mCoreManager;
        mFactory = build.mFactory;
        mLoader = build.mLoader;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public XPlugin getPlugin(Class<? extends XPlugin> tClass) {
        return mXPlugins.get(tClass);
    }

    @Override
    public boolean hasPlugin(Class<? extends XPlugin> tClass) {
        return mXPlugins.containsKey(tClass);
    }

    @Override
    public List<XPlugin> getPlugins(Filter filter) {

        List<XPlugin> list = new ArrayList<>();

        for (XPlugin value : mXPlugins.values()) {
            if (filter.accept(value)) {
                list.add(value);
            }
        }
        return list;
    }

    @Override
    public void loadPlugins() {

        if (mLoader == null) {
            mLoader = new InternalPluginLoader(mCoreManager);
        }

        // 添加的列表中
        mXPlugins.putAll(mLoader.loadPlugin(mFactory));

        for (XPlugin plugin : mXPlugins.values()) {
            // 开始处理
            handlePlugin(plugin);
        }
    }

    /**
     * 开始处理需要Hook的插件
     * @param
     */
    private void handlePlugin(XPlugin plugin) {

        try {
            // 开始处理
            plugin.hook();
        } catch (Throwable tr) {
            Alog.e("Hook异常", tr);
        }
    }

    /**
     * 设置插件加载类
     */
    private static final class InternalPluginLoader implements XPluginManager.Loader {

        private XCoreManager mCoreManager;
        private LoadPackage mLoadPackage;

        private int mVersionCode;
        private int mProcessType;

        private InternalPluginLoader(XCoreManager coreManager) {
            mCoreManager = coreManager;
            mLoadPackage = coreManager.getLoadPackage();

            mVersionCode = coreManager.getVersionManager().getVersionCode();
            mProcessType = mLoadPackage.isMainProcess() ? Process.MAIN : Process.OTHER;
        }

        @Override
        public Map<Class<? extends XPlugin>, XPlugin> loadPlugin(Factory factory) {

            List<APluginInfo> pluginInfos = getLoaderPlugins(factory);

            // 排序
            Collections.sort(pluginInfos,
                    (o1, o2) -> o1.getPriority() - o2.getPriority());

            return createPlugin(pluginInfos);
        }

        private List<APluginInfo> getLoaderPlugins(Factory factory) {

            return FilterUtil.filterList(getPluginInfos(factory.pluginList()), data -> {

                final String packageName = data.getPackageName();

                if (!BuildConfig.DEBUG && data.isDebug()) {
                    // 非Debug状态,不需要处理
                    Alog.d("调试插件不需要处理: " + data.getPluginClass());
                    return false;
                }

                if (!TextUtils.isEmpty(packageName)
                        && !TextUtils.equals(mLoadPackage.getPackageName(), packageName)) {
                    // 包名不匹配不需要处理
                    Alog.d("包名不匹配不需要处理: " + data.getPluginClass());
                    return false;
                }

                if (!matchVersion(mVersionCode, data.getBegin(), data.getEnd())) {
                    Alog.d("版本不匹配不需要处理: " + mVersionCode  + ", "
                            + data.getBegin() + ", " + data.getEnd() + ", " + data.getPluginClass());
                    return false;
                }

                if (!matchProcess(data.getFilter(), mProcessType)) {
                    Alog.d("进程不匹配不需要处理: "
                            + mLoadPackage.getProcessName() + ", " + data.getPluginClass());
                    return false;
                }

                return true;
            });
        }

        private List<APluginInfo> getPluginInfos(List<Class<? extends XPlugin>> list) {

            List<APluginInfo> pluginInfos = new ArrayList<>();

            for (Class<? extends XPlugin> tClass: list) {

                APluginInfo info = getPluginInfo(tClass);

                if (info != null) pluginInfos.add(info);
            }
            return pluginInfos;
        }

        private APluginInfo getPluginInfo(Class<? extends XPlugin> tClass) {

            APlugin plugin = tClass.getAnnotation(APlugin.class);

            return plugin != null ? new APluginInfo(
                    plugin.begin(),
                    plugin.end(),
                    plugin.filter(),
                    plugin.packageName(),
                    plugin.priority(),
                    plugin.debug(),
                    tClass)
                    : null;
        }

        private Map<Class<? extends XPlugin>, XPlugin> createPlugin(List<APluginInfo> pluginInfos) {

            Map<Class<? extends XPlugin>, XPlugin> xPluginMap = new HashMap<>();

            for (APluginInfo  info : pluginInfos) {

                XPlugin plugin = createPlugin(info);

                if (plugin != null) {
                    // 添加到表中
                    xPluginMap.put(info.getPluginClass(), plugin);
                }
            }

            return xPluginMap;
        }

        private XPlugin createPlugin(APluginInfo info) {

            try {
                // 创建插件类
                XPlugin plugin = (XPlugin) XposedHelpers
                        .newInstance(info.getPluginClass(), mCoreManager);
                plugin.initialize();
                return plugin;
            } catch (Throwable tr) {
                Alog.e("创建插件异常", tr);
            }
            return null;
        }

        /**
         * 匹配版本
         * @param version
         * @param begin
         * @param end
         * @return
         */
        private boolean matchVersion(int version, int begin, int end) {

            if (begin == -1 && end == -1) {
                // 全版本支持
                return true;
            }

            if (end == -1) {
                // 支持>=begin的所有版本
                return version >= begin;
            } else if (begin == -1) {
                // 支持<end的所有版本
                return version < end;
            } else {
                // 支持版本之间的
                return version >= begin && version < end;
            }
        }

        /**
         * 是否匹配进程
         * @param filter
         * @param type
         * @return
         */
        private boolean matchProcess(int[] filter, int type) {

            if (filter == null || filter.length <= 0) {
                // 没有过滤不需要处理
                return true;
            }

            for (int value : filter) {
                // 匹配一个即可
                if (value == type || Process.ALL == value) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Build {

        private XCoreManager mCoreManager;
        private XPluginManager.Factory mFactory;
        private XPluginManager.Loader mLoader;

        public Build(XCoreManager coreManager) {
            mCoreManager = coreManager;
        }

        public Build setFactory(Factory factory) {
            mFactory = factory;
            return this;
        }

        public Build setLoader(Loader loader) {
            mLoader = loader;
            return this;
        }

        public XPluginManager build() {

            if (mFactory == null) {
                throw new IllegalArgumentException("参数不能为空");
            }

            return new PluginManager(this);
        }
    }
}
