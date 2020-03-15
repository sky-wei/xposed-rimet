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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.sky.xposed.ui.UIAttribute;
import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.cardview.CardView;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.interfaces.XFrameLayout;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.ViewUtil;

/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PluginFrameLayout extends FrameLayout implements XFrameLayout {

    public interface Style {

        int SCROLL = 0x01;          // 滚动竖向线性布局

        int LINE = 0x02;            // 竖向纯线性布局
    }

    private CardView mSubFrame;

    private TitleView mTitleView;

    private LinearLayout mContent;
    private LinearLayout mEndView;

    public PluginFrameLayout(Context context) {
        this(context, null);
    }

    public PluginFrameLayout(Context context, XAttributeSet attrs) {
        super(context, null, 0);
        initView(attrs);
    }

    /**
     * 初始化View
     * @param attrs
     */
    protected void initView(XAttributeSet attrs) {

        mSubFrame = new CardView(getContext());
        mSubFrame.setOrientation(LinearLayout.VERTICAL);
        mSubFrame.setCardBackgroundColor(UIConstant.Color.DEFAULT_BACKGROUND);

        addView(mSubFrame, new LayoutUtil.Build()
                .setTopMargin(DisplayUtil.DIP_10)
                .setBottomMargin(DisplayUtil.DIP_10)
                .setLeftMargin(DisplayUtil.DIP_10)
                .setRightMargin(DisplayUtil.DIP_10)
                .frameParams()
        );

        // 添加标题
        mTitleView = new TitleView(getContext(), attrs);
        mSubFrame.addView(mTitleView);


        // 获取样式
        int style = attrs.getInt(UIAttribute.PluginFrame.style, Style.LINE);

        if (Style.LINE == style) {
            // 竖向纯线性布局
            mContent = LayoutUtil.newPluginLinearLayout(getContext(), attrs);
            mSubFrame.addView(mContent);
        } else {
            // 滚动竖向线性布局
            ScrollView scrollView = new ScrollView(getContext());
            scrollView.setLayoutParams(new LayoutUtil.Build()
                    .setWeight(1.0f)
                    .linearParams());

            mContent = new LinearLayout(getContext());
            mContent.setLayoutParams(new LayoutUtil.Build()
                    .linearParams());
            mContent.setOrientation(LinearLayout.VERTICAL);
            mContent.setPadding(0, DisplayUtil.DIP_5, 0, DisplayUtil.DIP_5);

            scrollView.addView(mContent);
            mSubFrame.addView(scrollView);
        }


        mEndView = new LinearLayout(getContext());
//        mEndView.setBackgroundColor(Color.WHITE);
        mEndView.setLayoutParams(LayoutUtil.newMatchLinearLayoutParams());
        mEndView.setPadding(0, DisplayUtil.DIP_5, 0, 0);
        mEndView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        mEndView.setOrientation(LinearLayout.HORIZONTAL);
        mEndView.setVisibility(GONE);

        mSubFrame.addView(mEndView);
    }

    public TitleView getTitleView() {
        return mTitleView;
    }

    public LinearLayout getContentView() {
        return mContent;
    }

    public LinearLayout getEndView() {
        return mEndView;
    }

    public void setTitle(String title) {
        mTitleView.setTitle(title);
    }

    @Override
    public void addSubView(View child) {
        mContent.addView(child);
    }

    @Override
    public void addSubView(View child, int index) {
        mContent.addView(child, index);
    }

    @Override
    public void addSubView(View child, ViewGroup.LayoutParams params) {
        mContent.addView(child, params);
    }

    @Override
    public void addSubView(View child, boolean line) {
        mContent.addView(child);
        if (line) mContent.addView(ViewUtil.newLineView(getContext()));
    }

    public void showEndView() {
        ViewUtil.setVisibility(mEndView, View.VISIBLE);
    }

    public PluginFrameLayout addToEndView(View child) {
        mEndView.addView(child);
        return this;
    }
}

