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

package com.sky.xposed.rimet.data.source.remote;

import com.sky.xposed.rimet.data.cache.IRimetCache;
import com.sky.xposed.rimet.data.model.ConfigModel;
import com.sky.xposed.rimet.data.model.UpdateModel;
import com.sky.xposed.rimet.data.model.VersionModel;
import com.sky.xposed.rimet.data.service.IRimetService;
import com.sky.xposed.rimet.data.service.IServiceFactory;
import com.sky.xposed.rimet.data.source.IRimetSource;

import io.reactivex.Observable;

/**
 * Created by sky on 2019-05-27.
 */
public class RimetRemoteSource implements IRimetSource {

    private IServiceFactory mServiceFactory;
    private IRimetCache mRimetCache;

    public RimetRemoteSource(IServiceFactory iServiceFactory, IRimetCache iRimetCache) {
        mServiceFactory = iServiceFactory;
        mRimetCache = iRimetCache;
    }

    @Override
    public Observable<UpdateModel> checkUpdate() {
        return mServiceFactory
                .createService(IRimetService.class)
                .checkUpdate()
                .doOnNext(model -> mRimetCache.saveUpdateInfo(model));
    }

    @Override
    public Observable<VersionModel> getSupportVersion() {
        return mServiceFactory
                .createService(IRimetService.class)
                .getSupportVersion()
                .doOnNext(model -> mRimetCache.saveSupportVersion(model));
    }

    @Override
    public Observable<ConfigModel> getVersionConfig(String versionCode) {
        return mServiceFactory
                .createService(IRimetService.class)
                .getVersionConfig(versionCode)
                .doOnNext(model -> mRimetCache.saveConfigModel(versionCode, model));
    }
}
