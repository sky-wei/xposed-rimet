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
import android.content.ContentValues;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ConversionUtil;
import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.plugin.base.BaseHandler;
import com.sky.xposed.rimet.plugin.interfaces.XConfigManager;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;
import com.sky.xposed.rimet.util.CollectionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2019/3/15.
 */
public class DingDingHandler extends BaseHandler implements DingDingPlugin.Handler {

    private XConfigManager mXConfigManager;

    private boolean mEnableLucky;
    private boolean mEnableFastLucky;
    private boolean mEnableRecall;
    private boolean mEnableLocation;

    public DingDingHandler(XPluginManager pluginManager) {
        super(pluginManager);
        mXConfigManager = getPluginManager().getConfigManager();

        mEnableLucky = mXConfigManager.getBoolean(Constant.XFlag.ENABLE_LUCKY, true);
        mEnableFastLucky = mXConfigManager.getBoolean(Constant.XFlag.ENABLE_FAST_LUCKY, true);
        mEnableRecall = mXConfigManager.getBoolean(Constant.XFlag.ENABLE_RECALL, true);
        mEnableLocation = mXConfigManager.getBoolean(Constant.XFlag.ENABLE_LOCATION, false);
    }

    @Override
    public void onHandlerMessage(String cid, Collection messages) {

        // 未开启不需要处理
        if (!mEnableLucky || CollectionUtil.isEmpty(messages)) return;

        for (Object message : messages) {
            // 处理消息
            handlerMessage(cid, message);
        }
    }

    @Override
    public void onHandlerFestivalRedPacketsPick(Activity activity) {

        if (!mEnableFastLucky) return;

        View view = activity.findViewById(ResourceUtil.getId(activity, getXString(M.res.res_iv_pick)));
        if (view != null && view.isClickable()) view.performClick();
    }

    @Override
    public void onHandlerPickRedPackets(Activity activity) {

        if (!mEnableFastLucky) return;

        View view = activity.findViewById(ResourceUtil.getId(activity, getXString(M.res.res_btn_pick)));
        if (view != null && view.isClickable()) view.performClick();
    }

    @Override
    public boolean onRecallMessage(ContentValues contentValues) {

        if (!mEnableRecall || contentValues == null) return false;

        Integer integer = contentValues.getAsInteger("recall");

        return integer != null && integer == 1;
    }

    @Override
    public Object getLastKnownLocation(Object location) {
        // 生效时不返回位置信息
        return mEnableLocation ? null : location;
    }

    @Override
    public Object onHandlerLocationListener(Object listener) {

        if (!Proxy.isProxyClass(listener.getClass())) {
            // 创建代理类
            return Proxy.newProxyInstance(
                    listener.getClass().getClassLoader(),
                    listener.getClass().getInterfaces(),
                    new AMapLocationListenerProxy(listener));
        }
        return listener;
    }

