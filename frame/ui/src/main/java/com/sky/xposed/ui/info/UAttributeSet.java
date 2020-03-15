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

package com.sky.xposed.ui.info;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import com.sky.xposed.ui.interfaces.XAttributeSet;

/**
 * Created by sky on 2019-09-10.
 */
public class UAttributeSet implements XAttributeSet {

    private SparseArray<Object> mSparseArray;

    private UAttributeSet(Build build) {
        mSparseArray = build.mSparseArray;
    }

    @Override
    public int getInt(int key, int defaultValue) {
        Object value = mSparseArray.get(key);
        return value != null ? (int) value : defaultValue;
    }

    @Override
    public int getColor(int key, int defaultValue) {
        Object value = mSparseArray.get(key);
        return value != null ? (int) value : defaultValue;
    }

    @Override
    public int getImage(int key, int defaultValue) {
        Object value = mSparseArray.get(key);
        return value != null ? (int) value : defaultValue;
    }

    @Override
    public ColorDrawable getColorDrawable(int key, ColorDrawable defaultValue) {
        Object value = mSparseArray.get(key);
        return value != null ? (ColorDrawable) value : defaultValue;
    }

    @Override
    public Drawable getDrawable(int key, Drawable defaultValue) {
        Object value = mSparseArray.get(key);
        return value != null ? (Drawable) value : defaultValue;
    }

    public final static class Build {

        private SparseArray<Object> mSparseArray = new SparseArray<>();

        public Build() {

        }

        public Build putInt(int key, int value) {
            mSparseArray.append(key, value);
            return this;
        }

        public UAttributeSet build() {
            return new UAttributeSet(this);
        }
    }
}
