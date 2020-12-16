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

package com.sky.xposed.rimet.data.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.jakewharton.disklrucache.DiskLruCache;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.rimet.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by sky on 2019-05-27.
 */
public class CacheManager implements ICacheManager {

    private static final long MAX_SIZE = 1024 * 1024 * 100;

    private Gson mGson;
    private DiskLruCache mDiskLruCache;

    public CacheManager(Context context) {
        mGson = new Gson();
        File cacheDir = new File(context.getCacheDir(), "config");
        try {
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, 2, 1, MAX_SIZE);
        } catch (IOException e) {
            Alog.e("异常了", e);
        }
    }


    @Override
    public String buildKey(String value) {
        return value.toLowerCase();
    }

    @Override
    public synchronized  <T> T get(String key, Class<T> tClass) {

        if (mDiskLruCache == null) return null;

        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                final String temp = snapshot.getString(0);
                return mGson.fromJson(temp, tClass);
            }
        } catch (Throwable tr) {
            Alog.e("get异常", tr);
        } finally {
            FileUtil.closeQuietly(snapshot);
        }
        return null;
    }

    @Override
    public synchronized <T> void put(String key, T value) {

        if (mDiskLruCache == null) return;

        DiskLruCache.Editor editor = null;

        try {
            editor = mDiskLruCache.edit(key);
            if (editor != null) {
                editor.set(0, mGson.toJson(value));
                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (Throwable tr) {
            abortQuietly(editor);
            Alog.e("put异常", tr);
        }
    }

    @Override
    public synchronized void remove(String key) {

        if (mDiskLruCache == null) return;

        try {
            mDiskLruCache.remove(key);
            mDiskLruCache.flush();
        } catch (Throwable tr) {
            Alog.e("remove异常", tr);
        }
    }

    @Override
    public void clear() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.delete();
            } catch (IOException e) {
                Alog.e("删除异常", e);
            }
        }
    }

    @Override
    public void close() {
        FileUtil.closeQuietly(mDiskLruCache);
    }

    /**
     * 保存失败
     * @param editor
     */
    private void abortQuietly(DiskLruCache.Editor editor) {

        if (editor == null) return;

        try {
            editor.abort();
        } catch (Throwable tr) {
            Alog.e("abort异常", tr);
        }
    }
}
