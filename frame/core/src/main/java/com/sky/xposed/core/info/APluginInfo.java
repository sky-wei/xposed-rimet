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

import com.sky.xposed.core.interfaces.XPlugin;

/**
 * Created by sky on 2020-01-16.
 */
public final class APluginInfo {

    private final int mBegin;
    private final int mEnd;
    private final int[] mFilter;
    private final String mPackageName;
    private final int mPriority;
    private final boolean mDebug;
    private final Class<? extends XPlugin> mPluginClass;

    public APluginInfo(int begin, int end, int[] filter, String packageName,
                       int priority, boolean debug, Class<? extends XPlugin> pluginClass) {
        mBegin = begin;
        mEnd = end;
        mFilter = filter;
        mPackageName = packageName;
        mPriority = priority;
        mDebug = debug;
        mPluginClass = pluginClass;
    }

    public int getBegin() {
        return mBegin;
    }

    public int getEnd() {
        return mEnd;
    }

    public int[] getFilter() {
        return mFilter;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public int getPriority() {
        return mPriority;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public Class<? extends XPlugin> getPluginClass() {
        return mPluginClass;
    }
}
