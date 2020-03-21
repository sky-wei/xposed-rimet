package com.sky.xposed.rimet.plugin.base;

import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.event.MessageEvent;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020-03-21.
 */
public abstract class MessagePlugin extends AbstractPlugin implements MessageEvent {

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
