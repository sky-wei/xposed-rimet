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

package tk.anysoft.xposed.lark.ui.base;

import android.content.SharedPreferences;

import tk.anysoft.xposed.lark.data.ResourceManager;
import com.sky.xposed.common.ui.base.BaseDialogFragment;
import tk.anysoft.xposed.lark.Constant;
import tk.anysoft.xposed.lark.plugin.PluginManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XResourceManager;

/**
 * Created by sky on 2018/12/18.
 */
public abstract class BaseDialog extends BaseDialogFragment {

    @Override
    public SharedPreferences getDefaultSharedPreferences() {
        return getSharedPreferences(Constant.Name.LARK);
    }

    public XPluginManager getPluginManager() {
        return PluginManager.getInstance();
    }

    public XResourceManager getResourceManager() {
        return getPluginManager().getResourceManager();
    }

    public ResourceManager.Theme getMTheme() {
        return getResourceManager().getTheme();
    }
}
