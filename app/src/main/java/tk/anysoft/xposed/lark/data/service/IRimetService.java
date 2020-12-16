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

package tk.anysoft.xposed.lark.data.service;

import tk.anysoft.xposed.lark.data.model.ConfigModel;
import tk.anysoft.xposed.lark.data.model.UpdateModel;
import tk.anysoft.xposed.lark.data.model.VersionModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sky on 2019-05-27.
 */
public interface IRimetService {


    /**
     * 检测版本更新
     * @return
     */
    @GET("res/update.json")
    Observable<UpdateModel> checkUpdate();


    /**
     * 获取支持的版本
     * @return
     */
    @GET("res/version.json")
    Observable<VersionModel> getSupportVersion();


    /**
     * 获取相应的版本版本
     * @param versionCode
     * @return
     */
    @GET("res/config/{versionCode}.json")
    Observable<ConfigModel> getVersionConfig(@Path("versionCode") String versionCode);
}
