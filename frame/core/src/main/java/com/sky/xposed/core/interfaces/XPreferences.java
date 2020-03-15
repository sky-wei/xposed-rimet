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

import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by sky on 2020-03-11.
 */
public interface XPreferences extends SharedPreferences, XComponent {

    double getDouble(String key, double defValue);

    String getString(String key);

    boolean getBoolean(String key);

    int getInt(String key);

    float getFloat(String key);

    long getLong(String key);

    double getDouble(String key);

    Set<String> getStringSet(String key, Set<String> defValue);

    XPreferences putString(String key, String value);

    XPreferences putBoolean(String key, boolean value);

    XPreferences putInt(String key, int value);

    XPreferences putFloat(String key, float value);

    XPreferences putLong(String key, long value);

    XPreferences putDouble(String key, double value);

    XPreferences putStringSet(String key, Set<String> value);

    XPreferences remove(String key);

    XPreferences clear();

    /**
     * 获取辅助原始实现SharedPreferences的对象
     * @return
     */
    SharedPreferences getOriginalPreferences();

    SharedPreferences getAssistPreferences();

    /**
     * 刷新名称(为了解决配置跟用户相关的问题)
     * @param name
     * @return
     */
    XPreferences refreshName(String name);

    /**
     * 获取指定名称的配置管理对象
     * @param name
     * @return
     */
    XPreferences getPreferences(String name);

    /**
     * 释放
     */
    void release();


    interface Factory {

        /**
         * 创建指定名称的SharedPreferences
         * @param name
         * @return
         */
        SharedPreferences create(String name);
    }


    /**
     * 数据类型
     */
    interface DataType {

        int UNKNOWN = 0x00;

        int INT = 0x01;

        int LONG = 0x02;

        int FLOAT = 0x03;

        int BOOLEAN = 0x04;

        int STRING = 0x05;

        int STR_SET = 0x06;
    }
}
