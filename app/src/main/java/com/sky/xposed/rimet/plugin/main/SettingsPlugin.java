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

package com.sky.xposed.rimet.plugin.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.model.PluginInfo;
import com.sky.xposed.rimet.plugin.base.BasePlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;

/**
 * Created by sky on 2018/12/30.
 */
public class SettingsPlugin extends BasePlugin {

    public SettingsPlugin(XPluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Info getInfo() {
        return new PluginInfo(Constant.Plugin.MAIN_SETTINGS, "设置");
    }

    @Override
    public void onHandleLoadPackage() {

        findMethod(
                "com.alibaba.android.user.settings.activity.NewSettingActivity",
                "onCreate", Bundle.class)
                .after(param -> {

                    final Activity activity = (Activity) param.thisObject;

                    View view = activity.findViewById(ResourceUtil.getId(activity, "setting_msg_notice"));
                    ViewGroup viewGroup = (ViewGroup) view.getParent();

                    final int index = viewGroup.indexOfChild(view);

                    SimpleItemView viewDing = new SimpleItemView(activity);
                    viewDing.getNameView().setTextSize(17);
                    viewDing.setName(Constant.Name.TITLE);
                    viewDing.setOnClickListener(v -> {
                        // 打开设置
                        openSettings(activity);
                    });
                    viewGroup.addView(viewDing, index);
                });
    }

    @Override
    public void openSettings(Activity activity) {

        // 打开插件设置
        getPluginManager().getXPluginById(Constant.Plugin.DING_DING).openSettings(activity);

//        PluginSettingsDialog dialog = new PluginSettingsDialog();
//        dialog.show(activity.getFragmentManager(), "settings");
    }
}
