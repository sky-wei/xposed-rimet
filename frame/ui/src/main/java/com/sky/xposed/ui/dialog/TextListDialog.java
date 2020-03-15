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

package com.sky.xposed.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sky.xposed.ui.adapter.TextListAdapter;
import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.info.TextItem;
import com.sky.xposed.ui.util.ViewUtil;
import com.sky.xposed.ui.view.PluginFrameLayout;

import java.util.List;

/**
 * Created by sky on 2019-09-02.
 */
public abstract class TextListDialog extends BasePluginDialog implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private TextListAdapter mTextListAdapter;

    @Override
    public void createView(PluginFrameLayout frameView) {

        // 创建列表View
        mListView = ViewUtil.newListView(getContext());
        frameView.addSubView(frameView);
    }

    @Override
    protected PluginFrameLayout onCreatePluginFrame() {
        return createLinePluginFrame();
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        mTextListAdapter = new TextListAdapter(getContext());
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mTextListAdapter);
    }

    public ListView getListView() {
        return mListView;
    }

    public TextListAdapter getTextListAdapter() {
        return mTextListAdapter;
    }

    /**
     * 加载数据
     */
    public void loadMenuData() {
        mTextListAdapter.setItems(onLoadMenuItems());
        mTextListAdapter.notifyDataSetChanged();
    }

    /**
     * 加载菜单项
     * @return
     */
    public abstract List<TextItem> onLoadMenuItems();

    /**
     * 菜单点击事件
     * @param item
     * @return
     */
    public boolean onItemClick(TextItem item) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 响应点击事件
        onItemClick(mTextListAdapter.getItem(position));
    }
}
