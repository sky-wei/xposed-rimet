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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.interfaces.XFrameLayout;
import com.sky.xposed.ui.util.ViewUtil;

/**
 * Created by sky on 2019-09-11.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PluginLinearLayout extends LinearLayout implements XFrameLayout {

    public PluginLinearLayout(Context context) {
        this(context, null);
    }

    public PluginLinearLayout(Context context, XAttributeSet attrs) {
        super(context, null, 0);
        initView(attrs);
    }

    /**
     * 初始化View
     * @param attrs
     */
    protected void initView(XAttributeSet attrs) {

        setOrientation(VERTICAL);
        setBackgroundColor(UIConstant.Color.DEFAULT_BACKGROUND);
    }

    @Override
    public void addSubView(View child) {
        addView(child);
    }

    @Override
    public void addSubView(View child, int index) {
        addView(child, index);
    }

    @Override
    public void addSubView(View child, ViewGroup.LayoutParams params) {
        addView(child, params);
    }

    @Override
    public void addSubView(View child, boolean line) {
        addView(child);
        if (line) addView(ViewUtil.newLineView(getContext()));
    }
}
