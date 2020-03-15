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

import java.util.List;

/**
 * Created by sky on 2020-03-11.
 */
public interface XComponentManager {

    /**
     * 获取组件
     * @param name
     * @param <T>
     * @return
     */
    <T extends XComponent> T get(Class<T> name);

    /**
     * 获取所有组件
     * @return
     */
    List<XComponent> getList();

    /**
     * 移除组件
     * @param name
     */
    void remove(Class<? extends XComponent> name);

    /**
     * 是否包含组件
     * @param name
     * @return
     */
    boolean contains(Class<? extends XComponent> name);

    /**
     * 释放所有
     */
    void release();

    /**
     * 添加监听
     * @param listener
     */
    void addListener(ComponentListener listener);

    /**
     * 移除监听
     * @param listener
     */
    void removeListener(ComponentListener listener);


    interface Factory {

        <T extends XComponent> T create(XCoreManager coreManager, Class<T> name);
    }

    interface ComponentListener {

        void onCreate(Class<? extends XComponent> name);

        void onRelease(Class<? extends XComponent> name);
    }
}
