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

package com.sky.xposed.rimet;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.javax.MethodHook;
import com.sky.xposed.javax.XposedPlus;
import com.sky.xposed.javax.XposedUtil;
import com.sky.xposed.rimet.plugin.PluginManager;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by sky on 2019/3/14.
 */
public class Main implements IXposedHookLoadPackage, MethodHook.ThrowableCallback {

    @Override
    public void onThrowable(Throwable tr) {
        Alog.e("Throwable", tr);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpParam) throws Throwable {

        if (!Constant.Rimet.PACKAGE_NAME.equals(lpParam.packageName)) return;

        // 设置默认的参数
        XposedPlus.setDefaultInstance(new XposedPlus.Builder(lpParam)
                .throwableCallback(this)
                .build());

        // Hook
        XposedUtil
                .findMethod(
                        "com.alibaba.android.dingtalkbase.multidexsupport.DDApplication",
                        "onCreate")
                .before(param -> {

                    Application application = (Application) param.thisObject;
                    Context context = application.getApplicationContext();

                    if (TextUtils.equals(
                            "com.alibaba.android.rimet.LauncherApplication",
                            application.getClass().getName())) {

                        XPluginManager pluginManager = new PluginManager
                                .Build(context)
                                .setLoadPackageParam(lpParam)
                                .build();

                        // 开始处理加载的包
                        pluginManager.handleLoadPackage();
                    }
                });
    }
}
