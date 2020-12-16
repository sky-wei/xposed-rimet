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

package tk.anysoft.xposed.lark.plugin.interfaces;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by sky on 2019/3/12.
 */
public interface XResourceManager {

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
     * 获取主题
     * @return
     */
    Theme getTheme();


    /**
     * 主题资源接口
     */
    interface Theme {

        /**
         * 获取颜色值
         * @param id
         * @return
         */
        int getColor(int id);

        /**
         * 获取图片值
         * @param id
         * @return
         */
        int getImage(int id);
    }
}
