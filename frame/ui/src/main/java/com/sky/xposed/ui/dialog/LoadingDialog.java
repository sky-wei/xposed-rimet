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

package com.sky.xposed.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;

/**
 * Created by sky on 2019-09-29.
 */
public class LoadingDialog extends Dialog {

    private TextView tvTip;

    public LoadingDialog(Context context) {
        super(context);

        initView();
    }

    private void initView() {

        // 不显示默认标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        FrameLayout mFrameLayout = new FrameLayout(getContext());
        mFrameLayout.setLayoutParams(LayoutUtil.newMatchFrameLayoutParams());

        LinearLayout contentLayout = new LinearLayout(getContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setGravity(Gravity.CENTER_VERTICAL);
        contentLayout.setLayoutParams(new LayoutUtil.Build()
                .setWidth(DisplayUtil.dip2px(getContext(), 160))
                .setHeight(DisplayUtil.dip2px(getContext(), 70))
                .linearParams());

        ProgressBar mProgressBar = new ProgressBar(getContext());

        contentLayout.addView(mProgressBar, new LayoutUtil.Build()
                .setWidth(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setLeftMargin(DisplayUtil.DIP_10)
                .linearParams());

        tvTip = new TextView(getContext());
        tvTip.setSingleLine(true);
        tvTip.setTextSize(16);
        tvTip.setTextColor(0xffd5d5d5);
        tvTip.setText("Loading...");

        contentLayout.addView(tvTip, new LayoutUtil.Build()
                .setWidth(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setLeftMargin(DisplayUtil.DIP_10)
                .linearParams());
        mFrameLayout.addView(contentLayout);

        setContentView(mFrameLayout);
    }

    @Override
    public void show() {

        // 获取Dialog的Window
        Window window = getWindow();

        if (window != null) {
            // 设置属性信息
            GradientDrawable defDrawable = new GradientDrawable();
            defDrawable.setColor(0x6f000000);
            defDrawable.setCornerRadius(12);
            window.setBackgroundDrawable(defDrawable);
        }
        super.show();
    }

    public void setTip(String text) {
        tvTip.setText(text);
    }
}
