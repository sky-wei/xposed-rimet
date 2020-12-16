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

package tk.anysoft.xposed.lark.data;

import android.content.Context;
import android.content.SharedPreferences;

import tk.anysoft.xposed.lark.plugin.interfaces.XConfigManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sky on 2018/12/24.
 */
public class ConfigManager implements XConfigManager {

    private Context mContext;
    private SimplePreferences mSimplePreferences;
    private Map<String, XConfigManager> mManagerMap = new HashMap<>();

    private ConfigManager(Build build) {
        mContext = build.mXPluginManager.getContext();
        mSimplePreferences = new SimplePreferences(mContext, build.mName);
    }

    @Override
    public String getString(int flag, String defValue) {
        return mSimplePreferences.getString(flag, defValue);
    }

    @Override
    public boolean getBoolean(int flag, boolean defValue) {
        return mSimplePreferences.getBoolean(flag, defValue);
    }

    @Override
    public int getInt(int flag, int defValue) {
        return mSimplePreferences.getInt(flag, defValue);
    }

    @Override
    public long getLong(int flag, long defValue) {
        return mSimplePreferences.getLong(flag, defValue);
    }

    @Override
    public Set<String> getStringSet(int flag, Set<String> defValue) {
        return mSimplePreferences.getStringSet(flag, defValue);
    }

    @Override
    public void putString(int flag, String value) {
        mSimplePreferences.putString(flag, value);
    }

    @Override
    public void putBoolean(int flag, boolean value) {
        mSimplePreferences.putBoolean(flag, value);
    }

    @Override
    public void putInt(int flag, int value) {
        mSimplePreferences.putInt(flag, value);
    }

    @Override
    public void putLong(int flag, long value) {
        mSimplePreferences.putLong(flag, value);
    }

    @Override
    public void putStringSet(int flag, Set<String> value) {
        mSimplePreferences.putStringSet(flag, value);
    }

    @Override
    public XConfigManager getConfigManager(String name) {

        if (mManagerMap.containsKey(name)) {
            // 存在了直接返回
            return mManagerMap.get(name);
        }

        // 创建对象
        SimplePreferences preferences = new SimplePreferences(mContext, name);
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
    private final class SimplePreferences implements XConfigManager {

        private SharedPreferences mSharedPreferences;

        public SimplePreferences(Context context, String name) {
            mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        @Override
        public String getString(int flag, String defValue) {
            return mSharedPreferences.getString(Integer.toString(flag), defValue);
        }

        @Override
        public boolean getBoolean(int flag, boolean defValue) {
            return mSharedPreferences.getBoolean(Integer.toString(flag), defValue);
        }

        @Override
        public int getInt(int flag, int defValue) {
            return mSharedPreferences.getInt(Integer.toString(flag), defValue);
        }

        @Override
        public long getLong(int flag, long defValue) {
            return mSharedPreferences.getLong(Integer.toString(flag), defValue);
        }

        @Override
        public Set<String> getStringSet(int flag, Set<String> defValue) {
            return mSharedPreferences.getStringSet(Integer.toString(flag), defValue);
        }

        @Override
        public void putString(int flag, String value) {
            mSharedPreferences.edit().putString(Integer.toString(flag), value).apply();
        }

        @Override
        public void putBoolean(int flag, boolean value) {
            mSharedPreferences.edit().putBoolean(Integer.toString(flag), value).apply();
        }

        @Override
        public void putInt(int flag, int value) {
            mSharedPreferences.edit().putInt(Integer.toString(flag), value).apply();
        }

        @Override
        public void putLong(int flag, long value) {
            mSharedPreferences.edit().putLong(Integer.toString(flag), value).apply();
        }

        @Override
        public void putStringSet(int flag, Set<String> value) {
            mSharedPreferences.edit().putStringSet(Integer.toString(flag), value).apply();
        }

        @Override
        public XConfigManager getConfigManager(String name) {
            throw new IllegalArgumentException("不支持当前操作");
        }

        @Override
        public void release() {
            throw new IllegalArgumentException("不支持当前操作");
        }
    }

    public static class Build {

        private XPluginManager mXPluginManager;
        private String mName;

        public Build(XPluginManager xPluginManager) {
            mXPluginManager = xPluginManager;
        }

        public Build setConfigName(String name) {
            mName = name;
            return this;
        }

        public XConfigManager build() {
            return new ConfigManager(this);
        }
    }
}
