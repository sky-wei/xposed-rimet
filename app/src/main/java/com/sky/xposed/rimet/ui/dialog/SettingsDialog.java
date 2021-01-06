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
import com.sky.xposed.rimet.data.model.StationModel;
import com.sky.xposed.rimet.data.model.WifiModel;
import com.sky.xposed.rimet.ui.activity.AnalysisActivity;
import com.sky.xposed.rimet.ui.activity.MainActivity;
import com.sky.xposed.rimet.ui.util.ActivityUtil;
import com.sky.xposed.rimet.ui.util.DialogUtil;
import com.sky.xposed.rimet.ui.util.XViewUtil;
import com.sky.xposed.rimet.util.FileUtil;
import com.sky.xposed.rimet.util.GsonUtil;
import com.sky.xposed.ui.UIAttribute;
import com.sky.xposed.ui.UIConstant;
import com.sky.xposed.ui.base.BasePluginDialog;
import com.sky.xposed.ui.info.UAttributeSet;
import com.sky.xposed.ui.util.DisplayUtil;
import com.sky.xposed.ui.util.ViewUtil;
import com.sky.xposed.ui.view.EditNumItemView;
import com.sky.xposed.ui.view.EditTextItemView;
import com.sky.xposed.ui.view.GroupItemView;
import com.sky.xposed.ui.view.PluginFrameLayout;
import com.sky.xposed.ui.view.XEditItemView;

import java.io.File;
import java.util.Map;

/**
 * Created by sky on 2019/3/13.
 */
public class SettingsDialog extends BasePluginDialog {

    private TextView tvPrompt;
    private EditTextItemView sivSettingsLocation;
    private EditTextItemView sivSettingsWifi;
    private EditTextItemView sivSettingsStation;

    private XPreferences mPreferences;

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


        GroupItemView noOpenTimeGroup = new GroupItemView(getContext());
        noOpenTimeGroup.setVisibility(View.GONE);
        noOpenTimeGroup.addToFrame(frameView);

        XViewUtil.newSwitchItemView(getContext(), "夜间不抢", "夜间时段不自动抢红包")
                .trackBind(XConstant.Key.ENABLE_NO_OPEN_TIME, Boolean.FALSE, noOpenTimeGroup)
                .addToFrame(luckyGroup);

        EditTextItemView noOpenStartTime = new EditTextItemView(getContext(), new UAttributeSet.Build().build());
        noOpenStartTime.setMaxLength(4);
        noOpenStartTime.setName("夜间开始时间");
        noOpenStartTime.setExtendHint("夜间开始时间(HHmm)");
        noOpenStartTime.setUnit("(HHmm)");
        noOpenStartTime.setInputType(UIConstant.InputType.NUMBER);
        noOpenStartTime.trackBind(XConstant.Key.NO_OPEN_START_TIME, "2300");
        noOpenStartTime.addToFrame(noOpenTimeGroup);

        EditTextItemView noOpenEndTime = new EditTextItemView(getContext(), new UAttributeSet.Build().build());
        noOpenEndTime.setMaxLength(4);
        noOpenEndTime.setName("夜间结束时间");
        noOpenEndTime.setExtendHint("夜间结束时间(HHmm)");
        noOpenEndTime.setUnit("(HHmm)");
        noOpenEndTime.setInputType(UIConstant.InputType.NUMBER);
        noOpenEndTime.trackBind(XConstant.Key.NO_OPEN_END_TIME, "0800");
        noOpenEndTime.addToFrame(noOpenTimeGroup);





        /*****************   防撤回   ****************/

        XViewUtil.newSortItemView(getContext(), "防撤回")
                .addToFrame(frameView);

        XViewUtil.newSwitchItemView(getContext(), "消息防撤回", "开启时消息不会被撤回")
                .trackBind(XConstant.Key.ENABLE_RECALL, Boolean.FALSE)
                .addToFrame(frameView);


        XViewUtil.newSortItemView(getContext(), "打卡(Beta)")
                .addToFrame(frameView);

        /*****************   虚拟定位   ****************/

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

        /*****************   Wifi   ****************/

        GroupItemView wifiGroup = new GroupItemView(getContext());
        wifiGroup.setVisibility(View.GONE);

        XViewUtil.newSwitchItemView(getContext(), "虚拟Wifi", "开启时会修改当前Wifi信息")
                .trackBind(XConstant.Key.ENABLE_VIRTUAL_WIFI, Boolean.FALSE, wifiGroup)
                .addToFrame(frameView);

        wifiGroup.addToFrame(frameView);

        sivSettingsWifi = new EditTextItemView(getContext(), new UAttributeSet.Build()
                .putInt(UIAttribute.EditTextItem.style, XEditItemView.Style.MULTI_LINE)
                .build());
        sivSettingsWifi.setName("Wifi信息");
        sivSettingsWifi.setExtendHint("设置Wifi信息");
        sivSettingsWifi.setOnItemClickListener(view -> {

            // 记录最后的结果,并关闭当前开关(自己获取信息不需要Hook)
            final boolean lastValue =
                    mPreferences.getBoolean(XConstant.Key.ENABLE_VIRTUAL_WIFI);
            mPreferences.putBoolean(XConstant.Key.ENABLE_VIRTUAL_WIFI, false);

            WifiDialog dialog = new WifiDialog();
            dialog.show(getActivity(), (resultCode, data) -> {

                mPreferences.putBoolean(XConstant.Key.ENABLE_VIRTUAL_WIFI, lastValue);

                if (Activity.RESULT_OK == resultCode) {
                    // 保存选择的位置信息
                    saveWifiInfo((WifiModel) data.getSerializable(XConstant.Key.DATA));
                }
            });
        });
        sivSettingsWifi.trackBind(XConstant.Key.WIFI_INFO, "");
        sivSettingsWifi.addToFrame(wifiGroup);

