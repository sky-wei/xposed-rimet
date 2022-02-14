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

package com.sky.xposed.rimet.plugin.debug;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ToStringUtil;
import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;

import java.util.Arrays;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2018/12/19.
 */
@APlugin(filter = { APlugin.Process.ALL })
public class DebugPlugin extends AbstractPlugin {

    public DebugPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        findMethod("android.app.Activity", "setResult",
                int.class, Intent.class)
                .before(param -> {

                    Intent intent = (Intent) param.args[1];
                    ToStringUtil.toString("Activity#setResult: " + intent.getComponent(), intent);
                });

        findMethod("android.app.Instrumentation", "execStartActivity",
                Context.class, IBinder.class, IBinder.class,
                Activity.class, Intent.class, int.class, Bundle.class)
                .before(param -> {

                    Intent intent = (Intent) param.args[4];
                    ToStringUtil.toString("Instrumentation#execStartActivity: " + intent.getComponent(), intent);
                });

//        findMethod("android.app.Instrumentation", "execStartActivity",
//                Context.class, IBinder.class, IBinder.class,
//                Activity.class, Intent.class, int.class,
//                Bundle.class, UserHandle.class)
//                .before(param -> {
//
//                    Intent intent = (Intent) param.args[4];
//                    ToStringUtil.toString("Instrumentation#execStartActivity: " + intent.getComponent(), intent);
//                });
//
//        findMethod(
//                "android.app.Dialog", "show")
//                .before(param -> Alog.d(">>>>>>>>>>>>>> dialog " + param.thisObject));
//
//        findMethod(
//                "android.support.v4.app.Fragment",
//                "onCreate", Bundle.class)
//                .before(param -> Alog.d(">>>>>>>>>>>>>> Fragment " + param.thisObject.getClass()));



//        findMethod("cxw", "getView", int.class, View.class, ViewGroup.class)
//                .before(param -> {
//
//                    int position = (int) param.args[0];
//                    Adapter adapter = (Adapter) param.thisObject;
//
//                    Alog.d(">>>>>>>>>>>>>>>>>> " + adapter.getCount());
//
//
//                    Object message = XposedHelpers.callMethod(param.thisObject, "getItem", position);
//                    ToStringUtil.toString(message);
//
//                });
        findMethod("com.alibaba.bee.DBManager", "insert", String.class, Class.class, String.class, ContentValues.class)
                .before(param -> {

                    Alog.d(">>>>>>>>>> insert " + Arrays.toString(param.args));
                });


        findMethod("com.alibaba.bee.DBManager", "insertWithOnConflict", String.class, Class.class, String.class, ContentValues.class, int.class)
                .before(param -> {

                    Alog.d(">>>>>>>>>> insertWithOnConflict " + Arrays.toString(param.args));
                });

        findMethod("com.alibaba.bee.DBManager", "update", String.class, Class.class, String.class, ContentValues.class, String.class, String[].class)
                .before(param -> {

                    Alog.d(">>>>>>>>>> update " + Arrays.toString(param.args));

//                    ContentValues contentValues = (ContentValues) param.args[3];
//                    Class tClass = (Class) param.args[1];
//                    String[] strings = (String[]) param.args[5];
//
////                    Alog.d(">>>>>>>>>>>>>>>>>> tClass " + tClass);
//
//                    if ("com.alibaba.wukong.im.message.MessageEntry".equals(tClass.getName())) {
//
//                        Integer integer = contentValues.getAsInteger("recall");
//
//                        Alog.d(">>>>>>>>>>>>>> " + Arrays.toString(strings));
//
//                        if (integer != null && integer.intValue() == 1) {
//                            DebugUtil.printStackTrace();
//                            param.setResult(null);
//                            return;
//                        }
//                    }
                });



//        findMethod("com.alibaba.bee.DBManager", "updateWithOnConflict", String.class, Class.class, String.class, ContentValues.class, String.class, String[].class, int.class)
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> updateWithOnConflict " + Arrays.toString(param.args));
//                });
//
//        findMethod("com.alibaba.bee.DBManager", "delete", String.class, Class.class, String.class, String.class, String[].class)
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> delete " + Arrays.toString(param.args));
//                });
//
//        findMethod("com.alibaba.bee.DBManager", "execRaw", String.class, String.class)
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> execRaw " + Arrays.toString(param.args));
//                });
//
//        findMethod("com.alibaba.bee.DBManager", "execRaw", String.class, String.class, Object[].class)
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> execRaw " + Arrays.toString(param.args));
//                });
//
//        findMethod("com.alibaba.bee.DBManager", "executeWriteTask", String.class, "com.alibaba.sqlcrypto.sqlite.DatabaseTask")
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> executeWriteTask " + Arrays.toString(param.args));
//                });
//
//        findMethod("com.alibaba.bee.DBManager", "execInTransaction", String.class, Runnable.class, "com.alibaba.bee.SQLiteTransactionListener")
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> execInTransaction " + Arrays.toString(param.args));
//                });
//
//        findMethod("com.alibaba.bee.DBManager", "compileStatement", String.class, Class.class, String.class)
//                .before(param -> {
//
//                    Alog.d(">>>>>>>>>> compileStatement " + Arrays.toString(param.args));
//
//                    Class tClass = (Class) param.args[1];
//
//                    if ("com.alibaba.wukong.im.message.MessageEntry".equals(tClass.getName())) {
//
//                        DebugUtil.printStackTrace();
//                    }
//                });

