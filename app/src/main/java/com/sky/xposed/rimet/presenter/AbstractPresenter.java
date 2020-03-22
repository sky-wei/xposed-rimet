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

import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.ui.base.BasePresenter;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sky on 2019-05-28.
 */
public abstract class AbstractPresenter implements BasePresenter {

    private XCoreManager mCoreManager;

    public AbstractPresenter(XCoreManager coreManager) {
        mCoreManager = coreManager;
    }

    @Override
    public void restart() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public final Context getContext() {
        return mCoreManager.getLoadPackage().getContext();
    }

    public final XCoreManager getCoreManager() {
        return mCoreManager;
    }

    public final String getString(int resId) {
        return getContext().getString(resId);
    }

    public final String getString(int resId, Object... formatArg) {
        return getContext().getString(resId, formatArg);
    }

    public <T> Observable<T> ioToMain(Observable<T> observable) {

        if (observable == null) return null;

        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable ioToMain(Completable completable) {

        if (completable == null) return null;

        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Single<T> ioToMain(Single<T> completable) {

        if (completable == null) return null;

        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
