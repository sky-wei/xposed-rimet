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

package com.sky.xposed.frame;

import android.app.Application;
import android.content.Context;

import com.sky.xposed.core.adapter.CoreListenerAdapter;
import com.sky.xposed.core.adapter.ThrowableAdapter;
import com.sky.xposed.core.component.ComponentFactory;
import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPlugin;
import com.sky.xposed.core.internal.CoreManager;
import com.sky.xposed.javax.XposedPlus;
import com.sky.xposed.javax.XposedUtil;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by sky on 2020-03-14.
 */
public class Main implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpParam) throws Throwable {

        if (!"con.xxx.xxx.package".equals(lpParam.packageName)) {
            // 不需要处理
            return;
        }

        // 初始化XposedPlus
        XposedPlus.setDefaultInstance(new XposedPlus.Builder(lpParam)
                .throwableCallback(new ThrowableAdapter())
                .build());

        // Hook入口Application
        XposedUtil.findMethod("con.xxx.xxx.package", "method")
                .before(param -> handleLoadPackage(param, lpParam));
    }

    /**
     * 处理加载的包
     * @param param
     * @param lpParam
     * @throws Throwable
     */
    private void handleLoadPackage(
            XC_MethodHook.MethodHookParam param,
            XC_LoadPackage.LoadPackageParam lpParam) throws Throwable {

        Application application = (Application) param.thisObject;
        Context context = application.getApplicationContext();

        XCoreManager coreManager = new CoreManager.Build(context)
                .setPluginPackageName(BuildConfig.APPLICATION_ID)
                .setProcessName(lpParam.processName)
                .setClassLoader(lpParam.classLoader)
                .setComponentFactory(new ComponentFactory() {
                    @Override
                    protected List<Class<? extends XConfig>> getVersionData() {
                        return super.getVersionData();
                    }

                    @Override
                    protected List<Class<? extends XPlugin>> getPluginData() {
                        return super.getPluginData();
                    }
                })
                .setCoreListener(new CoreListenerAdapter() {

                    @Override
                    public void onInitComplete(XCoreManager coreManager) {
                        super.onInitComplete(coreManager);
                    }
                })
                .build();

        // 开始处理加载的包
        coreManager.loadPlugins();
    }
}
