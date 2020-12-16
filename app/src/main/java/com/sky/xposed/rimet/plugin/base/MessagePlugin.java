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

package com.sky.xposed.rimet.plugin.base;

import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.event.MessageEvent;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020-03-21.
 */
public abstract class MessagePlugin extends BaseDingPlugin implements MessageEvent {

    public MessagePlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void initialize() {
        super.initialize();

        register(MessageEvent.class, this);
    }

    /**
     * 获取消息类型
     * @param message
     * @return
     */
    protected int getMsgType(Object message) {

        Object msgDisplayType = getObjectField(message,
                M.field.field_android_dingtalkim_base_model_DingtalkMessage_msgDisplayType);

        return (int) XposedHelpers.callMethod(msgDisplayType,
                getXString(M.method.method_android_dingtalkim_base_model_typeValue));
    }

    protected void postDelayed(Runnable runnable, long delayMillis) {
        getLoadPackage().getHandler().postDelayed(runnable, delayMillis);
    }
}
