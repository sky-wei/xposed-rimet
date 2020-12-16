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

package com.sky.xposed.rimet.plugin.base;

import android.text.TextUtils;

import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.data.M;

/**
 * Created by sky on 2020-03-24.
 */
public abstract class BaseDingPlugin extends AbstractPlugin {

    public BaseDingPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    protected boolean isOpenHook() {
        return TextUtils.equals(getPString(XConstant.Key.PACKAGE_MD5), getXString(M.sky.rimet_package_md5));
    }

    @Override
    public String getXString(int key) {
        final String value = super.getXString(key);
        return TextUtils.isEmpty(value) ? getAString(key) : value;
    }

    /**
     * 获取分析出来的
     * @param key
     * @return
     */
    private String getAString(int key) {
        return getPString(Integer.toHexString(key), "");
    }
}
