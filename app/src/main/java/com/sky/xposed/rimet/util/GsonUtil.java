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

package com.sky.xposed.rimet.util;

import com.google.gson.Gson;
import com.sky.xposed.common.util.Alog;

import java.lang.reflect.Type;

/**
 * Created by sky on 2020/3/25.
 */
public final class GsonUtil {

    private GsonUtil() {

    }

    public static String toJson(Object value) {

        if (null == value) return "";

        try {
            return new Gson().toJson(value);
        } catch (Exception e) {
            Alog.e("toJson异常", e);
        }
        return "";
    }

    public static <T> T fromJson(String value, Type type) {

        try {
            return new Gson().fromJson(value, type);
        } catch (Exception e) {
            Alog.e("fromJson异常", e);
        }
        return null;
    }
}
