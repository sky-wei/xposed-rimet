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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by sky on 2020-03-11.
 */
public class LoadPackage {

    private final Context context;
    private final Handler handler;
    private final String packageName;
    private final String processName;
    private final ClassLoader classLoader;
    private final boolean mainProcess;

    private LoadPackage(Build build) {
        this.context = build.context;
        this.handler = build.handler;
        this.packageName = build.packageName;
        this.processName = build.processName;
        this.classLoader = build.classLoader;
        this.mainProcess = TextUtils.equals(packageName, processName);
    }

    public Context getContext() {
        return context;
    }

    public Handler getHandler() {
        return handler;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getProcessName() {
        return processName;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ApplicationInfo getAppInfo() {
        return context.getApplicationInfo();
    }

    public boolean isMainProcess() {
        return mainProcess;
    }

    public static class Build {

        private Context context;
        private Handler handler;
        private String packageName;
        private String processName;
        private ClassLoader classLoader;

        public Build(Context context) {
            this.context = context;
        }

        public Build setHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public Build setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Build setProcessName(String processName) {
            this.processName = processName;
            return this;
        }

        public Build setClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public LoadPackage build() {
            return new LoadPackage(this);
        }
    }
}
