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

/**
 * Created by sky on 2019-05-27.
 */
public interface ICacheManager {

    /**
     * 生成Key
     * @param value
     * @return
     */
    String buildKey(String value);

    /**
     * 获取相应Key的信息
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> tClass);

    /**
     * 保存相应的信息
     * @param key
     * @param value
     * @param <T>
     */
    <T> void put(String key, T value);

    /**
     * 移除信息
     * @param key
     */
    void remove(String key);

    /**
     * 清除所有
     */
    void clear();

    /**
     * 关闭
     */
    void close();
}
