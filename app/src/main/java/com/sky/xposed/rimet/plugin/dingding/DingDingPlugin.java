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

package com.sky.xposed.rimet.plugin.dingding;

import android.app.Activity;
import android.content.Context;

import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.data.model.PluginInfo;
import com.sky.xposed.rimet.plugin.base.BasePlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;
import com.sky.xposed.rimet.ui.dialog.DingDingDialog;

import java.util.List;

/**
 * Created by sky on 2019/3/14.
 */
public class DingDingPlugin extends BasePlugin {

    private Handler mHandler;

    public DingDingPlugin(Build build) {
        super(build.mPluginManager);
        mHandler = build.mHandler;
    }

    @Override
    public void setEnable(int flag, boolean enable) {
//        super.setEnable(flag, enable);    // 不需要处理
        mHandler.setEnable(flag, enable);
    }

    @Override
    public Info getInfo() {
        return new PluginInfo(Constant.Plugin.DING_DING, Constant.Name.TITLE);
    }

    @Override
    public void onHandleLoadPackage() {

        findMethod(
                M.classz.class_lightapp_runtime_LightAppRuntimeReverseInterfaceImpl,
                M.method.method_lightapp_runtime_LightAppRuntimeReverseInterfaceImpl_initSecurityGuard,
                Context.class)
                .before(param -> param.setResult(null));

        findMethod(
                M.classz.class_defpackage_ConversationChangeMaid,
                M.method.method_defpackage_ConversationChangeMaid_onLatestMessageChanged,
                List.class)
                .after(param -> {
                    // 处理消息
                    mHandler.onHandlerMessage((List) param.args[0]);
                });

        findMethod(
                M.classz.class_android_dingtalk_redpackets_activities_FestivalRedPacketsPickActivity,
                M.method.method_android_dingtalk_redpackets_activities_FestivalRedPacketsPickActivity_initView)
                .after(param -> {
                    // 处理快速打开红包
                    mHandler.onHandlerFestivalRedPacketsPick((Activity) param.thisObject);
                });

        findMethod(
                M.classz.class_android_dingtalk_redpackets_activities_PickRedPacketsActivity,
                M.method.method_android_dingtalk_redpackets_activities_PickRedPacketsActivity_initView)
                .after(param -> {
                    // 处理快速打开红包
                    mHandler.onHandlerPickRedPackets((Activity) param.thisObject);
                });
    }



    @Override
    public void openSettings(Activity activity) {

        DingDingDialog dialog = new DingDingDialog();
        dialog.show(activity.getFragmentManager(), "dingDing");
    }

    public interface Handler {

        void setEnable(int flag, boolean enable);

        void onHandlerMessage(List conversations);

        void onHandlerFestivalRedPacketsPick(Activity activity);

        void onHandlerPickRedPackets(Activity activity);
    }

    public static class Build {

        private XPluginManager mPluginManager;
        private Handler mHandler;

        public Build(XPluginManager pluginManager) {
            mPluginManager = pluginManager;
        }

        public Build setHandler(Handler handler) {
            mHandler = handler;
            return this;
        }

        public XPlugin build() {
            return new DingDingPlugin(this);
        }
    }
}
