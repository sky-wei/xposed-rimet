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

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.interfaces.XFrameLayout;
import com.sky.xposed.ui.interfaces.XItemView;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.ViewUtil;

/**
 * Created by sky on 2019-06-18.
 */
public abstract class XLinearItemView extends LinearLayout implements XItemView {

    public XLinearItemView(Context context) {
        this(context, null);
    }

    public XLinearItemView(Context context, XAttributeSet attrs) {
        super(context, null, 0);
        initView(attrs);
    }

    /**
     * 初始化View
     */
    protected abstract void initView(XAttributeSet attrs);

    /**
     * dip转px
     * @param dip
     * @return
     */
    public int dip2px(float dip) {
        return DisplayUtil.dip2px(getContext(), dip);
    }

    /**
     * 设置控件是否显示(只有VISIBLE与GONE)
     * @param show true:显示,false:不显示
     */
    @Override
    public void setVisibility(boolean show) {
        ViewUtil.setVisibility(this, show ? View.VISIBLE : View.GONE);
    }

    /**
     * 把View添加指定Layout中
     * @param frame
     * @return
     */
    @Override
    public XItemView addToFrame(XFrameLayout frame) {
        frame.addSubView(this);
        return this;
    }

    @Override
    public XItemView addToFrame(XFrameLayout frame, boolean line) {
        frame.addSubView(this, line);
        return this;
    }
}
