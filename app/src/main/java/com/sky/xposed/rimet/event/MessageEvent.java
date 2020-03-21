package com.sky.xposed.rimet.event;

import com.sky.xposed.core.interfaces.XEvent;

import java.util.Collection;

/**
 * Created by sky on 2020-03-21.
 */
public interface MessageEvent extends XEvent {

    /**
     * 处理消息
     * @param cid
     * @param messages
     */
    void onHandlerMessage(String cid, Collection messages);
}
