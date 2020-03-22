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

package com.sky.xposed.rimet.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.sky.xposed.rimet.R;
import com.sky.xposed.ui.base.BaseListAdapter;
import com.sky.xposed.ui.util.ViewUtil;

/**
 * Created by sky on 2019/4/3.
 */
public class SearchResultAdapter extends BaseListAdapter<PoiItem> {

    private int selectedPosition = 0;
    private String beginAddress;

    public SearchResultAdapter(Context context) {
        super(context);
    }

    public String getBeginAddress() {
        return beginAddress;
    }

    public void setBeginAddress(String beginAddress) {
        this.beginAddress = beginAddress;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_search_result, parent, false);
    }

    @Override
    public ViewHolder<PoiItem> onCreateViewHolder(View view, int viewType) {
        return new SearchResultHolder(view, this);
    }

    private class SearchResultHolder extends ViewHolder<PoiItem> {

        TextView tvTitle;
        TextView tvSubTitle;
        ImageView ivCheck;

        SearchResultHolder(View view, BaseListAdapter<PoiItem> itemView) {
            super(view, itemView);
        }

        @Override
        public void onInitialize() {
            super.onInitialize();

            tvTitle = mItemView.findViewById(R.id.tv_title);
            tvSubTitle = mItemView.findViewById(R.id.tv_title_sub);
            ivCheck = mItemView.findViewById(R.id.iv_check);
        }

        @Override
        public void onBind(int position, int viewType) {

            PoiItem poiItem = getItem(position);

            tvTitle.setText(poiItem.getTitle());
            tvSubTitle.setText(poiItemToString(poiItem));

            ViewUtil.setVisibility(ivCheck, position == selectedPosition ? View.VISIBLE : View.INVISIBLE);
            ViewUtil.setVisibility(tvSubTitle, (position == 0 && poiItem.getPoiId().equals("regeo") ? View.GONE : View.VISIBLE));
        }
    }

    public String poiItemToString(PoiItem poiItem) {

        if ("regeo".equals(poiItem.getPoiId())) {
            return poiItem.getSnippet();
        }

        if (beginAddress != null) {
            return beginAddress + poiItem.getSnippet();
        }

        StringBuilder builder = new StringBuilder(poiItem.getProvinceName());

        if (!TextUtils.equals(poiItem.getProvinceName(), poiItem.getCityName())) {
            builder.append(poiItem.getCityName());
        }

        builder.append(poiItem.getAdName());

        if (!TextUtils.equals(poiItem.getAdName(), poiItem.getSnippet())) {
            builder.append(poiItem.getSnippet());
        }

        return builder.toString();
    }
}
