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

package com.sky.xposed.rimet.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.contract.StationContract;
import com.sky.xposed.rimet.data.model.StationModel;
import com.sky.xposed.rimet.presenter.StationPresenter;
import com.sky.xposed.rimet.ui.adapter.StationAdapter;
import com.sky.xposed.rimet.ui.util.DialogUtil;
import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.PermissionUtil;
import com.sky.xposed.ui.util.ViewUtil;
import com.sky.xposed.ui.view.PluginFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2020-03-22.
 */
public class StationDialog extends BasePluginDialog implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, StationContract.View {

    private ListView mListView;
    private StationAdapter mAdapter;
    private StationContract.Presenter mPresenter;

    private List<StationModel> mStationModels = new ArrayList<>();

    @Override
    public void createView(PluginFrameLayout frameView) {

        LinearLayout.LayoutParams params = LayoutUtil.newWrapLinearLayoutParams();
        params.leftMargin = DisplayUtil.dip2px(getContext(), 15);
        params.topMargin = DisplayUtil.dip2px(getContext(), 10);
        params.bottomMargin = DisplayUtil.dip2px(getContext(), 5);

        TextView tvTip = new TextView(getContext());
        tvTip.setLayoutParams(params);
        tvTip.setText("点击选择基站,长按可删除位置！");
        tvTip.setTextSize(12);

        frameView.addSubView(tvTip);

        mListView = ViewUtil.newListView(getContext());

        frameView.addSubView(mListView);
    }

    @Override
    protected PluginFrameLayout onCreatePluginFrame() {
        return createLinePluginFrame();
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        getTitleView().setElevation(DisplayUtil.DIP_4);

        showBack();
        setTitle("基站列表");
        showMoreMenu();

        mAdapter = new StationAdapter(getContext());

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setAdapter(mAdapter);

        mPresenter = new StationPresenter(getCoreManager(), this);
        mPresenter.load();
    }

    @Override
    public void onCreateMoreMenu(Menu menu) {
        super.onCreateMoreMenu(menu);

        menu.add(0, 1, 0, "添加");
        menu.add(0, 2, 0, "清空");
    }

    @Override
    public boolean onMoreItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (1 == itemId) {

            if (mStationModels.size() > 10) {
                showMessage("最多添加10条信息!");
                return true;
            }

            if (!checkPermission()) {
                return true;
            }

            String alias = getDefaultPreferences().getString(XConstant.Key.LAST_ALIAS);
            DialogUtil.showEditDialog(getContext(),
                    "提示", alias, "请输入保存的别名", (view, value) -> {

                if (TextUtils.isEmpty(value)) {
                    showMessage("输入的信息不能为空!");
                    return;
                }

                // 保存名称
                getDefaultPreferences().putString(XConstant.Key.LAST_ALIAS, value);

                // 添加
                mPresenter.add(value);
            });
            return true;
        } else if (2 == itemId) {
            // 清空
            DialogUtil.showDialog(getContext(),
                    "提示", "\n是否清空列表所有信息!", (dialog, which) -> {

                if (mStationModels != null) {
                    mStationModels.clear();
                    mPresenter.save(mStationModels);
                }
            });
            return true;
        }
        return super.onMoreItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        StationModel model = mStationModels.get(position);

        Bundle data = new Bundle();
        data.putSerializable(XConstant.Key.DATA, model);

        setResult(Activity.RESULT_OK, data);
        dismiss();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        DialogUtil.showDialog(getContext(),
                "提示", "\n是否删除该基站信息?", (dialog, which) -> {
            // 删除信息并保存
            mStationModels.remove(position);
            mPresenter.save(mStationModels);
        });
        return true;
    }

    @Override
    public void onLoad(List<StationModel> models) {

        mStationModels.clear();

        if (CollectionUtil.isNotEmpty(models)) {
            mStationModels.addAll(models);
        }

        mAdapter.setItems(mStationModels);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void onAdd(StationModel model) {

        mStationModels.add(model);
        mPresenter.save(mStationModels);

        mAdapter.setItems(mStationModels);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void onSaveSucceed() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveFailed(String msg) {
        showMessage(msg);
    }

    private boolean checkPermission() {

        if (PermissionUtil.checkPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && PermissionUtil.checkPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            PermissionUtil.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION },
                    99);
            return false;
        }
        return true;
    }
}
