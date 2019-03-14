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

package com.sky.xposed.rimet.plugin.redpacket;

import android.app.Activity;

import com.sky.xposed.common.util.ConversionUtil;
import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.data.model.PluginInfo;
import com.sky.xposed.rimet.plugin.base.BasePlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;
import com.sky.xposed.rimet.util.CollectionUtil;
import com.sky.xposed.rimet.util.ToStringUtil;

import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2019/3/14.
 */
public class RedPacketPlugin extends BasePlugin {

    public RedPacketPlugin(XPluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Info getInfo() {
        return new PluginInfo(Constant.Plugin.RED_PACKET, "红包功能");
    }

    @Override
    public void onHandleLoadPackage() {

        findMethod(
                M.classz.class_defpackage_ConversationChangeMaid,
                M.method.method_defpackage_ConversationChangeMaid_onLatestMessageChanged,
                List.class)
                .after(param -> {

                    List conversations = (List) param.args[0];

                    for (Object conversation : conversations) {
                        // 处理消息
                        onHandlerMessage(conversation);
                    }
                });
    }

    private void onHandlerMessage(Object conversation) {

        Object message = XposedHelpers.callMethod(conversation,
                getXString(M.method.method_wukong_im_conversation_ConversationImpl_latestMessage));
        Object messageContent = XposedHelpers.callMethod(message,
                getXString(M.method.method_wukong_im_message_MessageImpl_messageContent));

        if (messageContent == null) return;

//        ToStringUtil.toString(messageContent);

        int type = XposedHelpers.getIntField(
                messageContent, getXString(M.field.field_wukong_im_message_MessageContentImpl_mType));

        if (902 != type) return;

        List messageContents = (List) XposedHelpers.callMethod(
                messageContent, getXString(M.method.method_wukong_im_message_MessageContentImpl_contents));

        if (CollectionUtil.isEmpty(messageContents)) return;

        Object customMessage = messageContents.get(0);
        int customType = XposedHelpers.getIntField(
                customMessage, getXString(M.field.field_wukong_im_message_MessageContentImpl_CustomMessageContentImpl_mCustomType));

        if (902 != customType) return;

        ToStringUtil.toString(customMessage);

        Map<String, String> extension = (Map<String, String>) XposedHelpers.getObjectField(
                customMessage, getXString(M.field.field_wukong_im_message_MessageContentImpl_CustomMessageContentImpl_mExtension));

        // 获取红包信息
        String sid = extension.get(getXString(M.key.key_sid));
        String clusterId = extension.get(getXString(M.key.key_clusterid));

        // 获取红包
        pickRedEnvelop(ConversionUtil.parseLong(sid), clusterId);
    }

    private void pickRedEnvelop(long sid, String clusterId) {

        Class classServiceFactory = findClass(M.classz.class_defpackage_ServiceFactory);
        Class classRedEnvelopPickIService = findClass(M.classz.class_android_dingtalk_redpackets_idl_service_RedEnvelopPickIService);
        Class classRedPacketsRpc = findClass(M.classz.class_defpackage_RedPacketsRpc);
        Class classSubRedPacketsRpc = findClass(M.classz.class_defpackage_RedPacketsRpc_9);

        Object redPacketsRpc = XposedHelpers.callStaticMethod(
                classRedPacketsRpc, getXString(M.method.method_defpackage_RedPacketsRpc_newInstance));
        Object handler = XposedHelpers.newInstance(classSubRedPacketsRpc, redPacketsRpc, null);

        // 获取红包服务
        Object redEnvelopPickIService = XposedHelpers.callStaticMethod(
                classServiceFactory, getXString(M.method.method_defpackage_ServiceFactory_getService), classRedEnvelopPickIService);

        // 自动接收红包
        XposedHelpers.callMethod(redEnvelopPickIService,
                getXString(M.method.method_android_dingtalk_redpackets_idl_service_RedEnvelopPickIService_pickRedEnvelopCluster),
                sid, clusterId, handler);
    }

    @Override
    public void openSettings(Activity activity) {

    }
}
