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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
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
public class SwitchItemView extends XTrackItemView<Boolean> implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView tvName;
    private TextView tvDesc;
    private Switch mSwitch;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchItemView(Context context) {
        super(context);
    }

    public SwitchItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        int top = DisplayUtil.DIP_4;
        int left = DisplayUtil.DIP_15;

        setPadding(left, top, left, top);
        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        setMinimumHeight(DisplayUtil.DIP_40);

        LinearLayout tvLayout = new LinearLayout(getContext());
        tvLayout.setOrientation(LinearLayout.VERTICAL);


        tvName = new TextView(getContext());
        tvName.setTextColor(UIConstant.Color.ITEM_TEXT);
        tvName.setTextSize(14);
        tvName.setMaxLines(2);
        tvName.setEllipsize(TextUtils.TruncateAt.END);

        tvDesc = new TextView(getContext());
        tvDesc.setTextColor(Color.GRAY);
        tvDesc.setTextSize(9);
        tvDesc.setMaxLines(2);
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvDesc.setPadding(DisplayUtil.DIP_1, 0, 0, 0);

        tvLayout.addView(tvName);
        tvLayout.addView(tvDesc);

        FrameLayout.LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        params.rightMargin = DisplayUtil.DIP_50;

        addView(tvLayout, params);

        mSwitch = new Switch(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            int[][] states = new int[2][];
            states[0] = new int[] { android.R.attr.state_checked };
            states[1] = new int[] { };

            int[] colors = new int[] {
                    UIConstant.Color.ITEM_STATE_CHECKED,
                    UIConstant.Color.ITEM_STATE_NORMAL
            };

            mSwitch.setTrackTintList(new ColorStateList(states, colors));
            mSwitch.setThumbTintList(new ColorStateList(states, colors));
        }

        mSwitch.setClickable(false);
        mSwitch.setFocusable(false);
        mSwitch.setFocusableInTouchMode(false);
        mSwitch.setOnCheckedChangeListener(this);

        params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

        addView(mSwitch, params);

        setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, isChecked);
        }
    }

    public TextView getNameView() {
        return tvName;
    }

    public TextView getDescView() {
        return tvDesc;
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

    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
        tvName.setTextColor(isChecked() ? UIConstant.Color.ITEM_TEXT : UIConstant.Color.ITEM_TEXT_DISABLE);
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    @Override
    public void onClick(View v) {
        setChecked(!isChecked());
    }

    @Override
    protected void bindView(String key, Boolean value) {

        // 设置状态
        setChecked(value);
        setOnCheckedChangeListener((view, isChecked) -> {

            if (callOnStatusChange(view, key, isChecked)) {
                // 保存状态信息
                getPreferences().putBoolean(key, isChecked);
            }
        });
    }

    @Override
    public Boolean getKeyValue() {
        return getPreferences().getBoolean(getKey(), getDefValue());
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(View view, boolean isChecked);
    }
}
