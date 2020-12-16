/*
 * Copyright (c) 2019 The sky Authors.
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

package tk.anysoft.xposed.lark.util;

import com.sky.xposed.common.util.Alog;

import java.lang.reflect.Method;

/**
 * Created by sky on 18-3-27.
 */

public class ToStringUtil {

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
            Alog.d((String) mMethodReflectionToString.invoke(null, object));
        } catch (Throwable ignored) {
        }
    }

    public static void toString(String tag, Object object) {

        if (mMethodReflectionToString == null || !Alog.isDebug()) {
            return;
        }

        if (object == null) {
            Alog.d("$tag 打印的对象为空");
            return;
        }

        try {
            Alog.d(tag + " " + mMethodReflectionToString.invoke(null, object));
        } catch (Throwable ignored) {
        }
    }
}
