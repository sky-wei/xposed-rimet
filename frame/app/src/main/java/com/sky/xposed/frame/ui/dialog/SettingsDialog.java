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

package com.sky.xposed.frame.ui.dialog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.util.ViewUtil;
import com.sky.xposed.ui.view.PluginFrameLayout;
import com.sky.xposed.ui.view.SimpleItemView;

/**
 * Created by sky on 2020-02-18.
 */
public class SettingsDialog extends BasePluginDialog {

    @Override
    public void createView(PluginFrameLayout frameView) {

        ViewUtil.newSwitchItemView(getContext(), "测试开关", "功能说明")
                .addToFrame(frameView);

        ViewUtil.newSwitchItemView(getContext(), "测试开关", "功能说明")
                .addToFrame(frameView);

        ViewUtil.newSwitchItemView(getContext(), "测试开关", "功能说明")
                .addToFrame(frameView);

        SimpleItemView itemView = new SimpleItemView(getContext());
        itemView.setName("其他");
        itemView.setOnClickListener(v -> {

        });
        itemView.addToFrame(frameView);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        showBack();
        showMoreMenu();
        setTitle("设置");
    }

    @Override
    public void onCreateMoreMenu(Menu menu) {
        super.onCreateMoreMenu(menu);

        menu.add(0, 0, 0, "关于");
    }

    @Override
    public boolean onMoreItemSelected(MenuItem item) {
        return super.onMoreItemSelected(item);
    }
}
