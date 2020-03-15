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
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
public class TitleView extends XFrameItemView implements View.OnClickListener {

    private ImageButton ivBack;
    private TextView tvTitle;
    private LinearLayout mMoreLayout;

    private OnBackEventListener mOnBackEventListener;

    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        int height = getTitleHeight();

        setLayoutParams(LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height));

        setBackgroundColor(UIConstant.Color.TITLE_BACKGROUND);

        LinearLayout tLayout = new LinearLayout(getContext());
        tLayout.setGravity(Gravity.CENTER_VERTICAL);
        tLayout.setOrientation(LinearLayout.HORIZONTAL);

        ivBack = new ImageButton(getContext());
        ivBack.setLayoutParams(LayoutUtil.newViewGroupParams(height, height));
        ivBack.setTag("back");
        ivBack.setBackground(ViewUtil.newTitleBackgroundDrawable());
        ivBack.setOnClickListener(this);

        tvTitle = new TextView(getContext());
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setLines(1);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setTextSize(18);
        tvTitle.getPaint().setFakeBoldText(true);

        tLayout.addView(ivBack);
        tLayout.addView(tvTitle);

        LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        // 更多的Layout
        mMoreLayout = new LinearLayout(getContext());
        mMoreLayout.setGravity(Gravity.CENTER_VERTICAL);
        mMoreLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams moreParams = LayoutUtil.newWrapFrameLayoutParams();
        moreParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;

        addView(tLayout, params);
        addView(mMoreLayout, moreParams);

        hideBack();
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    public int getTitleHeight() {
        return DisplayUtil.DIP_50;
    }

    public void addMoreView(View view) {
        mMoreLayout.addView(view, 0);
    }

    public ImageButton addMoreImageButton() {

        int height = getTitleHeight();

        ImageButton newImageButton = new ImageButton(getContext());
        newImageButton.setLayoutParams(LayoutUtil.newViewGroupParams(height, height));
        newImageButton.setBackground(ViewUtil.newTitleBackgroundDrawable());

        addMoreView(newImageButton);

        return newImageButton;
    }

    public TextView addMoreTextView(String text) {

        int height = getTitleHeight();
        int left = DisplayUtil.DIP_5;

        TextView newTextView = new TextView(getContext());
        newTextView.setText(text);
        newTextView.setTextSize(15);
        newTextView.getPaint().setFakeBoldText(true);
        newTextView.setTextColor(Color.WHITE);
        newTextView.setPadding(left, 0, left, 0);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setLayoutParams(LayoutUtil.newViewGroupParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        newTextView.setMinWidth(height);
        newTextView.setBackground(ViewUtil.newTitleBackgroundDrawable());

        addMoreView(newTextView);

        return newTextView;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    public void showBack() {
        ViewUtil.setVisibility(ivBack, View.VISIBLE);
        tvTitle.setPadding(0, 0, 0, 0);
    }

    public void hideBack() {
        ViewUtil.setVisibility(ivBack, View.GONE);
        tvTitle.setPadding(DisplayUtil.DIP_15, 0, 0, 0);
    }

    public ImageButton getBackView() {
        return ivBack;
    }

    public OnBackEventListener getOnBackEventListener() {
        return mOnBackEventListener;
    }

    public void setOnBackEventListener(OnBackEventListener onBackEventListener) {
        mOnBackEventListener = onBackEventListener;
    }

    @Override
    public void setElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setElevation(elevation);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnBackEventListener != null) mOnBackEventListener.onEvent(v);
    }

    public interface OnBackEventListener {

        void onEvent(View view);
    }
}
