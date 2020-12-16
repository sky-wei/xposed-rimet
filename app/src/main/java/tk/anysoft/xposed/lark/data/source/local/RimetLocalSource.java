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

package tk.anysoft.xposed.lark.data.source.local;

import tk.anysoft.xposed.lark.data.cache.IRimetCache;
import tk.anysoft.xposed.lark.data.model.ConfigModel;
import tk.anysoft.xposed.lark.data.model.UpdateModel;
import tk.anysoft.xposed.lark.data.model.VersionModel;
import tk.anysoft.xposed.lark.data.source.IRimetSource;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * Created by sky on 2019-05-27.
 */
public class RimetLocalSource implements IRimetSource {

    private IRimetCache mRimetCache;

    public RimetLocalSource(IRimetCache iRimetCache) {
        mRimetCache = iRimetCache;
    }

    @Override
    public Observable<UpdateModel> checkUpdate() {
        return Observable.create(emitter -> subscribe(emitter, () -> mRimetCache.getUpdateInfo()));
    }

    @Override
    public Observable<VersionModel> getSupportVersion() {
        return Observable.create(emitter -> subscribe(emitter, () -> mRimetCache.getSupportVersion()));
    }

    @Override
    public Observable<ConfigModel> getVersionConfig(String versionCode) {
        return Observable.create(emitter -> subscribe(emitter, () -> mRimetCache.getVersionConfig(versionCode)));
    }

    private <T> void subscribe(ObservableEmitter<T> emitter, OnHandler<T> handler) {

        try {
            // 开始处理
            T value = handler.onHandler();

            if (value != null) {
                // 返回结果
                emitter.onNext(value);
            }
            emitter.onComplete();
        } catch (Throwable tr) {
            emitter.onError(tr);
        }
    }

    private interface OnHandler<T> {

        T onHandler();
    }
}
