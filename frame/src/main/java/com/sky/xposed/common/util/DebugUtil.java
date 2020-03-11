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

import android.util.Log;


/**
 * Created by sky on 2019/1/25.
 */
public final class DebugUtil {

    private DebugUtil() {
    }

    /**
     * 输出异常的信息
     */
    public static void printStackTrace() {

        if (!Alog.isDebug()) return;

        Alog.e(Log.getStackTraceString(new NullPointerException("test")));
    }

    public static void printStackTrace2() {

        if (!Alog.isDebug()) return;

        Exception exception = new Exception("NullPointerException");

        Alog.e(exception.getMessage());

        StackTraceElement[] elements = exception.getStackTrace();

        for (StackTraceElement element : elements) {
           Alog.e(element.toString());
        }
    }
}
