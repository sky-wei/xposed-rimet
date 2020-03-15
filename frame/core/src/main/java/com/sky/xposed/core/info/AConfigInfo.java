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

package com.sky.xposed.core.info;

import com.sky.xposed.core.interfaces.XConfig;

/**
 * Created by sky on 2020-01-16.
 */
public final class AConfigInfo {

    private final String mPackageName;
    private final String mVersionName;
    private final int mVersionCode;
    private final Class<? extends XConfig> mConfigClass;

    public AConfigInfo(String packageName, String versionName, int versionCode, Class<? extends XConfig> configClass) {
        mPackageName = packageName;
        mVersionName = versionName;
        mVersionCode = versionCode;
        mConfigClass = configClass;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public Class<? extends XConfig> getConfigClass() {
        return mConfigClass;
    }
}
