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

package com.sky.xposed.rimet.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.rimet.BuildConfig;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.data.model.LocationModel;
import com.sky.xposed.rimet.ui.activity.AnalysisActivity;
import com.sky.xposed.rimet.ui.activity.MainActivity;
import com.sky.xposed.rimet.ui.util.ActivityUtil;
import com.sky.xposed.rimet.ui.util.DialogUtil;
import com.sky.xposed.rimet.ui.util.XViewUtil;
import com.sky.xposed.ui.UIAttribute;
import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.info.UAttributeSet;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.ViewUtil;
import com.sky.xposed.ui.view.EditNumItemView;
import com.sky.xposed.ui.view.EditTextItemView;
import com.sky.xposed.ui.view.GroupItemView;
import com.sky.xposed.ui.view.PluginFrameLayout;
import com.sky.xposed.ui.view.XEditItemView;

import java.util.Map;

/**
 * Created by sky on 2019/3/13.
 */
public class SettingsDialog extends BasePluginDialog {

    private TextView tvPrompt;
    private EditTextItemView sivSettingsLocation;

    @Override
    public void createView(PluginFrameLayout frameView) {

        removeContentTopPadding();

        int left = DisplayUtil.dip2px(getContext(), 15);
        int top = DisplayUtil.dip2px(getContext(), 12);

        tvPrompt = new TextView(getContext());
        tvPrompt.setTextSize(14);
        tvPrompt.setBackgroundColor(Color.GRAY);
        tvPrompt.setTextColor(Color.WHITE);
        tvPrompt.setPadding(left, top, left, top);
        frameView.addSubView(tvPrompt);


        /*****************   红包   ****************/

        XViewUtil.newTopSortItemView(getContext(), "红包")
                .addToFrame(frameView);


        XViewUtil.newSwitchItemView(getContext(), "快速打开红包", "用户点击红包,程序自动打开红包")
                .trackBind(XConstant.Key.ENABLE_FAST_LUCKY, Boolean.FALSE)
                .addToFrame(frameView);

        GroupItemView luckyGroup = new GroupItemView(getContext());
        luckyGroup.setVisibility(View.GONE);

        XViewUtil.newSwitchItemView(getContext(), "自动接收红包", "开启时自动接收红包")
                .trackBind(XConstant.Key.ENABLE_LUCKY, Boolean.FALSE, luckyGroup)
                .addToFrame(frameView);

        luckyGroup.addToFrame(frameView);

        EditNumItemView sivLuckyDelayed = new EditNumItemView(getContext(), new UAttributeSet.Build().build());
        sivLuckyDelayed.setMaxLength(4);
        sivLuckyDelayed.setUnit("毫秒");
        sivLuckyDelayed.setName("红包延迟时间");
        sivLuckyDelayed.setExtendHint("延迟时间单位(毫秒)");
        sivLuckyDelayed.trackBind(XConstant.Key.LUCKY_DELAYED, 500);
        sivLuckyDelayed.addToFrame(luckyGroup);


        /*****************   防撤回   ****************/

        XViewUtil.newSortItemView(getContext(), "防撤回")
                .addToFrame(frameView);

        XViewUtil.newSwitchItemView(getContext(), "消息防撤回", "开启时消息不会被撤回")
                .trackBind(XConstant.Key.ENABLE_RECALL, Boolean.FALSE)
                .addToFrame(frameView);


        /*****************   虚拟定位   ****************/

        XViewUtil.newSortItemView(getContext(), "打卡(Beta)")
                .addToFrame(frameView);

        GroupItemView locationGroup = new GroupItemView(getContext());
        locationGroup.setVisibility(View.GONE);

        XViewUtil.newSwitchItemView(getContext(), "虚拟定位", "开启时会修改当前位置信息")
                .trackBind(XConstant.Key.ENABLE_VIRTUAL_LOCATION, Boolean.FALSE, locationGroup)
                .addToFrame(frameView);

        locationGroup.addToFrame(frameView);

        sivSettingsLocation = new EditTextItemView(getContext(), new UAttributeSet.Build()
                .putInt(UIAttribute.EditTextItem.style, XEditItemView.Style.MULTI_LINE)
                .build());
        sivSettingsLocation.setName("位置信息");
        sivSettingsLocation.setExtendHint("设置位置信息");
        sivSettingsLocation.setOnItemClickListener(view -> {

            LocationDialog dialog = new LocationDialog();
            dialog.show(getActivity(), (resultCode, data) -> {

                if (Activity.RESULT_OK == resultCode) {
                    // 保存选择的位置信息
                    saveLocationInfo((LocationModel) data.getSerializable(XConstant.Key.DATA));
                }
            });
        });
        sivSettingsLocation.trackBind(XConstant.Key.LOCATION_ADDRESS, "");
        sivSettingsLocation.addToFrame(locationGroup);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        getTitleView().setElevation(DisplayUtil.DIP_4);

        showBack();
        setTitle("钉钉助手");
        showMoreMenu();

        tvPrompt.setOnClickListener(v -> {
            // 进入分析界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(BuildConfig.APPLICATION_ID, AnalysisActivity.class.getName());
            startActivityForResult(intent, 99);
        });

        // 是否支持版本
        XPreferences preferences = getDefaultPreferences();
        String cMd5 = preferences.getString(XConstant.Key.PACKAGE_MD5);
        String aMd5 = preferences.getString(toHexString(M.sky.rimet_package_md5));

        setPromptText(TextUtils.equals(cMd5, aMd5) ? "" : "不支持当前版本! 点击适配版本");
    }