        /*****************   基站   ****************/

        GroupItemView stationGroup = new GroupItemView(getContext());
        stationGroup.setVisibility(View.GONE);

        XViewUtil.newSwitchItemView(getContext(), "虚拟基站", "开启时会修改当前基站信息")
                .trackBind(XConstant.Key.ENABLE_VIRTUAL_STATION, Boolean.FALSE, stationGroup)
                .addToFrame(frameView);

        stationGroup.addToFrame(frameView);

        sivSettingsStation = new EditTextItemView(getContext(), new UAttributeSet.Build()
                .putInt(UIAttribute.EditTextItem.style, XEditItemView.Style.MULTI_LINE)
                .build());
        sivSettingsStation.setName("基站信息");
        sivSettingsStation.setExtendHint("设置基站信息");
        sivSettingsStation.setOnItemClickListener(view -> {

            // 记录最后的结果,并关闭当前开关(自己获取信息不需要Hook)
            final boolean lastValue =
                    mPreferences.getBoolean(XConstant.Key.ENABLE_VIRTUAL_STATION);
            mPreferences.putBoolean(XConstant.Key.ENABLE_VIRTUAL_STATION, false);

            StationDialog dialog = new StationDialog();
            dialog.show(getActivity(), (resultCode, data) -> {

                mPreferences.putBoolean(XConstant.Key.ENABLE_VIRTUAL_STATION, lastValue);

                if (Activity.RESULT_OK == resultCode) {
                    // 保存选择的位置信息
                    saveStationInfo((StationModel) data.getSerializable(XConstant.Key.DATA));
                }
            });
        });
        sivSettingsStation.trackBind(XConstant.Key.STATION_INFO, "");
        sivSettingsStation.addToFrame(stationGroup);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        mPreferences = getDefaultPreferences();

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
        String cMd5 = mPreferences.getString(XConstant.Key.PACKAGE_MD5);
        String aMd5 = mPreferences.getString(toHexString(M.sky.rimet_package_md5));

        setPromptText(TextUtils.equals(cMd5, aMd5) ? "" : "不支持当前版本! 点击去适配");
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

        mPreferences.putString(XConstant.Key.LOCATION_ADDRESS, model.getAddress());
        mPreferences.putString(XConstant.Key.LOCATION_LATITUDE, Double.toString(model.getLatitude()));
        mPreferences.putString(XConstant.Key.LOCATION_LONGITUDE, Double.toString(model.getLongitude()));

        // 设置UI信息
        sivSettingsLocation.setExtend(model.getAddress());
    }

    /**
     * 保存基站信息
     * @param model
     */
    private void saveStationInfo(StationModel model) {

        if (model == null) return;

        mPreferences.putString(XConstant.Key.STATION_INFO, model.getDesc());
        mPreferences.putInt(XConstant.Key.STATION_MCC, model.getMcc());
        mPreferences.putInt(XConstant.Key.STATION_MNC, model.getMnc());
        mPreferences.putInt(XConstant.Key.STATION_LAC, model.getLac());
        mPreferences.putInt(XConstant.Key.STATION_CELL_ID, model.getCellId());

        // 设置UI信息
        sivSettingsStation.setExtend(model.getDesc());
    }

    /**
     * 保存Wifi信息
     * @param model
     */
    private void saveWifiInfo(WifiModel model) {

        if (model == null) return;

        mPreferences.putString(XConstant.Key.WIFI_INFO, model.getDesc());
        mPreferences.putBoolean(XConstant.Key.WIFI_ENABLED, model.isEnabled());
        mPreferences.putInt(XConstant.Key.WIFI_STATE, model.getState());
        mPreferences.putString(XConstant.Key.WIFI_SS_ID, model.getSsId());
        mPreferences.putString(XConstant.Key.WIFI_BSS_ID, model.getBssId());
        mPreferences.putString(XConstant.Key.WIFI_MAC_ADDRESS, model.getMacAddress());
        mPreferences.putString(XConstant.Key.WIFI_SCAN_RESULT, GsonUtil.toJson(model.getScanResults()));

        // 设置UI信息
        sivSettingsWifi.setExtend(model.getDesc());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 99 || Activity.RESULT_OK != resultCode) return;

        Map<Integer, String> map = (Map<Integer, String>) data.getSerializableExtra(XConstant.Key.DATA);

        if (CollectionUtil.isEmpty(map) || map.size() < 3) {
            showMessage("无法获取适配的版本信息!");
            return;
        }

        // 获取当前md5值
        String md5 = FileUtil.getFileMD5(new File(getContext().getApplicationInfo().sourceDir));

        // 保存信息
        XPreferences preferences = getDefaultPreferences();
        preferences.putString(toHexString(M.sky.rimet_package_md5), md5);
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
