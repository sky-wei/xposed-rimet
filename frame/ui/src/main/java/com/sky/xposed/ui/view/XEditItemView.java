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

import com.sky.xposed.ui.UIAttribute;
import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.util.DialogUtil;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.ViewUtil;

/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public abstract class XEditItemView<T> extends XTrackItemView<T> implements View.OnClickListener {

    public interface Style {

        int SINGLE_LINE = 0x00;     // 单行样式

        int MULTI_LINE = 0x01;      // 多行样式
    }

    private TextView tvName;
    private TextView tvExtend;
    private String mExtend;
    private String mUnit;
    private int mMaxLength;
    private int mInputType = UIConstant.InputType.TEXT;
    private OnItemClickListener mOnItemClickListener;
    private OnEditChangeListener mOnEditChangeListener;

    public XEditItemView(Context context) {
        super(context);
    }

    public XEditItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        int style = attrs.getInt(UIAttribute.EditTextItem.style, Style.SINGLE_LINE);

        int top = DisplayUtil.DIP_4;
        int left = DisplayUtil.DIP_15;
        int xTop = Style.MULTI_LINE == style ? DisplayUtil.DIP_8 : DisplayUtil.DIP_4;

        setPadding(left, xTop, left, xTop);
        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        if (Style.MULTI_LINE == style) {
            // 多行模式
            setMinimumHeight(DisplayUtil.DIP_55);

            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            tvName = new TextView(getContext());
            tvName.setTextColor(UIConstant.Color.ITEM_TEXT);
            tvName.setTextSize(14);
            tvName.setSingleLine(true);
            tvName.setEllipsize(TextUtils.TruncateAt.END);

            tvExtend = new TextView(getContext());
            tvExtend.setTextColor(UIConstant.Color.ITEM_TEXT_EXTEND);
            tvExtend.setHintTextColor(UIConstant.Color.ITEM_TEXT_EXTEND);
            tvExtend.setTextSize(14);
            tvExtend.setMaxLines(3);
            tvExtend.setPadding(0, top << 1, 0, 0);
            tvExtend.setEllipsize(TextUtils.TruncateAt.END);

            layout.addView(tvName);
            layout.addView(tvExtend);

            LayoutParams params = LayoutUtil.newFrameLayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;

            addView(layout, params);
        } else {
            // 默认单行模式
            setMinimumHeight(DisplayUtil.DIP_40);

            tvName = new TextView(getContext());
            tvName.setTextColor(UIConstant.Color.ITEM_TEXT);
            tvName.setTextSize(14);
            tvName.setMaxLines(2);
            tvName.setEllipsize(TextUtils.TruncateAt.END);

            tvExtend = new TextView(getContext());
            tvExtend.setTextColor(UIConstant.Color.ITEM_TEXT_EXTEND);
            tvExtend.setHintTextColor(UIConstant.Color.ITEM_TEXT_EXTEND);
            tvExtend.setGravity(Gravity.RIGHT);
            tvExtend.setTextSize(14);
            tvExtend.setSingleLine(true);
            tvExtend.setEllipsize(TextUtils.TruncateAt.END);

            LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
            params.gravity = Gravity.CENTER_VERTICAL;

            addView(tvName, params);

            params = LayoutUtil.newWrapFrameLayoutParams();
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            params.leftMargin = DisplayUtil.DIP_120;

            addView(tvExtend, params);
        }

        setOnClickListener(this);
    }

    public OnEditChangeListener getOnEditChangeListener() {
        return mOnEditChangeListener;
    }

    public void setOnEditChangeListener(OnEditChangeListener onEditChangeListener) {
        mOnEditChangeListener = onEditChangeListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
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

        // 保存设置信息
        mExtend = value;

        if ((UIConstant.InputType.NUMBER_PASSWORD == mInputType
                || UIConstant.InputType.TEXT_PASSWORD == mInputType)
                && !TextUtils.isEmpty(value)) {
            tvExtend.setText("******");
            return;
        }

        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(mUnit)) {
            tvExtend.setText(value);
            tvExtend.append(mUnit);
            return;
        }

        if (mMaxLength != 0 && value.length() > mMaxLength) {
            tvExtend.setText(value.substring(0, mMaxLength));
            tvExtend.append("...");
            return;
        }

        tvExtend.setText(value);
    }

    public void setExtendHint(String value) {
        tvExtend.setHint(value);
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public void setMaxLength(int maxLength) {
        mMaxLength = maxLength;
    }

    public int getInputType() {
        return mInputType;
    }

    public void setInputType(int inputType) {
        mInputType = inputType;
    }

    public String getExtend() {
        return mExtend;
    }

    @Override
    public void onClick(View v) {

        if (mOnEditChangeListener == null) {
            // 内部处理
            mOnEditChangeListener = new OnEditChangeListener() {
                @Override
                public String getDefaultText() {
                    return getExtend();
                }

                @Override
                public void onEditChanged(View view, String text) {
                    setExtend(text);
                }
            };
        }

        if (mOnItemClickListener != null) {
            // 拦截事件,不需要处理后面的了
            mOnItemClickListener.onItemClick(this);
            return;
        }

        // 显示默认输入提示框
        DialogUtil.showInputDialog(getContext(), getName(),
                tvExtend.getHint().toString(), mInputType, mOnEditChangeListener);
    }

    /**
     * 编辑状态监听
     */
    public interface OnEditChangeListener {

        /**
         * 获取默认的内容
         * @return
         */
        String getDefaultText();

        /**
         * 编辑状态修改回调方法
         * @param view
         * @param text
         */
        void onEditChanged(View view, String text);
    }

    /**
     * EditItemView点击事件监听
     */
    public interface OnItemClickListener {

        /**
         * Item点击回调方法
         * @param view
         */
        void onItemClick(View view);
    }
}
