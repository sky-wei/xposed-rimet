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

package com.sky.xposed.common.util;


import java.lang.reflect.Method;

/**
 * Created by sky on 2018/9/24.
 */
public class ToStringUtil {

    private static final String TAG = "Xposed";

    private static Method mMethodReflectionToString;

    static {
        try {
            Class<?> classToStringBuilder = Class
                    .forName("org.apache.commons.lang3.builder.ToStringBuilder");
            mMethodReflectionToString = classToStringBuilder
                    .getDeclaredMethod("reflectionToString", Object.class);
        } catch (Throwable ignored) {
        }
    }

    public static void toString(Object object) {

        if (mMethodReflectionToString == null || !Alog.isDebug()) {
            return;
        }

        if (object == null) {
            Alog.d("打印的对象为空");
            return;
        }

        try {
            Alog.d(TAG, (String) mMethodReflectionToString.invoke(null, object));
        } catch (Throwable ignored) {
        }
    }

    public static void toString(String prefix, Object object) {

        if (mMethodReflectionToString == null || !Alog.isDebug()) {
            return;
        }

        if (object == null) {
            Alog.d("打印的对象为空");
            return;
        }

        try {
            Alog.d(TAG, prefix + " " + mMethodReflectionToString.invoke(null, object));
        } catch (Throwable ignored) {
        }
    }

    public static void toString(String tag, String prefix, Object object) {

        if (mMethodReflectionToString == null || !Alog.isDebug()) {
            return;
        }

        if (object == null) {
            Alog.d(tag, "打印的对象为空");
            return;
        }

        try {
            Alog.d(tag, prefix + " " + mMethodReflectionToString.invoke(null, object));
        } catch (Throwable ignored) {
        }
    }
}
