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

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.sky.xposed.common.util.ConversionUtil;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.interfaces.XAttributeSet;

/**
 * Created by sky on 2019-06-21.
 */
public class EditNumItemView extends XEditItemView<Number> {

    public EditNumItemView(Context context) {
        super(context);
    }

    public EditNumItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(XAttributeSet attrs) {
        super.initView(attrs);

        // 默认输入纯数字
        setInputType(UIConstant.InputType.NUMBER_SIGNED);
    }

    @Override
    public void setInputType(int inputType) {
        super.setInputType(inputType);

        if (UIConstant.InputType.TEXT == inputType
                || UIConstant.InputType.TEXT_PASSWORD == inputType
                || UIConstant.InputType.NUMBER_PASSWORD == inputType) {
            throw new IllegalArgumentException("只支持数字类型");
        }
    }

    @Override
    protected void bindView(String key, Number value) {

        // 设置显示信息
        setExtend(value.toString());
        setOnEditChangeListener(new OnEditChangeListener() {
            @Override
            public String getDefaultText() {
                // 获取文本信息
                return getKeyValue().toString();
            }

            @Override
            public void onEditChanged(View view, String text) {

                final Number value = transformValue(text, getDefValue());

                if (callOnStatusChange(view, key, value)) {
                    // 保存信息
                    setExtend(value.toString());

                    final XPreferences xPreferences = getPreferences();

                    if (value instanceof Integer) {
                        xPreferences.putInt(getKey(), value.intValue());
                    } else if (value instanceof Long) {
                        xPreferences.putLong(getKey(), value.longValue());
                    } else if (value instanceof Float) {
                        xPreferences.putFloat(getKey(), value.floatValue());
                    } else if (value instanceof Double) {
                        xPreferences.putDouble(getKey(), value.doubleValue());
                    } else {
                        throw new IllegalArgumentException("不支持类型:" + value.getClass());
                    }
                }
            }
        });
    }

    @Override
    public Number getKeyValue() {

        final Number value = getDefValue();
        final XPreferences xPreferences = getPreferences();

        if (value instanceof Integer) {
            return xPreferences.getInt(getKey(), value.intValue());
        } else if (value instanceof Long) {
            return xPreferences.getLong(getKey(), value.longValue());
        } else if (value instanceof Float) {
            return xPreferences.getFloat(getKey(), value.floatValue());
        } else if (value instanceof Double) {
            return xPreferences.getDouble(getKey(), value.doubleValue());
        } else {
            throw new IllegalArgumentException("不支持类型:" + value.getClass());
        }
    }

    /**
     * 转换编辑的内容
     * @param value
     * @param defValue
     * @return
     */
    private Number transformValue(String value, Number defValue) {

        if (TextUtils.isEmpty(value)) return defValue;

        if (defValue instanceof Integer) {
            return ConversionUtil.parseInt(value, defValue.intValue());
        } else if (defValue instanceof Long) {
            return ConversionUtil.parseLong(value, defValue.longValue());
        } else if (defValue instanceof Float) {
            return ConversionUtil.parseFloat(value, defValue.floatValue());
        } else if (defValue instanceof Double) {
            return ConversionUtil.parseDouble(value, defValue.doubleValue());
        } else {
            throw new IllegalArgumentException("不支持类型:" + value.getClass());
        }
    }
}
