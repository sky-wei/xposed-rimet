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

package com.sky.xposed.ui.util;

import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPreferences;

/**
 * Created by sky on 2020-02-18.
 */
public final class CoreUtil {

    private static XCoreManager sCoreManager;

    public static void init(XCoreManager coreManager) {
        sCoreManager = coreManager;
    }

    public static XCoreManager getCoreManager() {
        return sCoreManager;
    }

    public static XPreferences getDefaultPreferences() {
        return sCoreManager.getDefaultPreferences();
    }
}
