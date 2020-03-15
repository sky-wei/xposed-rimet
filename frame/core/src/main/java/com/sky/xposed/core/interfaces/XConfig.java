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

/**
 * Created by sky on 2020-03-11.
 */
public interface XConfig {

    /**
     * 重新加载
     * @return
     */
    XConfig reload();

    /**
     * 获取相应key的值
     * @param key
     * @return
     */
    String get(int key);

    /**
     * 获取相应key的值
     * @param key
     * @return
     */
    boolean getBoolean(int key);

    /**
     * 获取相应key的值
     * @param key
     * @return
     */
    int getInt(int key);

    /**
     * 判断相应key的值是否存在
     * @param key
     * @return
     */
    boolean has(int key);
}
