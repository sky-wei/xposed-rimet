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

import android.content.ContentValues;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.plugin.base.MessagePlugin;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020-03-01.
 */
@APlugin()
public class RecallMsgPlugin extends MessagePlugin {

    public RecallMsgPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        if (!isOpenHook()) return;

        findMethod(
                M.classz.class_defpackage_MessageDs,
                M.method.method_defpackage_MessageDs_recall,
                String.class, List.class, ContentValues.class)
                .before(param -> {
                    if (recallMessage((ContentValues) param.args[2])) {
                        // 直接返回0
                        param.setResult(0);
                    }
                });
    }

    @Override
    public void onHandlerMessage(String cid, Collection messages) {

        if (CollectionUtil.isEmpty(messages) || !isEnableRecall()) {
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

        if (126 == msgType) {
            // 处理撤回消息
            handlerRecallMessage(cid, message);
        }
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

            Method methodMessageUpdate =  XposedHelpers.findMethodExact(
                    findClass(M.classz.class_defpackage_MessageDs),
                    getXString(M.method.method_defpackage_MessageDs_update),
                    String.class, String.class, List.class);

            setMsgText(recallMessage, getMsgText(recallMessage) + " [已撤回]");

            // 更新消息信息
            methodMessageUpdate.invoke(null, dbName, cid, Collections.singletonList(recallMessage));
        } catch (Throwable tr) {
            Alog.e("异常了", tr);
        }
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
     * 判断是不是撤回消息
     * @param contentValues
     * @return
     */
    private boolean recallMessage(ContentValues contentValues) {

        if (!isEnableRecall() || contentValues == null) {
            // 不需要处理
            return false;
        }

        Integer integer = contentValues.getAsInteger("recall");

        return integer != null && integer == 1;
    }

    private boolean isEnableRecall() {
        return isEnable(XConstant.Key.ENABLE_RECALL);
    }
}
