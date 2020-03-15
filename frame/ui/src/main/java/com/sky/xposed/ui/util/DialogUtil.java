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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.sky.xposed.ui.adapter.DisplayListAdapter;
import com.sky.xposed.ui.interfaces.XDisplayItem;
import com.sky.xposed.ui.view.XEditItemView;

import java.util.List;

/**
 * Created by sky on 2019-06-21.
 */
public final class DialogUtil {

    private DialogUtil() {

    }

    /**
     * 显示一个输入的Dialog提示框
     * @param context
     * @param title
     * @param hint
     * @param inputType
     * @param listener
     */
    public static void showInputDialog(
            Context context, String title, String hint, int inputType, XEditItemView.OnEditChangeListener listener) {

        int top = DisplayUtil.DIP_14;
        int left = DisplayUtil.DIP_24;

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(LayoutUtil.newFrameLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setPadding(left, top, left, 0);

        int padding = DisplayUtil.DIP_4;

        EditText editText = new EditText(context);
        editText.setPadding(padding >> 1, padding, padding >> 1, padding);
        editText.setText(listener != null ? listener.getDefaultText() : null);
        editText.setTextSize(14);
        editText.setHint(hint);
        editText.setLayoutParams(LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewUtil.setInputType(editText, inputType);
        frameLayout.addView(editText);

        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(frameLayout)
                .setPositiveButton("确定", (dialog1, which) -> {
                    if (listener != null) listener.onEditChanged(editText, editText.getText().toString());
                })
                .setNegativeButton("取消", null)
                .create();

        dialog.setOnShowListener(dialog1 -> SoftKeyboardUtil.showSoftInput(context, editText));
        dialog.setOnDismissListener(dialog1 -> SoftKeyboardUtil.hideSoftKeyboard(context, editText));

        dialog.show();
    }

    public static void showChooseDialog(Context context, String title,
                                        List<XDisplayItem> items, OnItemChooseListener listener) {

        DisplayListAdapter adapter = new DisplayListAdapter(context);
        adapter.setItems(items);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setAdapter(adapter, (dialog, which) -> {
            listener.onChooseItem(adapter.getItem(which));
        });
        builder.setTitle(title);
        builder.show();
    }

    public interface OnItemChooseListener {

        void onChooseItem(XDisplayItem item);
    }
}
