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
import android.view.ViewGroup;

/**
 * Created by sky on 2019-06-19.
 */
public interface XFrameLayout {

    /**
     * 添加View到Layout中
     * @param child
     * @return
     */
    void addSubView(View child);

    /**
     * 添加View到Layout中
     * @param child
     * @param index
     * @return
     */
    void addSubView(View child, int index);

    /**
     * 添加View到Layout中
     * @param child
     * @param params
     * @return
     */
    void addSubView(View child, ViewGroup.LayoutParams params);

    /**
     * 添加View到Layout中(是否添加分割线)
     * @param child
     * @param  line
     * @return
     */
    @Deprecated
    void addSubView(View child, boolean line);
}
