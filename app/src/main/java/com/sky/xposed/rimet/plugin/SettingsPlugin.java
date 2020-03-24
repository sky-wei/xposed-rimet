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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.BuildConfig;
import com.sky.xposed.rimet.plugin.base.BaseDingPlugin;
import com.sky.xposed.rimet.ui.dialog.SettingsDialog;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.view.SimpleItemView;

/**
 * Created by sky on 2018/12/30.
 */
@APlugin()
public class SettingsPlugin extends BaseDingPlugin {

    public SettingsPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        findMethod(
                "com.alibaba.android.user.settings.activity.NewSettingActivity",
                "onCreate",
                Bundle.class)
                .after(param -> onHandlerSettings((Activity) param.thisObject));

        findMethod(
                "com.alibaba.android.user.settings.activity.UserSettingsActivity",
                "onCreate",
                Bundle.class)
                .after(param -> onHandlerSettings((Activity) param.thisObject));
    }

    private void onHandlerSettings(Activity activity) {

        View view = activity.findViewById(ResourceUtil.getId(activity, "setting_msg_notice"));
        ViewGroup viewGroup = (ViewGroup) view.getParent();

        final int index = viewGroup.indexOfChild(view);

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        SimpleItemView itemView = new SimpleItemView(activity);
        itemView.setMinimumHeight(DisplayUtil.DIP_55);
        itemView.getNameView().setTextSize(16);
        itemView.setName("钉钉助手");
        itemView.setExtend("v" + BuildConfig.VERSION_NAME);
        itemView.setOnClickListener(v -> {
            // 打开设置
            openSettings(activity);
        });
        linearLayout.addView(itemView);

        View lineView = new View(activity);
        lineView.setBackgroundColor(0xFFEFEFEF);
        linearLayout.addView(lineView, new LayoutUtil.Build()
                .setHeight(2)
                .linearParams());

        viewGroup.addView(linearLayout, index);
    }

    private void openSettings(Activity activity) {

        SettingsDialog dialog = new SettingsDialog();
        dialog.show(activity);
    }
}
