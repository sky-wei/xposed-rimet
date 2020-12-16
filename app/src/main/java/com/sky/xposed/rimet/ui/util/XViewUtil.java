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

package com.sky.xposed.rimet.ui.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.rimet.BuildConfig;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.view.SimpleItemView;
import com.sky.xposed.ui.view.SortItemView;
import com.sky.xposed.ui.view.SwitchItemView;
import com.squareup.picasso.Picasso;

/**
 * Created by sky on 2019-12-13.
 */
public final class XViewUtil {

    private XViewUtil() {

    }

    /**
     * 创建View
     * @param context
     * @param imageRes
     * @param desc
     * @param listener
     * @return
     */
    public static LinearLayout newButtonView(Context context, int imageRes, String desc, View.OnClickListener listener) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(ResourceUtil.resourceIdToUri(
                BuildConfig.APPLICATION_ID, imageRes)).into(imageView);

        TextView textView = new TextView(context);
        textView.setText(desc);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xFF666666);
        textView.setTextSize(10);

        LinearLayout layout = new LinearLayout(context);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);

        int width = DisplayUtil.dip2px(context, 48);
        int bottom = DisplayUtil.dip2px(context, 6);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(0, 0, 0, bottom);

        layout.addView(imageView, params);
        layout.addView(textView, LayoutUtil.newWrapLinearLayoutParams());

        layout.setOnClickListener(listener);

        return layout;
    }

    public static SimpleItemView newSimpleItemView(Context context, String name) {
        return newSimpleItemView(context, name, null);
    }

    public static SimpleItemView newSimpleItemView(Context context, String name, View.OnClickListener listener) {

        SimpleItemView itemView = new SimpleItemView(context);
        itemView.setName(name);
        itemView.setOnClickListener(listener);

        return itemView;
    }

    public static SwitchItemView newSwitchItemView(Context context, String name) {
        return newSwitchItemView(context, name, "");
    }

    public static SwitchItemView newSwitchItemView(Context context, String name, String desc) {

        SwitchItemView itemView = new SwitchItemView(context);
        itemView.setName(name);
        itemView.setDesc(desc);

        return itemView;
    }

    /**
     * 创建一个分类的ItemView
     * @param context
     * @param name
     * @return
     */
    public static SortItemView newSortItemView(Context context, String name) {

        int margin = DisplayUtil.DIP_5;

        SortItemView itemView = new SortItemView(context);
        itemView.setLayoutParams(new LayoutUtil.Build()
                .setBottomMargin(margin)
                .setTopMargin(margin)
                .linearParams());
        itemView.setName(name);

        return itemView;
    }

    /**
     * 创建一个分类的ItemView
     * @param context
     * @param name
     * @return
     */
    public static SortItemView newTopSortItemView(Context context, String name) {

        int margin = DisplayUtil.DIP_5;

        SortItemView itemView = new SortItemView(context);
        itemView.setLayoutParams(new LayoutUtil.Build()
                .setBottomMargin(margin)
                .linearParams());
        itemView.setName(name);

        return itemView;
    }
}
