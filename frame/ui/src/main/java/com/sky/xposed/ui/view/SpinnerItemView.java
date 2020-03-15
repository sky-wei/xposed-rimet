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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.adapter.DisplayListAdapter;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.interfaces.XDisplayItem;
import com.sky.xposed.ui.util.DialogUtil;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sky on 2018/8/20.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class SpinnerItemView extends XTrackItemView<Integer> implements View.OnClickListener {

    public interface Style {

        int DIALOG = 0x01;

        int POPUP = 0x02;
    }

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvValue;
    private OnValueChangeListener mOnValueChangeListener;
    private List<XDisplayItem> mDisplayItems;
    private XDisplayItem mDisplayItem;

    private int mStyle = Style.POPUP;     // 展示样式

    public SpinnerItemView(Context context) {
        super(context);
    }

    public SpinnerItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    public OnValueChangeListener getOnValueChangeListener() {
        return mOnValueChangeListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    @Override
    protected void initView(XAttributeSet attrs) {

        int top = DisplayUtil.DIP_4;
        int left = DisplayUtil.DIP_15;

        setPadding(left, top, left, top);
        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        setMinimumHeight(DisplayUtil.DIP_40);

        LinearLayout tvLayout = new LinearLayout(getContext());
        tvLayout.setOrientation(LinearLayout.VERTICAL);

        tvName = new TextView(getContext());
        tvName.setTextColor(UIConstant.Color.ITEM_TEXT);
        tvName.setTextSize(14);
        tvName.setMaxLines(2);
        tvName.setEllipsize(TextUtils.TruncateAt.END);

        tvDesc = new TextView(getContext());
        tvDesc.setTextColor(Color.GRAY);
        tvDesc.setTextSize(9);
        tvDesc.setMaxLines(2);
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvDesc.setPadding(DisplayUtil.DIP_1, 0, 0, 0);

        tvLayout.addView(tvName);
        tvLayout.addView(tvDesc);

        FrameLayout.LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        addView(tvLayout, params);

        // 扩展的Layout
        LinearLayout extendLayout = new LinearLayout(getContext());
        extendLayout.setGravity(Gravity.CENTER_VERTICAL);
        extendLayout.setOrientation(LinearLayout.HORIZONTAL);

        tvValue = new TextView(getContext());
        tvValue.setTextColor(Color.GRAY);
        tvValue.setTextSize(14);
        tvValue.setSingleLine(true);
        tvValue.setEllipsize(TextUtils.TruncateAt.END);

        int pLeft = DisplayUtil.DIP_10;
        TextView tvSymbol = new TextView(getContext());
        tvSymbol.setTextColor(UIConstant.Color.ITEM_TEXT);
        tvSymbol.setPadding(pLeft, 0, pLeft >> 2, 0);
        tvSymbol.setTextSize(20);
        tvSymbol.setText("▾");

        LinearLayout.LayoutParams valueParams = LayoutUtil.newWrapLinearLayoutParams();
        valueParams.weight = 1f;

        extendLayout.addView(tvValue, valueParams);
        extendLayout.addView(tvSymbol);

        params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        params.leftMargin = DisplayUtil.DIP_100;

        addView(extendLayout, params);

        setOnClickListener(this);
    }

    public TextView getNameView() {
        return tvName;
    }

    public TextView getDescView() {
        return tvDesc;
    }

    public TextView getValueView() {
        return tvValue;
    }

    public void setChooseItem(String... items) {

        if (items == null) return;

        mDisplayItems = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            mDisplayItems.add(new InternalDisplayItem(i, items[i]));
        }
    }

    public void setChooseItem(XDisplayItem... items) {
        if (items != null) setChooseItem(Arrays.asList(items));
    }

    public void setChooseItem(List<? extends XDisplayItem> items) {
        mDisplayItems = (List<XDisplayItem>) items;
    }

    public void setName(String title) {
        tvName.setText(title);
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
        ViewUtil.setVisibility(tvDesc,
                TextUtils.isEmpty(desc) ? View.GONE : View.VISIBLE);
    }

    public String getDesc() {
        return tvDesc.getText().toString();
    }

    /**
     * 选择相应的id
     * @param id
     * @return 返回选择是否成功
     */
    public boolean chooseItem(int id) {
        XDisplayItem item = getDisplayItemById(id);
        setDisplayItem(item);
        return item != null;
    }

    public XDisplayItem getDisplayItem() {
        return mDisplayItem;
    }

    public void setDisplayItem(XDisplayItem displayItem) {

        if (displayItem == null) {
            // 设置空信息
            mDisplayItem = null;
            tvValue.setText("空");
            return;
        }

        mDisplayItem = displayItem;
        tvValue.setText(displayItem.getName());
    }

    private XDisplayItem getDisplayItemById(int id) {

        if (mDisplayItems == null) return null;

        for (XDisplayItem item : mDisplayItems) {

            if (id == item.getId()) return item;
        }
        return null;
    }

    @Override
    public void onClick(View v) {

        if (mDisplayItems == null) return;

        if (Style.DIALOG == mStyle) {
            // 设置控件信息
            DialogUtil.showChooseDialog(getContext(),
                    getName(), mDisplayItems, this::callDisplayItem);
            return;
        }

        if (CollectionUtil.isEmpty(mDisplayItems)) return;

        DisplayListAdapter adapter = new DisplayListAdapter(getContext());
        adapter.setItems(mDisplayItems);

        ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setAnchorView(this);
        listPopupWindow.setModal(true);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setWidth(getWidth() - DisplayUtil.DIP_10);
        listPopupWindow.setHorizontalOffset(DisplayUtil.DIP_5);
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            listPopupWindow.dismiss();
            callDisplayItem(mDisplayItems.get(position));
        });
        listPopupWindow.show();
    }

    private void callDisplayItem(XDisplayItem item) {
        setDisplayItem(item);
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChanged(SpinnerItemView.this, getDisplayItem());
        }
    }

    @Override
    protected void bindView(String key, Integer value) {

        chooseItem(value);
        setOnValueChangeListener((view, item) -> {

            if (callOnStatusChange(view, key, item.getId())) {
                // 保存信息
                getPreferences().putInt(key, item.getId());
            }
        });
    }

    @Override
    public Integer getKeyValue() {
        return getPreferences().getInt(getKey(), getDefValue());
    }

    private static final class InternalDisplayItem implements XDisplayItem {

        private final int id;
        private final String name;

        InternalDisplayItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getValue() {
            return null;
        }
    }

    public interface OnValueChangeListener {

        void onValueChanged(View view, XDisplayItem item);
    }
}
