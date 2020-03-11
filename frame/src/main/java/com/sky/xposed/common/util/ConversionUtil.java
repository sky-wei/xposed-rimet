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

import android.text.TextUtils;

/**
 * Created by sky on 2018/8/8.
 */
public class ConversionUtil {

    private static final String TAG = "ConversionUtils";

    private ConversionUtil() {

    }

    public static int parseInt(String value) {
        return parseInt(value, 0);
    }

    public static int parseInt(String value, int defaultValue) {

        int result = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                Alog.e(TAG, "NumberFormatException", e);
            }
        }
        return result;
    }

    public static long parseLong(String value) {
        return parseLong(value, 0L);
    }

    public static long parseLong(String value, long defaultValue) {

        long result = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            try {
                result = Long.parseLong(value);
            } catch (NumberFormatException e) {
                Alog.e(TAG, "NumberFormatException", e);
            }
        }
        return result;
    }

    public static float parseFloat(String value) {
        return parseFloat(value, 0.0F);
    }

    public static float parseFloat(String value, float defaultValue) {

        float result = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            try {
                result = Float.parseFloat(value);
            } catch (Throwable e) {
                Alog.e(TAG, "NumberFormatException", e);
            }
        }
        return result;
    }

    public static double parseDouble(String value, double defaultValue) {

        double result = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            try {
                result = Double.parseDouble(value);
            } catch (Throwable e) {
                Alog.e(TAG, "NumberFormatException", e);
            }
        }
        return result;
    }

    public static boolean parseBoolean(String value, boolean defaultValue) {

        boolean result = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            result = Boolean.parseBoolean(value);
        }
        return result;
    }

    public static boolean parseBoolean(String value) {
        return parseBoolean(value, false);
    }

    public static boolean booleanValue(Boolean value, boolean defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean booleanValue(Boolean value) {
        return booleanValue(value, false);
    }

    public static int intValue(Integer value) {
        return intValue(value, 0);
    }

    public static int intValue(Number value, int defaultValue) {
        return value != null ? value.intValue() : defaultValue;
    }

    public static int intValue(Number value) {
        return intValue(value, 0);
    }

    public static long longValue(Long value, long defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static long longValue(Long value) {
        return longValue(value, 0L);
    }

    public static float floatValue(Float value, float defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static float floatValue(Float value) {
        return floatValue(value, 0.0F);
    }
}
