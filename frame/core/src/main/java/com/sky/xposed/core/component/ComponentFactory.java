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

import com.sky.xposed.core.interfaces.XComponent;
import com.sky.xposed.core.interfaces.XComponentManager;
import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPlugin;
import com.sky.xposed.core.interfaces.XPluginManager;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.core.interfaces.XResourceManager;
import com.sky.xposed.core.interfaces.XVersionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2020-03-11.
 */
public class ComponentFactory implements XComponentManager.Factory {

    @Override
    public <T extends XComponent> T create(XCoreManager coreManager, Class<T> name) {

        if (XPreferences.class == name) {
            // 创建XPreferences
            return name.cast(createPreferences(coreManager));
        } else if (XVersionManager.class == name) {
            // 创建XVersionManager
            return name.cast(createVersionManager(coreManager));
        } else if (XResourceManager.class == name) {
            // 创建XResourceManager
            return name.cast(createResourceManager(coreManager));
        } else if (XPluginManager.class == name) {
            // 创建XPluginManager
            return name.cast(createPluginManager(coreManager));
        }
        return null;
    }

    protected XPreferences createPreferences(XCoreManager coreManager) {
        return new CorePreferences.Build(
                createPreferencesFactory(coreManager.getLoadPackage().getContext()))
                .setDefaultName("default_preferences")
                .build();
    }

    protected XPreferences.Factory createPreferencesFactory(Context context) {
        return name -> context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    protected XVersionManager createVersionManager(XCoreManager coreManager) {
        return new VersionManager.Build(coreManager.getLoadPackage().getContext())
                .setFactory(new XVersionManager.Factory() {
                    @Override
                    public List<Class<? extends XConfig>> supportVersion() {
                        return getVersionData();
                    }
                })
                .build();
    }

    protected XResourceManager createResourceManager(XCoreManager coreManager) {

        final Context context = coreManager.getLoadPackage().getContext();
        final String pluginPackageName = coreManager.getPluginPackage().getPackageName();

        return new ResourceManager.Build(context)
                .setPluginPackageName(pluginPackageName)
                .build();
    }

    protected XPluginManager createPluginManager(XCoreManager coreManager) {
        return new PluginManager.Build(coreManager)
                .setFactory(this::getPluginData)
                .build();
    }

    protected List<Class<? extends XConfig>> getVersionData() {
        return new ArrayList<>();
    }

    protected List<Class<? extends XPlugin>> getPluginData() {
        return new ArrayList<>();
    }
}
