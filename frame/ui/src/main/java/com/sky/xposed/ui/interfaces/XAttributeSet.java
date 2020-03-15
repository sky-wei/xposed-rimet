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

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by sky on 2020-02-17.
 */
public interface XAttributeSet {

    /**
     * 获取Int值
     * @param key
     * @param defaultValue
     * @return
     */
    int getInt(int key, int defaultValue);

    /**
     * 获取颜色值
     * @param key
     * @param defaultValue
     * @return
     */
    int getColor(int key, int defaultValue);

    /**
     * 获取图片值
     * @param key
     * @param defaultValue
     * @return
     */
    int getImage(int key, int defaultValue);

    /**
     * 获取ColorDrawable
     * @param id
     * @param defaultValue
     * @return
     */
    ColorDrawable getColorDrawable(int id, ColorDrawable defaultValue);

    /**
     * 获取Drawable
     * @param id
     * @param defaultValue
     * @return
     */
    Drawable getDrawable(int id, Drawable defaultValue);
}
