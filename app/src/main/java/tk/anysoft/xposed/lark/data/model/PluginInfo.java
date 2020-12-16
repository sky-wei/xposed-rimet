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

package tk.anysoft.xposed.lark.data.model;


import tk.anysoft.xposed.lark.plugin.interfaces.XPlugin;

/**
 * Created by sky on 2018/12/30.
 */
public class PluginInfo implements XPlugin.Info {

    private int id;
    private String name;
    private String desc;

    public PluginInfo(int id, String name) {
        this(id, name, "这是一个牛逼的功能");
    }

    public PluginInfo(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
