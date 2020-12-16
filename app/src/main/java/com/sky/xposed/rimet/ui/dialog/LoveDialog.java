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

package com.sky.xposed.rimet.ui.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.common.util.ToastUtil;
import com.sky.xposed.rimet.ui.util.ActivityUtil;
import com.sky.xposed.rimet.ui.util.XViewUtil;
import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.view.PluginFrameLayout;
import com.sky.xposed.ui.view.SimpleItemView;

/**
 * Created by sky on 2019/3/13.
 */
public class LoveDialog extends BasePluginDialog {

    private SimpleItemView sivAliPayLove;
    private SimpleItemView sivWeChatLove;

    @Override
    public void createView(PluginFrameLayout frameView) {

        LinearLayout.LayoutParams params = LayoutUtil.newWrapLinearLayoutParams();
        params.leftMargin = DisplayUtil.dip2px(getContext(), 15);
        params.topMargin = DisplayUtil.dip2px(getContext(), 10);
        params.bottomMargin = DisplayUtil.dip2px(getContext(), 5);

        TextView tvTip = new TextView(getContext());
        tvTip.setLayoutParams(params);
        tvTip.setText("这个世界有些人更需要你们的帮助！");
        tvTip.setTextSize(12);

        sivWeChatLove = XViewUtil.newSimpleItemView(getContext(), "腾讯公益");
        sivAliPayLove = XViewUtil.newSimpleItemView(getContext(), "支付宝公益");

        frameView.addSubView(tvTip);
        frameView.addSubView(sivWeChatLove);
        frameView.addSubView(sivAliPayLove);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        getTitleView().setElevation(DisplayUtil.DIP_4);

        showBack();
        setTitle("公益");

        sivAliPayLove.setOnClickListener(v -> {
            // 启动支付宝
            startAliPayLove();
        });

        sivWeChatLove.setOnClickListener(v -> {
            // 启动微信
            startWeChatLove();
        });
    }

    /**
     * 启动微信公益
     */
    private void startWeChatLove() {
        ActivityUtil.openUrl(getContext(), "https://gongyi.qq.com/");
    }

    /**
     * 启动支付宝公益
     */
    private void startAliPayLove() {

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromDesktop", true);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.eg.android.AlipayGphone", "com.alipay.mobile.framework.service.common.SchemeStartActivity");
        intent.setData(Uri.parse("alipaylite://platformapi/startapp?appId=2018080260797946&ap_framework_sceneId=1023&customParams=chInfo%3Dapp_desktop&chInfo=ch_desktop"));
        intent.putExtras(bundle);

        if (!ActivityUtil.startActivity(getContext(), intent)) {
            // 跳转失败
            ToastUtil.show("跳转到支付宝公益失败!");
        }
    }
}
