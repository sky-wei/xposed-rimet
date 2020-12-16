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

package tk.anysoft.xposed.lark.plugin.interfaces;

import java.util.Set;

/**
 * Created by sky on 2019/3/12.
 */
public interface XConfigManager {

    /**
     * 获取字符串
     * @param flag
     * @param defValue
     * @return
     */
    String getString(int flag, String defValue);

    boolean getBoolean(int flag, boolean defValue);

    int getInt(int flag, int defValue);

    long getLong(int flag, long defValue);

    Set<String> getStringSet(int flag, Set<String> defValue);

    void putString(int flag, String value);

    void putBoolean(int flag, boolean value);

    void putInt(int flag, int value);

    void putLong(int flag, long value);

    void putStringSet(int flag, Set<String> value);

    /**
     * 获取指定名称的配置管理对象
     * @param name
     * @return
     */
    XConfigManager getConfigManager(String name);

    void release();
}
