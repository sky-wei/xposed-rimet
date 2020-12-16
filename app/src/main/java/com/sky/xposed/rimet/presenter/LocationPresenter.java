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

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.rimet.contract.LocationContract;
import com.sky.xposed.rimet.data.model.LocationModel;
import com.sky.xposed.rimet.task.SimpleTask;
import com.sky.xposed.rimet.util.ExecutorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2020-03-22.
 */
public class LocationPresenter extends AbstractPresenter implements LocationContract.Presenter {

    private static final String KEY = "location_list";

    private LocationContract.View mView;
    private XPreferences mPreferences;
    private Gson mGson;

    public LocationPresenter(XCoreManager coreManager, LocationContract.View view) {
        super(coreManager);
        mView = view;
        mPreferences = coreManager.getDefaultPreferences();
        mGson = new Gson();
    }

    @Override
    public void load() {

        SimpleTask<String, List<LocationModel>> task =
                SimpleTask.createTask(this::loadLocation);
        task.setCompleteCallback(models -> mView.onLoad(models));
        task.setThrowableCallback(tr -> mView.onLoadFailed("加载信息失败!"));
        task.executeOnExecutor(ExecutorUtil.getBackExecutor(), KEY);
    }

    @Override
    public void save(List<LocationModel> models) {

        SimpleTask<String, Void> task =
                SimpleTask.createTask(param -> {
                    saveLocation(param, models);
                    return null;
                });
        task.setCompleteCallback(aVoid -> mView.onSaveSucceed());
        task.setThrowableCallback(tr -> mView.onLoadFailed("保存信息失败!"));
        task.executeOnExecutor(ExecutorUtil.getBackExecutor(), KEY);
    }

    private List<LocationModel> loadLocation(String key) {

        String value = mPreferences.getString(key, "");

        if (TextUtils.isEmpty(value)) return new ArrayList<>();

        return mGson.fromJson(value,
                new TypeToken<List<LocationModel>>() {}.getType());
    }

    private void saveLocation(String key, List<LocationModel> models) {

        if (models == null) return;

        // 保存信息
        mPreferences.putString(key, mGson.toJson(models));
    }
}
