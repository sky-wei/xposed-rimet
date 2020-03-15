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

package com.sky.xposed.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

import com.sky.xposed.ui.interfaces.XAttributeSet;

/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class EditTextItemView extends XEditItemView<String> {

    public EditTextItemView(Context context) {
        super(context);
    }

    public EditTextItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void bindView(String key, String value) {

        // 设置显示信息
        setExtend(value);
        setOnEditChangeListener(new OnEditChangeListener() {
            @Override
            public String getDefaultText() {
                // 获取文本信息
                return getKeyValue();
            }

            @Override
            public void onEditChanged(View view, String text) {
                if (callOnStatusChange(view, key, text)) {
                    // 保存信息
                    setExtend(text);
                    getPreferences().putString(key, text);
                }
            }
        });
    }

    @Override
    public String getKeyValue() {
        return getPreferences().getString(getKey(), getDefValue());
    }
}
