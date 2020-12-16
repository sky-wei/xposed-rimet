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

package tk.anysoft.xposed.lark.data.config;

import tk.anysoft.xposed.lark.data.model.ConfigModel;

import java.util.Map;

/**
 * Created by sky on 2019-05-28.
 */
public class CacheRimetConfig extends RimetConfig {

    private ConfigModel mConfigModel;

    public CacheRimetConfig(ConfigModel model) {
        mConfigModel = model;
    }

    @Override
    public RimetConfig loadConfig() {

        // 获取版本配置
        Map<Integer, String> config = mConfigModel.getVersionConfig();

        if (config == null) return this;

        for (int key : config.keySet()) {
            // 添加到配置中
            add(key, config.get(key));
        }

        return this;
    }
}
