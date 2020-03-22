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

package com.sky.xposed.rimet.data.cache;

import com.sky.xposed.rimet.data.model.ConfigModel;
import com.sky.xposed.rimet.data.model.UpdateModel;
import com.sky.xposed.rimet.data.model.VersionModel;

import java.util.Map;

/**
 * Created by sky on 2019-05-27.
 */
public class RimetCache implements IRimetCache {

    private static final long TIMEOUT = 1000 * 60 * 60;

    private ICacheManager mCacheManager;

    public RimetCache(ICacheManager iCacheManager) {
        mCacheManager = iCacheManager;
    }

    @Override
    public void saveUpdateInfo(UpdateModel model) {
        mCacheManager.put(buildKey(UpdateModel.class.getSimpleName()), model);
    }

    @Override
    public void saveSupportVersion(VersionModel model) {
        mCacheManager.put(buildKey(VersionModel.class.getSimpleName()), model);
    }

    @Override
    public void saveConfigModel(String versionCode, ConfigModel model) {
        mCacheManager.put(buildKey(versionCode), model);
    }

    @Override
    public UpdateModel getUpdateInfo() {

//        UpdateModel model = mCacheManager.get(buildKey(UpdateModel.class.getSimpleName()), UpdateModel.class);
//
//        return model != null && !isExpired(lastTime) ? model : null;
        return null;
    }

    @Override
    public VersionModel getSupportVersion() {
        return mCacheManager.get(buildKey(VersionModel.class.getSimpleName()), VersionModel.class);
    }

    @Override
    public ConfigModel getVersionConfig(String versionCode) {
        return mCacheManager.get(buildKey(versionCode), ConfigModel.class);
    }

    @Override
    public void clearVersionConfig() {

        VersionModel model = getSupportVersion();

        if (model == null || model.getSupportConfig() == null) {
            // 已经清除,不需要再处理
            return ;
        }

        Map<String, String> version = model.getSupportConfig();

        for (String versionName : version.keySet()) {
            // 删除所有版本配置
            mCacheManager.remove(buildKey(version.get(versionName)));
        }
        mCacheManager.remove(buildKey(VersionModel.class.getSimpleName()));
    }

    private String buildKey(String value) {
        return mCacheManager.buildKey(value);
    }

    private boolean isExpired(long lastTime) {

        long curTime = System.currentTimeMillis();

        // 当前时间-最后时间>=超时时间 || 异常情况: 当前时间 < 最后时间
        return curTime - lastTime >= RimetCache.TIMEOUT || curTime < lastTime;
    }
}
