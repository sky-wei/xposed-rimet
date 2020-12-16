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

import android.content.Context;
import android.os.Handler;

import tk.anysoft.xposed.lark.data.source.IRepositoryFactory;

import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by sky on 2019/3/11.
 */
public interface XPluginManager {

    /**
     * 返回当前Hook应用的Context
     * @return
     */
    Context getContext();

    /**
     * 返回程序创建的Handler
     * @return
     */
    Handler getHandler();

    /**
     * 获取加载的包信息
     * @return
     */
    XC_LoadPackage.LoadPackageParam getLoadPackageParam();

    /**
     * 获取版本管理对象
     * @return
     */
    XVersionManager getVersionManager();

    /**
     * 获取配置管理对象
     * @return
     */
    XConfigManager getConfigManager();

    /**
     * 获取资源管理对象
     * @return
     */
    XResourceManager getResourceManager();

    /**
     * 获取仓库工厂类
     * @return
     */
    IRepositoryFactory getRepositoryFactory();

    /**
     * 获取相应flag的插件
     * @param flag
     * @return
     */
    List<XPlugin> getXPlugins(int flag);

    /**
     * 获取插件信息获取指定插件
     * @param info
     * @return
     */
    XPlugin getXPlugin(XPlugin.Info info);

    /**
     * 获取相应id获取相应插件
     * @param id
     * @return
     */
    XPlugin getXPluginById(int id);

    /**
     * 根据相应的Class获取相应的对象
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T getObject(Class<T> tClass);

    /**
     * 开始处理加载的包
     */
    void handleLoadPackage();

    /**
     * 释放
     */
    void release();
}
