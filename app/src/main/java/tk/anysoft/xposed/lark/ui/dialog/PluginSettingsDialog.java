/*
 * Copyright (c) 2019 The sky Authors.
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

package tk.anysoft.xposed.lark.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import tk.anysoft.xposed.lark.ui.adapter.PluginSettingsAdapter;
import com.sky.xposed.common.ui.util.LayoutUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.TitleView;
import tk.anysoft.xposed.lark.BuildConfig;
import tk.anysoft.xposed.lark.Constant;
import tk.anysoft.xposed.lark.R;
import tk.anysoft.xposed.lark.plugin.interfaces.XPlugin;
import tk.anysoft.xposed.lark.ui.base.BaseDialog;
import tk.anysoft.xposed.lark.ui.util.DialogUtil;
import tk.anysoft.xposed.lark.ui.util.UriUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sky on 2018/12/30.
 */
public class PluginSettingsDialog extends BaseDialog implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;
    private ImageButton mMoreButton;

    private ListView mListView;
    private PluginSettingsAdapter mPluginListAdapter;

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();
        mMoreButton = mToolbar.addMoreImageButton();

        LinearLayout content = LayoutUtil.newCommonLayout(getContext());

        mListView = new ListView(getContext());
        mListView.setCacheColorHint(0x00000000);
        mListView.setDividerHeight(0);
        mListView.setLayoutParams(LayoutUtil.newMatchFrameLayoutParams());
        content.addView(mListView);

        mCommonFrameLayout.setContent(content);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mToolbar.setTitle(Constant.Name.TITLE);
        mToolbar.setBackgroundColor(Constant.Color.TOOLBAR);

        mMoreButton.setOnClickListener(this);

        mPluginListAdapter = new PluginSettingsAdapter(getContext());
        mPluginListAdapter.setItems(getXPlugins());
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mPluginListAdapter);

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_more_vert))
                .into(mMoreButton);
    }

    @Override
    public void onClick(View view) {

        if (mMoreButton == view) {
            // 显示更多菜单
            showMoreMenu();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        XPlugin.Info info = mPluginListAdapter.getItem(position);
        XPlugin xPlugin = getPluginManager().getXPlugin(info);
        xPlugin.openSettings(getActivity());
    }

    private List<XPlugin.Info> getXPlugins() {

        List<XPlugin.Info> infos = new ArrayList<>();
        List<XPlugin> xPlugins = getPluginManager().getXPlugins(Constant.Flag.MAIN);

        for (XPlugin xPlugin : xPlugins) {
            infos.add(xPlugin.getInfo());
        }
        return infos;
    }

    /**
     * 显示更多菜单
     */
    private void showMoreMenu() {

        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), mMoreButton, Gravity.RIGHT);
        Menu menu = popupMenu.getMenu();

        menu.add(1, 1, 1, "关于");

        popupMenu.setOnMenuItemClickListener(item -> {
            handlerMoreMenu(item);
            return true;
        });
        popupMenu.show();
    }

    /**
     * 处理更多菜单事件
     * @param item
     */
    private void handlerMoreMenu(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                // 显示关于
                DialogUtil.showMessage(
                        getContext(), "\n程序版本: v" + BuildConfig.VERSION_NAME);
                break;
        }
    }
}
