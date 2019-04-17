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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.common.ui.util.LayoutUtil;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.util.DisplayUtil;
import com.sky.xposed.common.util.ToastUtil;
import com.sky.xposed.rimet.ui.util.ActivityUtil;

/**
 * Created by sky on 2019/3/13.
 */
public class LoveDialog extends CommonDialog {

    private SimpleItemView sivAliPayLove;
    private SimpleItemView sivWeChatLove;

    @Override
    public void createView(CommonFrameLayout frameView) {

        LinearLayout.LayoutParams params = LayoutUtil.newWrapLinearLayoutParams();
        params.leftMargin = DisplayUtil.dip2px(getContext(), 15);
        params.topMargin = DisplayUtil.dip2px(getContext(), 5);
        params.bottomMargin = DisplayUtil.dip2px(getContext(), 5);

        TextView tvTip = new TextView(getContext());
        tvTip.setLayoutParams(params);
        tvTip.setText("这个世界有些人更需要你们的帮助！");
        tvTip.setTextSize(12);

        sivWeChatLove = ViewUtil.newSimpleItemView(getContext(), "微信公益");
        sivAliPayLove = ViewUtil.newSimpleItemView(getContext(), "支付宝公益");

        frameView.addContent(tvTip);
        frameView.addContent(sivWeChatLove);
        frameView.addContent(sivAliPayLove);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

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

        Bundle bundle = new Bundle();
        bundle.putString("ext_info", "shortcut_5fc2a1c29f62c2a14923c3a2c2adc3bac2aec3bac282c2a42d12c3b4c3a4");
        bundle.putString("digest", "d952772408daca364707bd7bf54912aa");
        bundle.putString("id", "shortcut_4fc2b1c2a635c2a41d72c3adc3bcc2a9c3bdc3b2c28fc3b67f33c3b3c2a5c39b");
        bundle.putInt("type", 1);
        bundle.putString("ext_info_1", "1");
        bundle.putString("token", "f622a9dbb4c4eaea685e476f225ccabf");

        Intent intent = new Intent("com.tencent.mm.action.WX_SHORTCUT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXShortcutEntryActivity");
        intent.putExtras(bundle);

        if (!ActivityUtil.startActivity(getContext(), intent)) {
            // 跳转失败
            ToastUtil.show("跳转到微信公益失败!");
        }
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
