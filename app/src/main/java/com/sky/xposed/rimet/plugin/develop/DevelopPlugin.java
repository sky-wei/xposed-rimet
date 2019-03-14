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

package com.sky.xposed.rimet.plugin.develop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.model.PluginInfo;
import com.sky.xposed.rimet.plugin.base.BasePlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;
import com.sky.xposed.rimet.ui.dialog.DevelopDialog;
import com.sky.xposed.rimet.util.ToStringUtil;

/**
 * Created by sky on 2018/12/19.
 */
public class DevelopPlugin extends BasePlugin {

    public DevelopPlugin(XPluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Info getInfo() {
        return new PluginInfo(Constant.Plugin.DEBUG, "微信调试");
    }

    @Override
    public void onHandleLoadPackage() {

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
    }

    @Override
    public void openSettings(Activity activity) {

        DevelopDialog developDialog = new DevelopDialog();
        developDialog.show(activity.getFragmentManager(), "develop");
    }
}
