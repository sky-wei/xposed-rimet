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

import android.annotation.SuppressLint;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XVersionManager;
import com.sky.xposed.rimet.BuildConfig;
import com.sky.xposed.rimet.contract.RimetContract;
import com.sky.xposed.rimet.data.model.UpdateModel;
import com.sky.xposed.rimet.data.model.VersionModel;
import com.sky.xposed.rimet.data.source.IRimetSource;

import java.util.Map;

import io.reactivex.Completable;

/**
 * Created by sky on 2019-05-28.
 */
public class RimetPresenter extends AbstractPresenter implements RimetContract.Presenter {

    private IRimetSource mRimetSource;
    private RimetContract.View mView;
    private XVersionManager mVersionManager;

    public RimetPresenter(XCoreManager coreManager, RimetContract.View view) {
        super(coreManager);
        mView = view;
        mVersionManager = getCoreManager().getVersionManager();
//        mRimetSource = getRepositoryFactory().createRimetSource();
    }

    @SuppressLint("CheckResult")
    @Override
    public void checkUpdate(boolean auto) {

        // 检测更新
        ioToMain(mRimetSource.checkUpdate())
                .subscribe(model -> {
                    // 检测更新成功
                    checkUpdate(auto, model);
                }, throwable -> {
                    Alog.e("检测异常", throwable);
                    if (!auto) mView.onUpdateFailed("检测更新失败,请稍后再试!");
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateConfig(boolean auto) {

        // 获取配置信息
        XConfig xConfig = mVersionManager.getSupportConfig();

        if (xConfig != null) {
            if (!auto) mView.onUpdateConfigFailed("当前配置不需要更新!");
            return;
        }

        // 清除版本信息
        mVersionManager.clearVersionConfig();

        // 获取获取配置
        ioToMain(mRimetSource.getSupportVersion())
                .subscribe(model -> {
                    // 更新配置
                    updateConfig(auto, model);
                }, throwable -> {
                    Alog.e("更新异常", throwable);
                    if (!auto) mView.onUpdateConfigFailed("更新配置失败!");
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void clearConfig() {

        ioToMain(Completable.create(emitter -> {
            try {
                // 开始处理
                mVersionManager.clearVersionConfig();
                emitter.onComplete();
            } catch (Throwable tr) {
                emitter.onError(tr);
            }
        })).subscribe(() -> {
            // 清除配置成功
            mView.onClearConfigSucceed();
        }, throwable -> {
            Alog.e("清除异常", throwable);
            mView.onClearConfigFailed("清除配置失败!");
        });

        try {

        } catch (Throwable tr) {

        }
    }

    private void checkUpdate(boolean auto, UpdateModel model) {

        if (BuildConfig.VERSION_CODE >= model.getVersionCode()) {
            // 不需要更新
            if (!auto) mView.onUpdateFailed("当前版本是最新的,不需要更新!");
            return ;
        }

        // 需要用户更新
        mView.onUpdate(model);
    }

    @SuppressLint("CheckResult")
    private void updateConfig(boolean auto, VersionModel model) {

        final Map<String, String> version = model.getSupportConfig();

        // 获取钉钉的版本信息
        final String versionName = mVersionManager.getVersionName();

        if (version == null
                || !version.containsKey(versionName)) {
            // 不支持当前版本
            if (!auto) mView.onUpdateConfigFailed("暂不支持当前版本!");
            return;
        }

        // 获取相应的版本配置
        ioToMain(mRimetSource.getVersionConfig(version.get(versionName)))
                .subscribe(model1 -> {
                    // 更新配置成功
                    mView.onUpdateConfigSucceed();
                }, throwable -> {
                    Alog.e("更新异常", throwable);
                    if (!auto) mView.onUpdateConfigFailed("更新配置失败!");
                });
    }
}
