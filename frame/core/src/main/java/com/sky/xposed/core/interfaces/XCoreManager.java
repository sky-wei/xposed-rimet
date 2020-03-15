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

package com.sky.xposed.core.interfaces;

import com.sky.xposed.core.info.LoadPackage;
import com.sky.xposed.core.info.PluginPackage;

/**
 * Created by sky on 2020-03-11.
 */
public interface XCoreManager {

    /**
     * 获取加载的包信息
     * @return
     */
    LoadPackage getLoadPackage();

    /**
     * 获取插件包信息
     * @return
     */
    PluginPackage getPluginPackage();

    /**
     * 获取组件管理
     * @return
     */
    XComponentManager getComponentManager();

    /**
     * 获取版本管理
     * @return
     */
    XVersionManager getVersionManager();

    /**
     * 获取插件管理
     * @return
     */
    XPluginManager getPluginManager();

    /**
     * 获取资源管理(字符串与插件包资源)
     * @return
     */
    XResourceManager getResourceManager();

    /**
     * 获取默认的XPreferences
     */
    XPreferences getDefaultPreferences();

    /**
     * 获取指定名称的XPreferences
     */
    XPreferences getPreferencesByName(String name);

    /**
     * 获取组件
     * @param name
     * @param <T>
     * @return
     */
    <T extends XComponent> T getComponent(Class<T> name);


    /**
     * 开始处理加载的插件
     */
    void loadPlugins();

    /**
     * 释放
     */
    void release();


    /**
     * 监听器
     */
    interface XCoreListener {

        /**
         * 初始化完成
         * @param coreManager
         */
        void onInitComplete(XCoreManager coreManager);
    }
}
