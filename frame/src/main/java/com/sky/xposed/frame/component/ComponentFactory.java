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

package com.sky.xposed.frame.component;

import com.sky.xposed.frame.interfaces.XComponent;
import com.sky.xposed.frame.interfaces.XComponentManager;
import com.sky.xposed.frame.interfaces.XCoreManager;

/**
 * Created by sky on 2020-03-11.
 */
public class ComponentFactory implements XComponentManager.Factory {

    @Override
    public <T extends XComponent> T create(XCoreManager coreManager, Class<T> name) {

//        if (XPreferences.class == name) {
//            // 创建XPreferences
//            return name.cast(createPreferences(context));
//        } else if (XVersionManager.class == name) {
//            // 创建XVersionManager
//            return name.cast(createVersionManager(context));
//        } else if (XResourceManager.class == name) {
//            // 创建XResourceManager
//            return name.cast(createResourceManager(context));
//        } else if (XPluginManager.class == name) {
//            // 创建XPluginManager
//            return name.cast(createPluginManager(context));
//        }
        return null;
    }
}
