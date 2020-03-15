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

package com.sky.xposed.core.util;

import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2020-01-16.
 */
public final class FilterUtil {

    private FilterUtil() {

    }

    public static  <T> List<T> filterList(List<T> list, Filter<T> filter) {

        if (list == null || filter == null) return list;

        List<T> newList = new ArrayList<>();

        for (T data : list) {
            if (filter.accept(data)) {
                newList.add(data);
            }
        }
        return newList;
    }

    public static <T> List<Class<? extends T>> filterClass(
            List<Class<? extends T>> list, Filter<Class<? extends T>> filter) {

        if (list == null || filter == null) return list;

        List<Class<? extends T>> newList = new ArrayList<>();

        for (Class<? extends T> data : list) {
            if (filter.accept(data)) {
                newList.add(data);
            }
        }
        return newList;
    }

    public static List<Class<? extends XPlugin>> filterPlugin(
            List<Class<? extends XPlugin>> list, Filter<Class<? extends XPlugin>> filter) {
        return filterClass(list, filter);
    }

    public static List<Class<? extends XConfig>> filterConfig(
            List<Class<? extends XConfig>> list, Filter<Class<? extends XConfig>> filter) {
        return filterClass(list, filter);
    }

    public interface Filter<T> {

        boolean accept(T data);
    }
}
