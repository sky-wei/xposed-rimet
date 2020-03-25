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

package com.sky.xposed.rimet.presenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.rimet.contract.WifiContract;
import com.sky.xposed.rimet.data.DataException;
import com.sky.xposed.rimet.data.model.WifiModel;
import com.sky.xposed.rimet.task.SimpleTask;
import com.sky.xposed.rimet.util.ExecutorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2020-03-22.
 */
public class WifiPresenter extends AbstractPresenter implements WifiContract.Presenter {

    private static final String KEY = "wifi_list";

    private WifiContract.View mView;
    private XPreferences mPreferences;
    private Gson mGson;

    public WifiPresenter(XCoreManager coreManager, WifiContract.View view) {
        super(coreManager);
        mView = view;
        mPreferences = coreManager.getDefaultPreferences();
        mGson = new Gson();
    }

    @Override
    public void load() {

        SimpleTask<String, List<WifiModel>> task =
                SimpleTask.createTask(this::loadWifi);
        task.setCompleteCallback(models -> mView.onLoad(models));
        task.setThrowableCallback(tr -> mView.onLoadFailed("加载信息失败!"));
        task.executeOnExecutor(ExecutorUtil.getBackExecutor(), KEY);
    }

    @Override
    public void add(String name) {

        SimpleTask<String, WifiModel> task =
                SimpleTask.createTask(this::addWifi);
        task.setBaseView(mView);
        task.setCompleteCallback(model -> mView.onAdd(model));
        task.setThrowableCallback(tr -> {
            if (tr instanceof DataException) {
                mView.onAddFailed(tr.getMessage());
            } else {
                mView.onAddFailed("添加信息失败!");
            }
        });
        task.executeOnExecutor(ExecutorUtil.getBackExecutor(), name);
    }

    @Override
    public void save(List<WifiModel> models) {

        SimpleTask<String, Void> task =
                SimpleTask.createTask(param -> {
                    saveWifi(param, models);
                    return null;
                });
        task.setCompleteCallback(aVoid -> mView.onSaveSucceed());
        task.setThrowableCallback(tr -> mView.onLoadFailed("保存信息失败!"));
        task.executeOnExecutor(ExecutorUtil.getBackExecutor(), KEY);
    }

    private List<WifiModel> loadWifi(String key) {

        String value = mPreferences.getString(key, "");

        if (TextUtils.isEmpty(value)) return new ArrayList<>();

        return mGson.fromJson(value,
                new TypeToken<List<WifiModel>>() {}.getType());
    }

    private void saveWifi(String key, List<WifiModel> models) {

        if (models == null) return;

        // 保存信息
        mPreferences.putString(key, mGson.toJson(models));
    }

    private WifiModel addWifi(String name) throws Exception {

        Thread.sleep(2000); // 给UI一个效果

        WifiManager wifiManager = (WifiManager) getContext()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            throw new DataException("获取Wifi信息失败!");
        }

        WifiModel model = new WifiModel(name);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        model.setEnabled(wifiManager.isWifiEnabled());
        model.setState(wifiManager.getWifiState());

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssId = wifiInfo.getSSID();

        if (ssId.startsWith("\"") && ssId.endsWith("\"")){
            ssId = ssId.substring(1, ssId.length() - 1);
        }

        model.setSsId(ssId);
        model.setBssId(wifiInfo.getBSSID());
        model.setMacAddress(wifiInfo.getMacAddress());

        List<ScanResult> scanResults = wifiManager.getScanResults();

        if (CollectionUtil.isEmpty(scanResults)) {
            throw new DataException("获取附近Wifi列表失败!");
        }

        List<WifiModel.ScanResult> list = new ArrayList<>();
        model.setScanResults(list);

        for (ScanResult result : scanResults) {
            // 添加附近Wifi信息
            list.add(new WifiModel.ScanResult(result.SSID, result.BSSID));
        }
        return model;
    }
}
