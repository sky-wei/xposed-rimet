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

package com.sky.xposed.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.ViewUtil;


/**
 * Created by sky on 2019-06-10.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TextItemView extends XFrameItemView {

    private TextView tvName;
    private TextView tvDesc;
    private View mLine;

    public TextItemView(Context context) {
        super(context);
    }

    public TextItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setMinimumHeight(DisplayUtil.DIP_40);

        LinearLayout tvLayout = new LinearLayout(getContext());
        tvLayout.setOrientation(LinearLayout.VERTICAL);

        tvName = new TextView(getContext());
        tvName.setTextColor(UIConstant.Color.ITEM_TEXT);
        tvName.setSingleLine(true);
        tvName.setTextSize(14f);
        tvName.setGravity(Gravity.CENTER_VERTICAL);
        tvName.setEllipsize(TextUtils.TruncateAt.END);

        tvDesc = new TextView(getContext());
        tvDesc.setTextColor(UIConstant.Color.ITEM_TEXT_EXTEND);
        tvDesc.setTextSize(9);
        tvDesc.setVisibility(GONE);
        tvDesc.setSingleLine(true);
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvDesc.setPadding(DisplayUtil.DIP_1, 0, 0, 0);

        tvLayout.addView(tvName);
        tvLayout.addView(tvDesc);

        int topMargin = DisplayUtil.DIP_6;
        int leftMargin = DisplayUtil.DIP_15;

        addView(tvLayout, new LayoutUtil.Build()
                .setGravity(Gravity.CENTER_VERTICAL)
                .setLeftMargin(leftMargin)
                .setRightMargin(leftMargin << 1)
                .setTopMargin(topMargin)
                .setBottomMargin(topMargin)
                .frameParams());

        mLine = ViewUtil.newLineView(getContext());
        mLine.setVisibility(GONE);

        addView(mLine, new LayoutUtil.Build()
                .setHeight(2)
                .setLeftMargin(leftMargin)
                .setRightMargin(leftMargin)
                .frameParams());
    }

    public TextView getNameView() {
        return tvName;
    }

    public TextView getDescView() {
        return tvDesc;
    }

    public View getLineView() {
        return mLine;
    }

    public void setName(String title) {
        tvName.setText(title);
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
        ViewUtil.setVisibility(tvDesc,
                TextUtils.isEmpty(desc) ? View.GONE : View.VISIBLE);
    }

    public String getDesc() {
        return tvDesc.getText().toString();
    }

    public void setLineVisibility(boolean show) {
        ViewUtil.setVisibility(mLine, show ? View.VISIBLE : View.INVISIBLE);
    }
}
