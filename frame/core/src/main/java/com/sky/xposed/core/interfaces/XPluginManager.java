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

package com.sky.xposed.core.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by sky on 2020-03-11.
 */
public interface XPluginManager extends XComponent {

    /**
     * 获取相应插件类获取相应插件
     * @param tClass
     * @return
     */
    XPlugin getPlugin(Class<? extends XPlugin> tClass);

    /**
     * 判断插件是否存在
     * @param tClass
     * @return
     */
    boolean hasPlugin(Class<? extends XPlugin> tClass);

    /**
     * 根据过滤插件获取相应的插件
     * @param filter
     * @return
     */
    List<XPlugin> getPlugins(Filter filter);

    /**
     * 开始处理加载的插件
     */
    void loadPlugins();


    /**
     * 插件过滤
     */
    interface Filter {

        boolean accept(XPlugin plugin);
    }


    interface Factory {

        /**
         * 获取插件列表
         * @return
         */
        List<Class<? extends XPlugin>> pluginList();
    }

    interface Loader {

        /**
         * 加载插件
         * @return
         */
        Map<Class<? extends XPlugin>, XPlugin> loadPlugin(Factory factory);
    }

    /**
     * 进程
     */
    interface Process {

        int ALL = 0x00;

        int MAIN = 0x01;

        int OTHER = 0x02;
    }
}
