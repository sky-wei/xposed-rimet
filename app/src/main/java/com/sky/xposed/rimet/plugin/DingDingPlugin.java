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

package com.sky.xposed.rimet.plugin;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;

/**
 * Created by sky on 2019/3/14.
 */
@APlugin
public class DingDingPlugin extends AbstractPlugin {

    public DingDingPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

//        findMethod(
//                M.classz.class_lightapp_runtime_LightAppRuntimeReverseInterfaceImpl,
//                M.method.method_lightapp_runtime_LightAppRuntimeReverseInterfaceImpl_initSecurityGuard,
//                Context.class)
//                .before(param -> param.setResult(null));
//
//        Method methodMessage = findMatcherMethod(
//                M.classz.class_defpackage_MessageDs,
//                M.method.method_defpackage_MessageDs_handler,
//                String.class, Collection.class, boolean.class);
//
//        XposedBridge.hookMethod(methodMessage, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                // 处理消息
//                mHandler.onHandlerMessage((String) param.args[0], (Collection) param.args[1]);
//            }
//        });
//
//        findMethod(
//                M.classz.class_android_dingtalk_redpackets_activities_FestivalRedPacketsPickActivity,
//                M.method.method_android_dingtalk_redpackets_activities_FestivalRedPacketsPickActivity_initView)
//                .after(param -> {
//                    // 处理快速打开红包
//                    mHandler.onHandlerFestivalRedPacketsPick((Activity) param.thisObject);
//                });
//
//        findMethod(
//                M.classz.class_android_dingtalk_redpackets_activities_PickRedPacketsActivity,
//                M.method.method_android_dingtalk_redpackets_activities_PickRedPacketsActivity_initView)
//                .after(param -> {
//                    // 处理快速打开红包
//                    mHandler.onHandlerPickRedPackets((Activity) param.thisObject);
//                });
//
//        Method methodRecall = findMatcherMethod(
//                M.classz.class_defpackage_MessageDs,
//                M.method.method_defpackage_MessageDs_recall,
//                String.class, List.class, ContentValues.class);
//
//        XposedBridge.hookMethod(methodRecall, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//                // 处理撤回消息
//                if (mHandler.onRecallMessage((ContentValues) param.args[2])) {
//                    // 直接返回0
//                    param.setResult(0);
//                }
//            }
//        });
//
//
//        /****************  位置信息处理 ******************/
//
//        findMethod(
//                "com.amap.api.location.AMapLocationClient",
//                "getLastKnownLocation")
//                .after(param -> param.setResult(mHandler.getLastKnownLocation(param.getResult())));
//
//        findMethod(
//                "com.amap.api.location.AMapLocationClient",
//                "setLocationListener",
//                "com.amap.api.location.AMapLocationListener")
//                .before(param -> param.args[0] = mHandler.onHandlerLocationListener(param.args[0]));
    }
}
