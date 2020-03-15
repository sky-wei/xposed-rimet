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
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.xposed.ui.R;

/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ItemMenu extends FrameLayout {

    private View viewLine;
    private TextView tvName;
    private TextView tvDesc;
    private ImageView ivArrow;

    public ItemMenu(Context context) {
        this(context, null);
    }

    public ItemMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(getContext())
                .inflate(R.layout.customize_item_menu, this, true);

        viewLine = findViewById(R.id.view_line);
        tvName = findViewById(R.id.tv_name);
        tvDesc = findViewById(R.id.tv_desc);
        ivArrow = findViewById(R.id.iv_arrow);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ItemMenu, defStyleAttr, 0);

        for (int i = 0; i < a.getIndexCount(); i++) {

            int attr = a.getIndex(i);

            if (attr == R.styleable.ItemMenu_sky_itemMenuName) {
                setName(a.getString(attr));
            } else if (attr == R.styleable.ItemMenu_sky_itemMenuDesc) {
                setDesc(a.getString(attr));
            } else if (attr == R.styleable.ItemMenu_sky_itemMenuLine) {
                setShowLine(a.getBoolean(attr, true));
            } else if (attr == R.styleable.ItemMenu_sky_itemMenuTextSize) {
                setTextSize(a.getDimensionPixelSize(attr, 14));
            } else if (attr == R.styleable.ItemMenu_sky_itemMenuShowMore) {
                setShowMore(a.getBoolean(attr, true));
            }
        }
        a.recycle();
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    public void setTextSize(int size) {
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setShowLine(boolean show) {
        viewLine.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setShowMore(boolean show) {
        ivArrow.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
