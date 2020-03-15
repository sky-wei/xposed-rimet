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

package com.sky.xposed.ui.interfaces;

import android.view.View;

import com.sky.xposed.core.interfaces.XPreferences;

/**
 * Created by sky on 2018/8/8.
 *
 * 一个跟踪状态的接口类
 */
public interface XTrackStatus<T> {

    /**
     * 设置属性管理类
     * @param preferences
     * @return
     */
    XTrackStatus<T> setPreferences(XPreferences preferences);

    /**
     * 绑定相应的Key(通过绑定相应的Key来跟踪相关值的变化)
     * @param key
     * @param defValue
     * @param listener
     * @return
     */
    XTrackStatus<T> bind(String key, T defValue, BindListener<T> listener);

    /**
     * 跟踪状态的变化
     * @param listener
     * @return
     */
    XTrackStatus<T> track(StatusListener<T> listener);

    /**
     * 获取Key对应的Value
     * @return
     */
    T getKeyValue();

    /**
     * 获取绑定的Key
     * @return
     */
    String getKey();


    /**
     * 状态监听接口
     * @param <T>
     */
    interface StatusListener<T> {

        boolean onStatusChange(View view, String key, T value);
    }

    /**
     * 绑定监听接口
     * @param <T>
     */
    interface BindListener<T> {

        void onStatusBind(String key, T value);
    }
}
