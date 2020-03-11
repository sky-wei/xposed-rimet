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

package com.sky.xposed.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sky on 2018/12/18.
 */
public class CollectionUtil {

    private CollectionUtil() {

    }

    public static ArrayList<String> setToList(Set<String> value) {
        return isEmpty(value) ? new ArrayList<>() : new ArrayList<>(value);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static Set<String> toSet(String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> boolean isArraysEmpty(T... ts) {
        return ts == null || ts.length <= 0;
    }

    public static <T> List<T> newEmptyList() {
        return new ArrayList<>();
    }

    public static <T> Set<T> newEmptySet() {
        return new HashSet<>();
    }

    public static <K, V> Map<K, V> newEmptyMap() {
        return new HashMap<>();
    }
}