        findMethod(
                "com.alibaba.android.user.contact.organization.friendcontact.FriendFragment",
                "onActivityCreated",
                Bundle.class
        ).after(param -> {

            BaseAdapter adapter = (BaseAdapter) XposedHelpers.getObjectField(param.thisObject, "l");

            getLoadPackage().getHandler().postDelayed(() -> {

                ToStringUtil.toString(adapter);
                Alog.d(">>>>>>>>>>>>>>>>>>>> " + adapter.getCount());
                ToStringUtil.toString(adapter.getItem(0));

            }, 3000);
        });

        findMethod(
                "com.alibaba.android.dingtalkim.activities.MsgForwardActivity",
                "onClick",
                View.class
        ).before(param -> {

            Alog.d(">>>>>>>>>>>>>>>>>>> onClick " + param.args[0]);
        });

        findMethod(
                "com.alibaba.android.dingtalkim.activities.MsgForwardActivity",
                "onItemClick",
                AdapterView.class, View.class, int.class, long.class
        ).before(param -> {

            Alog.d(">>>>>>>>>>>>>>>>>>> onItemClick " + Arrays.toString(param.args));
        });

        findMethod(
                "com.alibaba.android.dingtalkim.activities.MsgForwardActivity",
                "doEntranceItemClick",
                "com.alibaba.wukong.im.Conversation"
        ).before(param -> {

            Alog.d(">>>>>>>>>>>>>>>>>>> doEntranceItemClick " + Arrays.toString(param.args));
        });


        findMethod(
                "com.alibaba.android.dingtalkim.activities.MsgForwardActivity",
                "doItemClick",
                String.class, "com.alibaba.wukong.im.Conversation", boolean.class
        ).before(param -> {

            Alog.d(">>>>>>>>>>>>>>>>>>> doItemClick " + Arrays.toString(param.args));
//            param.setResult(null);
        });

        findMethod(
                "com.alibaba.android.dingtalkim.activities.MsgForwardActivity",
                "share2Conversation",
                String.class, "com.alibaba.android.dingtalkim.base.model.DingtalkConversation", boolean.class
        ).before(param -> {

            Alog.d(">>>>>>>>>>>>>>>>>>> share2Conversation " + Arrays.toString(param.args));
            Alog.d(">>>>>>>>>>>>>>>>>>> share2Conversation " + XposedHelpers.getObjectField(param.thisObject, "mForwardHandler"));
        });

        findMethod(
                "com.alibaba.android.dingtalkim.imtools.ChatMessageSender",
                "sendMessage",
                "com.alibaba.wukong.im.Message",
                boolean.class, boolean.class, String.class, String.class, boolean.class, boolean.class
        ).before(param -> {

            ToStringUtil.toString(param.thisObject);
            ToStringUtil.toString(param.args[0]);
            Alog.d(">>>>>>>>>>>>>>>>>>> sendMessage " + Arrays.toString(param.args));
            param.setResult(null);
        });
    }
}
