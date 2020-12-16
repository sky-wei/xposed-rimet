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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.rimet.BuildConfig;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.contract.LocationContract;
import com.sky.xposed.rimet.data.model.LocationModel;
import com.sky.xposed.rimet.presenter.LocationPresenter;
import com.sky.xposed.rimet.ui.activity.MapActivity;
import com.sky.xposed.rimet.ui.adapter.LocationAdapter;
import com.sky.xposed.rimet.ui.util.DialogUtil;
import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.LayoutUtil;
import com.sky.xposed.ui.util.ViewUtil;
import com.sky.xposed.ui.view.PluginFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2020-03-22.
 */
public class LocationDialog extends BasePluginDialog implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, LocationContract.View {

    private ListView mListView;
    private LocationAdapter mAdapter;
    private LocationContract.Presenter mPresenter;

    private List<LocationModel> mLocationModels = new ArrayList<>();

    @Override
    public void createView(PluginFrameLayout frameView) {

        LinearLayout.LayoutParams params = LayoutUtil.newWrapLinearLayoutParams();
        params.leftMargin = DisplayUtil.dip2px(getContext(), 15);
        params.topMargin = DisplayUtil.dip2px(getContext(), 10);
        params.bottomMargin = DisplayUtil.dip2px(getContext(), 5);

        TextView tvTip = new TextView(getContext());
        tvTip.setLayoutParams(params);
        tvTip.setText("点击选择位置,长按可删除位置！");
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
        setTitle("位置列表");
        showMoreMenu();

        mAdapter = new LocationAdapter(getContext());

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setAdapter(mAdapter);

        mPresenter = new LocationPresenter(getCoreManager(), this);
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

            if (mLocationModels.size() > 10) {
                showMessage("最多添加10条信息!");
                return true;
            }

            // 跳转到地图界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(BuildConfig.APPLICATION_ID, MapActivity.class.getName());
            startActivityForResult(intent, 99);
            return true;
        } else if (2 == itemId) {
            // 清空
            DialogUtil.showDialog(getContext(),
                    "提示", "\n是否清空列表所有信息!", (dialog, which) -> {

                if (mLocationModels != null) {
                    mLocationModels.clear();
                    mPresenter.save(mLocationModels);
                }
            });
            return true;
        }
        return super.onMoreItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        LocationModel model = mLocationModels.get(position);

        Bundle data = new Bundle();
        data.putSerializable(XConstant.Key.DATA, model);

        setResult(Activity.RESULT_OK, data);
        dismiss();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        DialogUtil.showDialog(getContext(),
                "提示", "\n是否删除该位置信息?", (dialog, which) -> {
            // 删除信息并保存
            mLocationModels.remove(position);
            mPresenter.save(mLocationModels);
        });
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            // 添加信息
            LocationModel model = new LocationModel();

            model.setAddress(data.getStringExtra("address"));
            model.setLatitude(data.getDoubleExtra("latitude", 0.0D));
            model.setLongitude(data.getDoubleExtra("longitude", 0.0D));

            mLocationModels.add(model);
            mPresenter.save(mLocationModels);
        }
    }

    @Override
    public void onLoad(List<LocationModel> models) {

        mLocationModels.clear();

        if (CollectionUtil.isNotEmpty(models)) {
            mLocationModels.addAll(models);
        }

        mAdapter.setItems(mLocationModels);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(String msg) {
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
}
