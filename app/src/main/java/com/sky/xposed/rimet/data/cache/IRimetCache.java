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

/**
 * Created by sky on 2019-05-27.
 */
public interface IRimetCache {

    /**
     * 保存更新的信息
     * @param model
     */
    void saveUpdateInfo(UpdateModel model);

    /**
     * 保存支持的版本信息
     * @param model
     */
    void saveSupportVersion(VersionModel model);

    /**
     * 保存配置信息
     * @param versionCode
     * @param model
     */
    void saveConfigModel(String versionCode, ConfigModel model);

    /**
     * 获取版本更新信息
     * @return
     */
    UpdateModel getUpdateInfo();

    /**
     * 获取支持的版本信息
     * @return
     */
    VersionModel getSupportVersion();

    /**
     * 获取版本配置信息
     * @param versionCode
     * @return
     */
    ConfigModel getVersionConfig(String versionCode);

    /**
     * 清除配置信息
     */
    void clearVersionConfig();
}
