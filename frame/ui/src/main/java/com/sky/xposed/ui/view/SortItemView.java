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
import android.view.Gravity;
import android.widget.TextView;

import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;

/**
 * Created by sky on 2019-06-11.
 */
public class SortItemView extends XFrameItemView {

    private TextView tvName;

    public SortItemView(Context context) {
        super(context);
    }

    public SortItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        setBackgroundColor(UIConstant.Color.SORT_ITEM_BACKGROUND);

        int top = DisplayUtil.DIP_8;
        int left = DisplayUtil.DIP_15;

        setPadding(left, top, left, top);
        setLayoutParams(LayoutUtil.newViewGroupParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tvName = new TextView(getContext());
        tvName.setTextColor(UIConstant.Color.SORT_ITEM_TEXT);
        tvName.getPaint().setFakeBoldText(true);
        tvName.setTextSize(14f);

        LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        addView(tvName, params);
    }

    public TextView getNameView() {
        return tvName;
    }

    public void setName(String text) {
        tvName.setText(text);
    }

    public String getName() {
        return tvName.getText().toString();
    }
}
