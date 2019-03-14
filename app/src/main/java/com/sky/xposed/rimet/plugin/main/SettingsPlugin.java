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

package com.sky.xposed.rimet.plugin.main;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.sky.xposed.rimet.Constant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.data.model.PluginInfo;
import com.sky.xposed.rimet.plugin.base.BasePlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPlugin;
import com.sky.xposed.rimet.plugin.interfaces.XPluginManager;
import com.sky.xposed.rimet.ui.dialog.PluginSettingsDialog;

/**
 * Created by sky on 2018/12/30.
 */
public class SettingsPlugin extends BasePlugin {

    public SettingsPlugin(XPluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Info getInfo() {
        return new PluginInfo(Constant.Plugin.MAIN_SETTINGS, "设置");
    }

    @Override
    public void onHandleLoadPackage() {

        findMethod(M.classz.class_ui_LauncherUI, M.method.method_ui_LauncherUI_onCreateOptionsMenu, Menu.class)
                .after(param -> {

                    XPlugin xPlugin = getPluginManager()
                            .getXPluginById(Constant.Plugin.MAIN_SETTINGS);

                    if (xPlugin.isEnable(Constant.XFlag.MAIN_MENU, true)) {
                        Menu menu = (Menu) param.args[0];
                        menu.add(Constant.GroupId.GROUP,
                                Constant.ItemId.MAIN_SETTINGS, 0, Constant.Name.TITLE);
                    }
                });

        findMethod(M.classz.class_ui_LauncherUI, M.method.method_ui_LauncherUI_onOptionsItemSelected, MenuItem.class)
                .after(param -> {

                    MenuItem item = (MenuItem) param.args[0];
                    Activity activity = (Activity) param.thisObject;

                    if (Constant.ItemId.MAIN_SETTINGS == item.getItemId()) {
                        // 打开设置界面
                        openSettings(activity);
                    }
                });
    }

    @Override
    public void openSettings(Activity activity) {

        PluginSettingsDialog dialog = new PluginSettingsDialog();
        dialog.show(activity.getFragmentManager(), "settings");
    }
}
