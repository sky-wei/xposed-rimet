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

package com.sky.xposed.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.xposed.ui.base.BaseListAdapter;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.interfaces.XDisplayItem;
import com.sky.xposed.ui.view.DisplayItemView;


/**
 * Created by sky on 2019-06-26.
 */
public class DisplayListAdapter extends BaseListAdapter<XDisplayItem> {

    public DisplayListAdapter(Context context) {
        super(context);
    }

    public DisplayListAdapter(Context context, XAttributeSet attrs) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new DisplayItemView(getContext());
    }

    @Override
    public ViewHolder<XDisplayItem> onCreateViewHolder(View view, int viewType) {
        return new DisplayViewHolder(view, this);
    }

    private final class DisplayViewHolder extends ViewHolder<XDisplayItem> {

        DisplayItemView mDisplayItemView;

        DisplayViewHolder(View itemView, BaseListAdapter<XDisplayItem> baseListAdapter) {
            super(itemView, baseListAdapter);
        }

        @Override
        public void onInitialize() {
            super.onInitialize();

            mDisplayItemView = (DisplayItemView) mItemView;
        }

        @Override
        public void onBind(int position, int viewType) {

            XDisplayItem item = getItem(position);

            // 设置内容
            mDisplayItemView.setName(item.getName());
        }
    }
}
