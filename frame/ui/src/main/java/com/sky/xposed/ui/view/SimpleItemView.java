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
import android.widget.TextView;

import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.ViewUtil;

/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SimpleItemView extends XFrameItemView {

    private TextView tvName;
    private TextView tvExtend;

    public SimpleItemView(Context context) {
        super(context);
    }

    public SimpleItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        int top = DisplayUtil.DIP_4;
        int left = DisplayUtil.DIP_15;

        setPadding(left, top, left, top);
        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setMinimumHeight(DisplayUtil.DIP_40);

        tvName = new TextView(getContext());
        tvName.setTextColor(UIConstant.Color.ITEM_TEXT);
        tvName.setTextSize(14f);
        tvName.setMaxLines(2);
        tvName.setEllipsize(TextUtils.TruncateAt.END);

        tvExtend = new TextView(getContext());
        tvExtend.setTextColor(UIConstant.Color.ITEM_TEXT_EXTEND);
        tvExtend.setGravity(Gravity.RIGHT);
        tvExtend.setTextSize(14f);
        tvExtend.setSingleLine(true);
        tvExtend.setEllipsize(TextUtils.TruncateAt.END);

        LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        addView(tvName, params);

        params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

        addView(tvExtend, params);
    }

    public TextView getNameView() {
        return tvName;
    }

    public TextView getExtendView() {
        return tvExtend;
    }

    public void setName(String title) {
        tvName.setText(title);
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public void setExtend(String value) {
        tvExtend.setText(value);
    }

    public String getExtend() {
        return tvExtend.getText().toString();
    }

    public void setExtendHint(String value) {
        tvExtend.setHint(value);
    }
}
