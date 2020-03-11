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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by sky on 2019-12-04.
 */
public final class ClassUtil {

    private ClassUtil() {}

    /**
     * 获取字段信息
     * @param object
     * @param field
     * @return
     */
    public static Object getFieldValue(Object object, Field field) {

        if (field == null || object == null) return null;

        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            Alog.e("获取信息异常", e);
        }
        return null;
    }

    /**
     * 调用静态方法
     * @param method
     * @param args
     * @return
     */
    public static Object callStaticMethod(Method method, Object... args) {
        return callMethod(null, method, args);
    }

    /**
     * 调用方法
     * @param method
     * @param object
     * @param args
     * @return
     */
    public static Object callMethod(Object object, Method method, Object... args) {

        if (method == null) return null;

        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            Alog.e("获取信息异常", e);
        }
        return null;
    }

    /**
     * 查找匹配的Field
     * @param tClass
     * @param filter
     * @return
     */
    public static Field findFieldByType(Class tClass, Filter<Class> filter) {
        return findField(tClass, value -> filter.accept(value.getType()));
    }

    /**
     * 查找匹配的Field
     * @param tClass
     * @param filter
     * @return
     */
    public static Field findField(Class tClass, Filter<Field> filter) {

        if (tClass == null || filter == null) return null;

        Field[] fields = tClass.getDeclaredFields();

        for (Field field : fields) {
            if (filter.accept(field)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 查找匹配的Field
     * @param tClass
     * @param filter
     * @return
     */
    public static Method findMethodByReturnType(Class tClass, Filter<Class> filter) {
        return findMethod(tClass, value -> filter.accept(value.getReturnType()));
    }

    /**
     * 查找匹配的Field
     * @param tClass
     * @param filter
     * @return
     */
    public static Method findMethod(Class tClass, Filter<Method> filter) {

        if (tClass == null || filter == null) return null;

        Method[] methods = tClass.getDeclaredMethods();

        for (Method method : methods) {
            if (filter.accept(method)) {
                return method;
            }
        }
        return null;
    }


    /**
     * 过滤的接口
     * @param <T>
     */
    public interface Filter<T> {

        boolean accept(T value);
    }
}
