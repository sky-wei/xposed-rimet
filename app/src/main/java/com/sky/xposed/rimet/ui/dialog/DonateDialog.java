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

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.common.ui.util.CommUtil;
import com.sky.xposed.common.ui.util.LayoutUtil;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.DisplayUtil;
import com.sky.xposed.common.util.ToastUtil;
import com.sky.xposed.rimet.R;
import com.sky.xposed.rimet.ui.util.ActivityUtil;
import com.sky.xposed.rimet.ui.util.UriUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by sky on 2019/3/13.
 */
public class DonateDialog extends CommonDialog {

    static final int CLICK = 0x01;
    static final int LONG_CLICK = 0x02;

    private SimpleItemView sivAliPayDonate;
    private SimpleItemView sivWeChatDonate;

    @Override
    public void createView(CommonFrameLayout frameView) {

        sivWeChatDonate = ViewUtil.newSimpleItemView(getContext(), "微信捐赠");
        sivAliPayDonate = ViewUtil.newSimpleItemView(getContext(), "支付宝捐赠");

        frameView.addContent(sivWeChatDonate);
        frameView.addContent(sivAliPayDonate);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        setTitle("支持我们");

        sivAliPayDonate.setOnClickListener(v -> {
            // 启动支付宝
            showDonateImageDialog(
                    "*点击支付二维码即可打开支付宝*",
                    UriUtil.getResource(R.drawable.alipay),
                    CLICK, (eventType, uri) -> {
                        // 直接拉起支付宝
                        ToastUtil.show("正在启动支付宝，感谢您的支持！");

                        if (!ActivityUtil.startAlipay(getContext(),
                                "HTTPS://QR.ALIPAY.COM/FKX05261FCJGZABDGWMR46")) {
                            ToastUtil.show("启动支付宝失败");
                        }
                        return true;
                    });
        });

        sivWeChatDonate.setOnClickListener(v -> {
            // 微信捐赠
            showDonateImageDialog(
                    "*长按保存到相册,再通过微信扫码二维码*",
                    UriUtil.getResource(R.drawable.wechat),
                    LONG_CLICK, (eventType, uri) -> {

                        Picasso.get().load(uri).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                if (bitmap == null) return;

                                File imagePath = new File(Environment
                                        .getExternalStorageDirectory(), "DCIM/wechat.png");

                                // 保存图片
                                if (CommUtil.saveImage2SDCard(imagePath.getPath(), bitmap)) {
                                    CommUtil.scanFile(getContext(), imagePath.getPath());
                                    ToastUtil.show("图片已保存到本地，感谢您的支持！");
                                } else {
                                    ToastUtil.show("图片保存失败！");
                                }
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                ToastUtil.show("保存图片失败");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
                        return true;
                    });
        });
    }

    /**
     * 显示气带图片捐赠的Dialog
     * @param desc
     * @param uri
     * @param eventType
     * @param listener
     */
    private void showDonateImageDialog(String desc, final Uri uri,
                                       final int eventType, final OnEventListener listener) {

        try {
            int left = DisplayUtil.dip2px(getContext(), 25f);
            int top = DisplayUtil.dip2px(getContext(), 10f);

            LinearLayout.LayoutParams contentParams = LayoutUtil.newMatchLinearLayoutParams();

            LinearLayout content = new LinearLayout(getContext());
            content.setLayoutParams(contentParams);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setBackgroundColor(Color.WHITE);
            content.setPadding(left, top, left, 0);

            TextView tvHead = new TextView(getContext());
            tvHead.setTextColor(0xff8f8f8f);
            tvHead.setTextSize(12f);
            tvHead.setText(desc);

            ImageView ivImage = new ImageView(getContext());
            ivImage.setLayoutParams(LayoutUtil.newWrapLinearLayoutParams());
            Picasso.get().load(uri).into(ivImage);

            switch (eventType) {
                case CLICK:
                    ivImage.setOnClickListener(v -> listener.onEvent(eventType, uri));
                    break;
                case LONG_CLICK:
                    ivImage.setOnLongClickListener(v -> listener.onEvent(eventType, uri));
                    break;
            }

            content.addView(tvHead);
            content.addView(ivImage);

            // 显示关于
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("感谢您的支持");
            builder.setView(content);
            builder.setPositiveButton("关闭", (dialog, which) -> dialog.dismiss());
            builder.show();
        } catch (Throwable tr) {
            Alog.e("异常了", tr);
        }
    }

    private interface OnEventListener {

        boolean onEvent(int eventType, Uri uri);
    }
}
