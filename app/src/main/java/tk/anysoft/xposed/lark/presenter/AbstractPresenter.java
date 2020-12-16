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

package tk.anysoft.xposed.lark.presenter;

import android.content.Context;

import tk.anysoft.xposed.lark.base.BasePresenter;
import tk.anysoft.xposed.lark.data.source.IRepositoryFactory;
import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sky on 2019-05-28.
 */
public abstract class AbstractPresenter implements BasePresenter {

    private XPluginManager mXPluginManager;

    public AbstractPresenter(XPluginManager xPluginManager) {
        mXPluginManager = xPluginManager;
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
        return mXPluginManager.getContext();
    }

    public final String getString(int resId) {
        return getContext().getString(resId);
    }

    public final String getString(int resId, Object... formatArg) {
        return getContext().getString(resId, formatArg);
    }

    public final XPluginManager getXPluginManager() {
        return mXPluginManager;
    }

    public final IRepositoryFactory getRepositoryFactory() {
        return mXPluginManager.getRepositoryFactory();
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
