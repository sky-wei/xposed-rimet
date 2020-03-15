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
import android.view.View;

import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.interfaces.XItemView;
import com.sky.xposed.ui.interfaces.XTrackStatus;
import com.sky.xposed.ui.util.CoreUtil;

/**
 * Created by sky on 2019-06-11.
 */
public abstract class XTrackItemView<T> extends XFrameItemView implements XTrackStatus<T> {

    private XPreferences mPreferences;
    private XTrackStatus.StatusListener<T> mStatusListener;
    private String mKey;
    private T mDefValue;

    public XTrackItemView(Context context) {
        super(context);
    }

    public XTrackItemView(Context context, XAttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 将Key跟View进行绑定
     */
    protected abstract void bindView(String key, T value);

    @Override
    public XTrackStatus<T> setPreferences(XPreferences preferences) {
        mPreferences = preferences;
        return this;
    }

    @Override
    public XTrackStatus<T> bind(String key, T defValue, BindListener<T> listener) {

        if (key == null) throw new NullPointerException("绑定的Key不能为空!");

        mKey = key;
        mDefValue = defValue;

        final T value = getKeyValue();

        if (listener != null) {
            listener.onStatusBind(key, value);
        }

        // 绑定
        bindView(key, value);
        return this;
    }

    @Override
    public XTrackStatus<T> track(StatusListener<T> listener) {
        mStatusListener = listener;
        return this;
    }

    @Override
    public String getKey() {
        return mKey;
    }

    public XPreferences getPreferences() {
        return mPreferences;
    }

    public T getDefValue() {
        return mDefValue;
    }

    public StatusListener<T> getStatusListener() {
        return mStatusListener;
    }

    public boolean callOnStatusChange(View view, String key, T value) {
        return mStatusListener == null || mStatusListener.onStatusChange(view, key, value);
    }

    public XTrackItemView<T> trackBind(XPreferences xPreferences,
                                       String key, T defValue, XTrackStatus.BindListener<T> listener,
                                       XTrackStatus.StatusListener<T> listener2) {
        setPreferences(xPreferences)
                .bind(key, defValue, listener)
                .track(listener2);
        return this;
    }

    public XTrackItemView<T> trackBind(XPreferences xPreferences, String key, T defValue) {
        return trackBind(xPreferences, key, defValue, null, null);
    }

    public XTrackItemView<T> trackBind(String key, T defValue, XTrackStatus.BindListener<T> listener,
                                       XTrackStatus.StatusListener<T> listener2) {
        return trackBind(CoreUtil.getDefaultPreferences(), key, defValue, listener, listener2);
    }

    public XTrackItemView<T> trackBind(String key, T defValue, XTrackStatus.StatusListener<T> listener) {
        return trackBind(key, defValue, null, listener);
    }

    public XTrackItemView<T> trackBind(String key, T defValue, XItemView xItemView) {
        if (!(defValue instanceof Boolean)) {
            throw new IllegalArgumentException("只支持Boolean类型操作!");
        }
        return trackBind(key, defValue, (key1, value) -> {
            xItemView.setVisibility((Boolean) value);
        }, (view, key12, value) -> {
            xItemView.setVisibility((Boolean) value);
            return true;
        });
    }

    public XTrackItemView<T> trackBind(String key, T defValue) {
        return trackBind(key, defValue, null, null);
    }
}