    @Override
    public void setEnable(int flag, boolean enable) {

        switch (flag) {
            case Constant.XFlag.ENABLE_LUCKY:
                mEnableLucky = enable;
                break;
            case Constant.XFlag.ENABLE_FAST_LUCKY:
                mEnableFastLucky = enable;
                break;
            case Constant.XFlag.ENABLE_RECALL:
                mEnableRecall = enable;
                break;
            case Constant.XFlag.ENABLE_LOCATION:
                mEnableLocation = enable;
                break;
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

        if (mEnableRecall && 126 == msgType) {
            // 处理撤回消息
            handlerRecallMessage(cid, message);
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
        long delayMillis = 1000L * ConversionUtil.parseInt(
                mXConfigManager.getString(Constant.XFlag.LUCKY_DELAYED, ""));

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
     * 处理撤回的消息
     * @param message
     */
    private void handlerRecallMessage(String cid, Object message) {

        Object conversation = getObjectField(message,
                M.field.field_android_dingtalkim_base_model_DingtalkMessage_mConversation);

        if (conversation == null) return;

        // 获取撤回的消息
        Object recallMessage = XposedHelpers.callMethod(conversation,
                getXString(M.method.method_wukong_im_conversation_ConversationImpl_latestMessage));

        if (recallMessage == null) return;

        // 获取消息类型
        int msgType = getMsgType(recallMessage);

        if (10 != msgType) return;  // 只处理文本消息

        try {
            Class classIMDatabase = findClass(M.classz.class_wukong_im_base_IMDatabase);
            String dbName = (String) XposedHelpers.callStaticMethod(classIMDatabase,
                    getXString(M.method.method_wukong_im_base_IMDatabase_getWritableDatabase));

            Method methodMessageUpdate =  findMatcherMethod(
                    M.classz.class_defpackage_MessageDs,
                    M.method.method_defpackage_MessageDs_update,
                    String.class, String.class, List.class);

            setMsgText(recallMessage, getMsgText(recallMessage) + " [已撤回]");

            // 更新消息信息
            methodMessageUpdate.invoke(null, dbName, cid, Collections.singletonList(recallMessage));
        } catch (Throwable tr) {
            Alog.e("异常了", tr);
        }
    }

    /**
     * 获取消息类型
     * @param message
     * @return
     */
    private int getMsgType(Object message) {

        Object msgDisplayType = getObjectField(message,
                M.field.field_android_dingtalkim_base_model_DingtalkMessage_msgDisplayType);

        return (int) XposedHelpers.callMethod(msgDisplayType,
                getXString(M.method.method_android_dingtalkim_base_model_typeValue));
    }

    /**
     * 获取消息文本信息
     * @param message
     * @return
     */
    private String getMsgText(Object message) {

        Object messageContent = XposedHelpers.callMethod(message,
                getXString(M.method.method_wukong_im_message_MessageImpl_messageContent));

        if (messageContent != null) {
            return (String) XposedHelpers.callMethod(messageContent,
                    getXString(M.method.method_wukong_im_message_MessageContentImpl_TextContentImpl_text));
        }
        return "";
    }

    /**
     * 设置消息文本信息
     * @param message
     * @param text
     */
    private void setMsgText(Object message, String text) {

        Object messageContent = XposedHelpers.callMethod(message,
                getXString(M.method.method_wukong_im_message_MessageImpl_messageContent));

        if (messageContent != null) {
            // 重新设置字符串
            XposedHelpers.callMethod(messageContent,
                    getXString(M.method.method_wukong_im_message_MessageContentImpl_TextContentImpl_setText), text);
        }
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

        Method methodGetService = findMatcherMethod(
                M.classz.class_defpackage_ServiceFactory,
                M.method.method_defpackage_ServiceFactory_getService,
                Class.class);

        // 获取红包服务
        Object redEnvelopPickIService = methodGetService.invoke(null, classRedEnvelopPickIService);

        // 自动接收红包
        XposedHelpers.callMethod(redEnvelopPickIService,
                getXString(M.method.method_android_dingtalk_redpackets_idl_service_RedEnvelopPickIService_pickRedEnvelopCluster),
                sid, clusterId, handler);
    }

    private final class AMapLocationListenerProxy implements InvocationHandler {

        private Object mListener;

        public AMapLocationListenerProxy(Object listener) {
            mListener = listener;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

            if (mEnableLocation
                    && "onLocationChanged".equals(method.getName())) {
                // 开始处理
                handlerLocationChanged(objects);
            }
            return method.invoke(mListener, objects);
        }

        private void handlerLocationChanged(Object[] objects) {

            if (objects == null || objects.length != 1) return;

            Location location = (Location) objects[0];

            String latitude = mXConfigManager
                    .getString(Constant.XFlag.LATITUDE, "");
            String longitude = mXConfigManager
                    .getString(Constant.XFlag.LONGITUDE, "");

            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                // 重新修改值
                location.setLongitude(Double.parseDouble(longitude));
                location.setLatitude(Double.parseDouble(latitude));
            }
        }
    }
}
