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

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by sky on 2018/12/25.
 */
public class SoftKeyboardUtil {

    private SoftKeyboardUtil() {

    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();

        if (view != null) {
            getInputMethodManager(activity).hideSoftInputFromWindow(
                    view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public static void hideSoftKeyboard(Context context, View... views) {

        if (views == null) return;

        InputMethodManager inputMethodManager = getInputMethodManager(context);

        for (View v : views) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean showSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager = getInputMethodManager(context);
        return inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isActive(Context context) {
        return getInputMethodManager(context).isActive();
    }

    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
