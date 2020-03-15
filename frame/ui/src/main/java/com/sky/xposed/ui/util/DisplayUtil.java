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

package com.sky.xposed.ui.util;

import android.content.Context;

/**
 * Created by sky on 2018/8/8.
 */
public class DisplayUtil {

    public static int WIDTH_PIXELS = 0;
    public static int HEIGHT_PIXELS = 0;

    public static int DIP_1 = 0;
    public static int DIP_2 = 0;
    public static int DIP_3 = 0;
    public static int DIP_4 = 0;
    public static int DIP_5 = 0;
    public static int DIP_6 = 0;
    public static int DIP_7 = 0;
    public static int DIP_8 = 0;
    public static int DIP_9 = 0;
    public static int DIP_10 = 0;
    public static int DIP_11 = 0;
    public static int DIP_12 = 0;
    public static int DIP_13 = 0;
    public static int DIP_14 = 0;
    public static int DIP_15 = 0;
    public static int DIP_17 = 0;
    public static int DIP_18 = 0;
    public static int DIP_24 = 0;
    public static int DIP_25 = 0;
    public static int DIP_30 = 0;
    public static int DIP_36 = 0;
    public static int DIP_40 = 0;
    public static int DIP_45 = 0;
    public static int DIP_50 = 0;
    public static int DIP_55 = 0;
    public static int DIP_60 = 0;
    public static int DIP_100 = 0;
    public static int DIP_120 = 0;
    public static int DIP_330 = 0;

    private DisplayUtil() {

    }

    public static void init(Context context) {

        WIDTH_PIXELS = getWidthPixels(context);
        HEIGHT_PIXELS = getHeightPixels(context);

        DIP_1 = dip2px(context, 1);
        DIP_2 = dip2px(context, 2);
        DIP_3 = dip2px(context, 3);
        DIP_4 = dip2px(context, 4);
        DIP_5 = dip2px(context, 5);
        DIP_6 = dip2px(context, 6);
        DIP_7 = dip2px(context, 7);
        DIP_8 = dip2px(context, 8);
        DIP_9 = dip2px(context, 9);
        DIP_10 = dip2px(context, 10);
        DIP_11 = dip2px(context, 11);
        DIP_12 = dip2px(context, 12);
        DIP_13 = dip2px(context, 13);
        DIP_14 = dip2px(context, 14);

        DIP_15 = dip2px(context, 15);
        DIP_17 = dip2px(context, 17);
        DIP_18 = dip2px(context, 18);
        DIP_24 = dip2px(context, 24);
        DIP_25 = dip2px(context, 25);
        DIP_30 = dip2px(context, 30);
        DIP_36 = dip2px(context, 36);
        DIP_40 = dip2px(context, 40);
        DIP_45 = dip2px(context, 45);
        DIP_50 = dip2px(context, 50);
        DIP_55 = dip2px(context, 55);
        DIP_60 = dip2px(context, 60);
        DIP_100 = dip2px(context, 100);
        DIP_120 = dip2px(context, 120);
        DIP_330 = dip2px(context, 330);
    }

    public static int getWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int dip2px(Context context, float dipValue){
        return dip2px(dipValue, getDensity(context));
    }

    public static int px2dip(Context context, float pxValue){
        return px2dip(pxValue, getDensity(context));
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param context
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        return px2sp(pxValue, getScaledDensity(context));
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param context
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        return sp2px(spValue, getScaledDensity(context));
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale（DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale（DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
        return (int) (spValue * fontScale + 0.5f);
    }
}
