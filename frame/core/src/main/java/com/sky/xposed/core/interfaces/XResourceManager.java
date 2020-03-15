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

package com.sky.xposed.core.interfaces;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

/**
 * Created by sky on 2020-03-11.
 */
public interface XResourceManager extends XComponent {

    /**
     * 获取相应Hook的Context
     * @return
     */
    Context getHookContext();

    /**
     * 获取插件的Context
     * @return
     */
    Context getPluginContext();

    /**
     * 获取相应Hook的资源对象
     * @return
     */
    Resources getHookResources();

    /**
     * 获取插件的资源对象
     * @return
     */
    Resources getPluginResources();

    /**
     * 获取插件资源的Uri
     * @param resId
     * @return
     */
    Uri getPluginResource(int resId);

    /**
     * 获取插件的字符串信息
     * @param resId
     * @return
     */
    String getString(int resId);

    /**
     * 获取插件的字符串信息
     * @param resId
     * @param formatArgs
     * @return
     */
    String getString(int resId, Object... formatArgs);

    /**
     * 获取颜色值
     * @param id
     * @return
     */
    int getColor(int id);
}
