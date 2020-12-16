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

package tk.anysoft.xposed.lark.plugin.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import tk.anysoft.xposed.lark.BuildConfig;
import tk.anysoft.xposed.lark.Constant;
import tk.anysoft.xposed.lark.plugin.base.BasePlugin;
import tk.anysoft.xposed.lark.plugin.interfaces.XPlugin;
import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;

import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.util.ResourceUtil;

import java.lang.reflect.Field;

import tk.anysoft.xposed.lark.data.model.PluginInfo;

/**
 * Created by sky on 2018/12/30.
 * 设置菜单注入菜单项
 */
public class SettingsPlugin extends BasePlugin {

    public SettingsPlugin(XPluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public XPlugin.Info getInfo() {
        return new PluginInfo(Constant.Plugin.MAIN_SETTINGS, "设置");
    }

    @Override
    public void onHandleLoadPackage() {

//        findMethod(
//                "com.alibaba.android.user.settings.activity.NewSettingActivity",
//                "onCreate",
//                Bundle.class)
//                .after(param -> onHnalderSettings((Activity) param.thisObject));

        //设置界面 hook 添加钉钉助手
        findMethod(
                "com.ss.android.lark.mine.impl.setting.MineSystemSettingActivity",
                "onCreate",
                Bundle.class)
                .after(param -> onHnalderSettings((Activity) param.thisObject));
    }

    private void onHnalderSettings(Activity activity) {

        //查找到设置菜单
        Log.d("LarkHelper", "aaaaaaaaaaaaa");
        Log.d("LarkHelper", activity.getLocalClassName());


        View view = activity.findViewById(ResourceUtil.getId(activity, "about_lark"));// 0x7f090040
//        ViewGroup viewGroup = (ViewGroup) view.getParent();
//
//        final int index = viewGroup.indexOfChild(view);
//        Log.d("LarkHelperindex ", viewGroup.indexOfChild(view) + "");

        SimpleItemView viewDing = new SimpleItemView(activity);
        viewDing.getNameView().setTextSize(17);
        viewDing.setName(Constant.Name.TITLE);
        viewDing.setExtend("v" + BuildConfig.VERSION_NAME);
        viewDing.setOnClickListener(v -> {
            // 打开设置
            openSettings(activity);
        });
        viewDing.callOnClick();
//        viewGroup.addView(viewDing, index);
    }

    @Override
    public void openSettings(Activity activity) {

        // 打开插件设置
        getPluginManager().getXPluginById(Constant.Plugin.DING_DING).openSettings(activity);

//        PluginSettingsDialog dialog = new PluginSettingsDialog();
//        dialog.show(activity.getFragmentManager(), "settings");
    }
}
