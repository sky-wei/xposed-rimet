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

package com.sky.xposed.core.component;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.core.base.AbstractComponent;
import com.sky.xposed.core.interfaces.XPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sky on 2020-03-11.
 */
public class CorePreferences extends AbstractComponent implements XPreferences {

    private final XPreferences.Factory mFactory;
    private final SimplePreferences mSimplePreferences;

    private Map<String, XPreferences> mManagerMap = new HashMap<>();

    private CorePreferences(Build build) {
        mFactory = build.mFactory;
        mSimplePreferences = new SimplePreferences(build.mFactory, build.mName);
    }

    @Override
    public Map<String, ?> getAll() {
        return mSimplePreferences.getAll();
    }

    @Override
    public String getString(String key, String defValue) {
        return mSimplePreferences.getString(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mSimplePreferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return mSimplePreferences.contains(key);
    }

    @Override
    public SharedPreferences.Editor edit() {
        return mSimplePreferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSimplePreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSimplePreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mSimplePreferences.getInt(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mSimplePreferences.getFloat(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mSimplePreferences.getLong(key, defValue);
    }

    @Override
    public double getDouble(String key, double defValue) {
        return mSimplePreferences.getDouble(key, defValue);
    }

    @Override
    public String getString(String key) {
        return getString(key, "");
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public int getInt(String key) {
        return getInt(key, 0);
    }

    @Override
    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    @Override
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    @Override
    public double getDouble(String key) {
        return getDouble(key, 0.0D);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValue) {
        return mSimplePreferences.getStringSet(key, defValue);
    }

    @Override
    public XPreferences putString(String key, String value) {
        return mSimplePreferences.putString(key, value);
    }

    @Override
    public XPreferences putBoolean(String key, boolean value) {
        return mSimplePreferences.putBoolean(key, value);
    }

    @Override
    public XPreferences putInt(String key, int value) {
        return mSimplePreferences.putInt(key, value);
    }

    @Override
    public XPreferences putFloat(String key, float value) {
        return mSimplePreferences.putFloat(key, value);
    }

    @Override
    public XPreferences putLong(String key, long value) {
        return mSimplePreferences.putLong(key, value);
    }

    @Override
    public XPreferences putDouble(String key, double value) {
        return mSimplePreferences.putDouble(key, value);
    }

    @Override
    public XPreferences putStringSet(String key, Set<String> value) {
        return mSimplePreferences.putStringSet(key, value);
    }

    @Override
    public XPreferences remove(String key) {
        return mSimplePreferences.remove(key);
    }

    @Override
    public XPreferences clear() {
        return mSimplePreferences.clear();
    }

    @Override
    public SharedPreferences getOriginalPreferences() {
        return mSimplePreferences.getOriginalPreferences();
    }

    @Override
    public SharedPreferences getAssistPreferences() {
        return mSimplePreferences.getAssistPreferences();
    }

    @Override
    public XPreferences refreshName(String name) {
        return mSimplePreferences.refreshName(name);
    }

    @Override
    public XPreferences getPreferences(String name) {

        if (mManagerMap.containsKey(name)) {
            // 存在了直接返回
            return mManagerMap.get(name);
        }

        // 创建对象
        SimplePreferences preferences = new SimplePreferences(mFactory, name, false);
        mManagerMap.put(name, preferences);

        return preferences;
    }

    @Override
    public void release() {
        mManagerMap.clear();
    }

    /**
     * 一个简单属性配置管理类
     */
    private final class SimplePreferences extends AbstractComponent implements XPreferences {

        private final XPreferences.Factory mFactory;
        private String mName;
        private boolean mBindName;
        private SharedPreferences mSharedPreferences;
        private SharedPreferences mAssistPreferences;   // 用于记录类型

        private SimplePreferences(XPreferences.Factory factory, String name) {
            this(factory, name, true);
        }

        private SimplePreferences(XPreferences.Factory factory, String name, boolean bindName) {
            mFactory = factory;
            mName = name;
            mBindName = bindName;
            mSharedPreferences = factory.create(name);
            mAssistPreferences = factory.create(name + "_assist");
        }

        @Override
        public Map<String, ?> getAll() {
            return null;
        }

        @Override
        public String getString(String key, String defValue) {
            return mSharedPreferences.getString(key, defValue);
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            return mSharedPreferences.getBoolean(key, defValue);
        }

        @Override
        public boolean contains(String key) {
            return mSharedPreferences.contains(key);
        }

        @Override
        public SharedPreferences.Editor edit() {
            return mSharedPreferences.edit();
        }

        @Override
        public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public int getInt(String key, int defValue) {
            return mSharedPreferences.getInt(key, defValue);
        }

        @Override
        public float getFloat(String key, float defValue) {
            return mSharedPreferences.getFloat(key, defValue);
        }

        @Override
        public long getLong(String key, long defValue) {
            return mSharedPreferences.getLong(key, defValue);
        }

        @Override
        public double getDouble(String key, double defValue) {
            return parseDouble(getString(key, ""), defValue);
        }

        @Override
        public String getString(String key) {
            return getString(key, "");
        }

        @Override
        public boolean getBoolean(String key) {
            return getBoolean(key, false);
        }

        @Override
        public int getInt(String key) {
            return getInt(key, 0);
        }

        @Override
        public float getFloat(String key) {
            return getFloat(key, 0.0f);
        }

        @Override
        public long getLong(String key) {
            return getLong(key, 0L);
        }

        @Override
        public double getDouble(String key) {
            return getDouble(key, 0.0D);
        }

        private double parseDouble(String value, double defaultValue) {

            double result = defaultValue;

            if (!TextUtils.isEmpty(value)) {
                try {
                    result = Double.parseDouble(value);
                } catch (Throwable e) {
                    Alog.e("NumberFormatException", e);
                }
            }
            return result;
        }

        @Override
        public Set<String> getStringSet(String key, Set<String> defValue) {
            return mSharedPreferences.getStringSet(key, defValue);
        }

        @Override
        public XPreferences putString(String key, String value) {
            mSharedPreferences.edit().putString(key, value).apply();
            mAssistPreferences.edit().putInt(key, DataType.STRING).apply();
            return this;
        }

        @Override
        public XPreferences putBoolean(String key, boolean value) {
            mSharedPreferences.edit().putBoolean(key, value).apply();
            mAssistPreferences.edit().putInt(key, DataType.BOOLEAN).apply();
            return this;
        }

        @Override
        public XPreferences putInt(String key, int value) {
            mSharedPreferences.edit().putInt(key, value).apply();
            mAssistPreferences.edit().putInt(key, DataType.INT).apply();
            return this;
        }

        @Override
        public XPreferences putFloat(String key, float value) {
            mSharedPreferences.edit().putFloat(key, value).apply();
            mAssistPreferences.edit().putInt(key, DataType.FLOAT).apply();
            return this;
        }

        @Override
        public XPreferences putLong(String key, long value) {
            mSharedPreferences.edit().putLong(key, value).apply();
            mAssistPreferences.edit().putInt(key, DataType.LONG).apply();
            return this;
        }

        @Override
        public XPreferences putDouble(String key, double value) {
            putString(key, Double.toString(value));
            return this;
        }

        @Override
        public XPreferences putStringSet(String key, Set<String> value) {
            mSharedPreferences.edit().putStringSet(key, value).apply();
            mAssistPreferences.edit().putInt(key, DataType.STR_SET).apply();
            return this;
        }

        @Override
        public XPreferences remove(String key) {
            mSharedPreferences.edit().remove(key).apply();
            mAssistPreferences.edit().remove(key).apply();
            return this;
        }

        @Override
        public XPreferences clear() {
            mSharedPreferences.edit().clear().apply();
            mAssistPreferences.edit().clear().apply();
            return this;
        }

        @Override
        public SharedPreferences getOriginalPreferences() {
            return mSharedPreferences;
        }

        @Override
        public SharedPreferences getAssistPreferences() {
            return mAssistPreferences;
        }

        @Override
        public XPreferences refreshName(String name) {

            // 不绑定名称的不处理
            if (!mBindName) return this;

            if (TextUtils.equals(mName, name)) {
                // 名称相同不需要处理
                return this;
            }

            Alog.d("CorePreferences refreshName: " + name);

            // 重新创建新的
            mName = name;
            mSharedPreferences = mFactory.create(name);
            mAssistPreferences = mFactory.create(name + "_assist");

            return this;
        }

        @Override
        public XPreferences getPreferences(String name) {
            throw new IllegalArgumentException("不支持当前操作");
        }

        @Override
        public void release() {
            throw new IllegalArgumentException("不支持当前操作");
        }
    }

    public static class Build {

        private XPreferences.Factory mFactory;
        private String mName;

        public Build(XPreferences.Factory factory) {
            mFactory = factory;
        }

        public Build setDefaultName(String name) {
            mName = name;
            return this;
        }

        public XPreferences build() {

            if (TextUtils.isEmpty(mName)) {
                throw new IllegalArgumentException("默认名称不能为空");
            }

            return new CorePreferences(this);
        }
    }
}
