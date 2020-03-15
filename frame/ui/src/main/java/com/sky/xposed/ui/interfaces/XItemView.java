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

/**
 * Created by sky on 2019-06-19.
 *
 * 所有的ItemView都需要实现这个接口,方便在Dialog使用
 */
public interface XItemView {

    /**
     * 设置控件是否显示(只有VISIBLE与GONE)
     * @param show true:显示,false:不显示
     */
    void setVisibility(boolean show);

    /**
     * 把View添加指定Layout中
     * @param frame
     * @return
     */
    XItemView addToFrame(XFrameLayout frame);

    /**
     * 把View添加指定Layout中并添加分割线
     * @param frame
     * @param line
     * @return
     */
    XItemView addToFrame(XFrameLayout frame, boolean line);
}
