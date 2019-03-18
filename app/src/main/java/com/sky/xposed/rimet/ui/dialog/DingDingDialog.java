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

package com.sky.xposed.rimet.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.EditTextItemView;
import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.ui.view.SwitchItemView;
import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.plugin.interfaces.XPlugin;
import com.sky.xposed.rimet.ui.util.DialogUtil;

/**
 * Created by sky on 2019/3/13.
 */
public class DingDingDialog extends CommonDialog {

    private SwitchItemView sivLuckyEnable;
    private EditTextItemView sivLuckyDelayed;
    private SwitchItemView sivFastLuckyEnable;
    private SwitchItemView sivRecallEnable;
    private SimpleItemView sivDonate;
    private SimpleItemView sivAbout;

    @Override
    public void createView(CommonFrameLayout frameView) {

        sivLuckyEnable = ViewUtil.newSwitchItemView(getContext(), "自动接收红包");
        sivLuckyEnable.setDesc("开启时自动接收红包");

        sivLuckyDelayed = new EditTextItemView(getContext());
        sivLuckyDelayed.setInputType(com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED);
        sivLuckyDelayed.setMaxLength(2);
        sivLuckyDelayed.setUnit("秒");
        sivLuckyDelayed.setName("红包延迟时间");
        sivLuckyDelayed.setExtendHint("单位(秒)");

        sivFastLuckyEnable = ViewUtil.newSwitchItemView(getContext(), "快速打开红包");
        sivFastLuckyEnable.setDesc("开启时快速打开红包");

        sivRecallEnable = ViewUtil.newSwitchItemView(getContext(), "消息防撤回");
        sivRecallEnable.setDesc("开启时消息不会被撤回(暂不可使用)");

        sivDonate = ViewUtil.newSimpleItemView(getContext(), "支持我们");
        sivAbout = ViewUtil.newSimpleItemView(getContext(), "关于");

        frameView.addContent(sivLuckyEnable);
        frameView.addContent(sivLuckyDelayed);
        frameView.addContent(sivFastLuckyEnable);
        frameView.addContent(sivRecallEnable);

        frameView.addContent(sivDonate);
        frameView.addContent(sivAbout);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        setTitle(Constant.Name.TITLE);

        XPlugin xPlugin = getPluginManager().getXPluginById(Constant.Plugin.DING_DING);

        sivLuckyEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_LUCKY), true,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_LUCKY, value);
                    return true;
                });

        sivLuckyDelayed.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.LUCKY_DELAYED), "",
                (view12, key, value) -> true);

        sivFastLuckyEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_FAST_LUCKY), true,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_FAST_LUCKY, value);
                    return true;
                });

        sivRecallEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_RECALL), true,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_RECALL, value);
                    return true;
                });

        sivDonate.setOnClickListener(v -> {
            // 打开捐赠界面
            DonateDialog donateDialog = new DonateDialog();
            donateDialog.show(getFragmentManager(), "donate");
        });

        sivAbout.setOnClickListener(v -> {
            // 打开关于界面
            DialogUtil.showAboutDialog(getContext());
        });
    }
}
