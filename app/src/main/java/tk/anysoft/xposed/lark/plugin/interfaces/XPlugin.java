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

import android.app.Activity;

/**
 * Created by sky on 2018/12/21.
 */
public interface XPlugin {

    /**
     * 获取插件信息
     * @return
     */
    Info getInfo();


    /**
     * 是否需要处理
     * @return
     */
    boolean isHandler();

    /**
     * 初始化
     */
    void initialization();

    /**
     * 处理加载的包
     */
    void onHandleLoadPackage();

    /**
     * 释放
     */
    void release();

    /**
     * 打开设置
     */
    void openSettings(Activity activity);

    /**
     * 是否启用
     * @param flag
     * @param defValue
     */
    boolean isEnable(int flag, boolean defValue);

    /**
     * 设置是否启用
     * @param flag
     * @param enable
     */
    void setEnable(int flag, boolean enable);

    /**
     * 插件信息
     */
    interface Info {

        int getId();

        String getName();

        String getDesc();
    }
}
