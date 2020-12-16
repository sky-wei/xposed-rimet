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

import android.app.Activity;
import android.view.View;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.common.util.ConversionUtil;
import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.plugin.base.MessagePlugin;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020-03-01.
 */
@APlugin()
public class LuckyPlugin extends MessagePlugin {

    public LuckyPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        findMethod(
                M.classz.class_android_dingtalk_redpackets_activities_FestivalRedPacketsPickActivity,
                M.method.method_android_dingtalk_redpackets_activities_FestivalRedPacketsPickActivity_initView)
                .after(param -> {
                    // 处理快速打开红包
                    handlerFestivalRedPacketsPick((Activity) param.thisObject);
                });

        findMethod(
                M.classz.class_android_dingtalk_redpackets_activities_PickRedPacketsActivity,
                M.method.method_android_dingtalk_redpackets_activities_PickRedPacketsActivity_initView)
                .after(param -> {
                    // 处理快速打开红包
                    handlerPickRedPackets((Activity) param.thisObject);
                });
    }

    @Override
    public void onHandlerMessage(String cid, Collection messages) {

        if (CollectionUtil.isEmpty(messages) || !isEnableLucky()) {
            return;
        }

        for (Object message : messages) {
            // 处理消息
            handlerMessage(cid, message);
        }
    }

    /**
     * 处理消息
     * @param message
     */
    private void handlerMessage(String cid, Object message) {

        if (message == null) return;

        int msgType = getMsgType(message);

//        Alog.d(">>>>>>>>>>>>>>>>> MsgType " + msgType);

        if (133 == msgType || 135 == msgType || 160 == msgType || 164 == msgType) {
            // 处理红包消息
            handlerLuckyMessage(message);
        }
    }

    /**
     * 处理红包消息
     * @param message
     */
    private void handlerLuckyMessage(Object message) {

        // 直接根据消息类型来处理
        Object messageContent = XposedHelpers.callMethod(message,
                getXString(M.method.method_wukong_im_message_MessageImpl_messageContent));

        List messageContents = (List) XposedHelpers.callMethod(
                messageContent, getXString(M.method.method_wukong_im_message_MessageContentImpl_contents));

        if (CollectionUtil.isEmpty(messageContents)) return;

        Object customMessage = messageContents.get(0);
        Map extension = (Map) XposedHelpers.getObjectField(
                customMessage, getXString(M.field.field_wukong_im_message_MessageContentImpl_CustomMessageContentImpl_mExtension));

        // 获取红包信息
        String sid = (String) extension.get(getXString(M.key.key_sid));
        String clusterId = (String) extension.get(getXString(M.key.key_clusterid));

        // 获取休眠的时间
        long delayMillis = getPInt(XConstant.Key.LUCKY_DELAYED, 500);;

        // 获取红包
        postDelayed(() -> {
            try {
                pickRedEnvelop(ConversionUtil.parseLong(sid), clusterId);
            } catch (Throwable tr) {
                Alog.e("处理异常", tr);
            }
        }, delayMillis);
    }

    /**
     * 接收红包
     * @param sid
     * @param clusterId
     */
    public void pickRedEnvelop(long sid, String clusterId) throws Exception {

        Class classRedEnvelopPickIService = findClass(M.classz.class_android_dingtalk_redpackets_idl_service_RedEnvelopPickIService);
        Class classRedPacketsRpc = findClass(M.classz.class_defpackage_RedPacketsRpc);
        Class classSubRedPacketsRpc = findClass(M.classz.class_defpackage_RedPacketsRpc_9);

        Object redPacketsRpc = XposedHelpers.callStaticMethod(
                classRedPacketsRpc, getXString(M.method.method_defpackage_RedPacketsRpc_newInstance));
        Object handler = XposedHelpers.newInstance(classSubRedPacketsRpc, redPacketsRpc, null);

        Method methodGetService = XposedHelpers.findMethodExact(
                findClass(M.classz.class_defpackage_ServiceFactory),
                getXString(M.method.method_defpackage_ServiceFactory_getService),
                Class.class);

        // 获取红包服务
        Object redEnvelopPickIService = methodGetService.invoke(null, classRedEnvelopPickIService);

        // 自动接收红包
        XposedHelpers.callMethod(redEnvelopPickIService,
                getXString(M.method.method_android_dingtalk_redpackets_idl_service_RedEnvelopPickIService_pickRedEnvelopCluster),
                sid, clusterId, handler);
    }

    /**
     * 处理拆包
     * @param activity
     */
    private void handlerFestivalRedPacketsPick(Activity activity) {

        if (!isEnable(XConstant.Key.ENABLE_FAST_LUCKY)) return;

        View view = activity.findViewById(ResourceUtil.getId(activity, getXString(M.res.res_iv_pick)));
        if (view != null && view.isClickable()) view.performClick();
    }

    /**
     * 处理拆包
     * @param activity
     */
    private void handlerPickRedPackets(Activity activity) {

        if (!isEnable(XConstant.Key.ENABLE_FAST_LUCKY)) return;

        View view = activity.findViewById(ResourceUtil.getId(activity, getXString(M.res.res_btn_pick)));
        if (view != null && view.isClickable()) view.performClick();
    }

    private boolean isEnableLucky() {
        return isEnable(XConstant.Key.ENABLE_LUCKY);
    }
}