    @Override
    public void onCreateMoreMenu(Menu menu) {
        super.onCreateMoreMenu(menu);

        menu.add(0, 0, 0, "爱心公益");
        menu.add(0, 1, 0, "助手");
        menu.add(0, 2, 0, "关于");
    }

    @Override
    public boolean onMoreItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                // 打开捐赠界面
                LoveDialog loveDialog = new LoveDialog();
                loveDialog.show(getFragmentManager(), "love");
                return true;
            case 1:
                // 打开助手界面
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName(BuildConfig.APPLICATION_ID, MainActivity.class.getName());
                ActivityUtil.startActivity(getContext(), intent);
                return true;
            case 2:
                // 打开关于界面
                DialogUtil.showAboutDialog(getContext());
                return true;
            default:
                break;
        }
        return super.onMoreItemSelected(item);
    }

    /**
     * 设置提示消息
     * @param text
     */
    private void setPromptText(String text) {
        tvPrompt.setText(text);
        ViewUtil.setVisibility(tvPrompt, TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    /**
     * 保存位置信息
     * @param model
     */
    private void saveLocationInfo(LocationModel model) {

        if (model == null) return;

        XPreferences preferences = getDefaultPreferences();
        preferences.putString(XConstant.Key.LOCATION_ADDRESS, model.getAddress());
        preferences.putString(XConstant.Key.LOCATION_LATITUDE, Double.toString(model.getLatitude()));
        preferences.putString(XConstant.Key.LOCATION_LONGITUDE, Double.toString(model.getLongitude()));

        // 设置UI信息
        sivSettingsLocation.setExtend(model.getAddress());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 99 || Activity.RESULT_OK != resultCode) return;

        Map<Integer, String> map = (Map<Integer, String>) data.getSerializableExtra(XConstant.Key.DATA);

        if (CollectionUtil.isEmpty(map) || map.size() < 4) {
            showMessage("无法获取适配的版本信息!");
            return;
        }

        // 保存信息
        XPreferences preferences = getDefaultPreferences();
        preferences.putString(toHexString(M.sky.rimet_package_md5), map.get(M.sky.rimet_package_md5));
        preferences.putString(toHexString(M.classz.class_defpackage_MessageDs), map.get(M.classz.class_defpackage_MessageDs));
        preferences.putString(toHexString(M.classz.class_defpackage_ServiceFactory), map.get(M.classz.class_defpackage_ServiceFactory));
        preferences.putString(toHexString(M.classz.class_defpackage_RedPacketsRpc), map.get(M.classz.class_defpackage_RedPacketsRpc));
        preferences.putString(toHexString(M.classz.class_defpackage_RedPacketsRpc_9), map.get(M.classz.class_defpackage_RedPacketsRpc) + "$9");

        DialogUtil.showDialog(getContext(),
                "提示", "\n适配成功! 重启即可生效,是否马上重启?", (dialog, which) -> {
            getCoreManager().getLoadPackage().getHandler().postDelayed(() -> {
                // 退出
                Process.killProcess(Process.myPid());
                System.exit(0);
            }, 300);
        });
    }

    private String toHexString(int key) {
        return Integer.toHexString(key);
    }
}
