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

package com.sky.xposed.rimet.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sky.xposed.core.interfaces.XConfig;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.rimet.BuildConfig;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.ui.activity.MapActivity;
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

        XViewUtil.newSwitchItemView(getContext(), "虚拟定位", "开启时会修改当前位置信息")
                .trackBind(XConstant.Key.ENABLE_VIRTUAL_LOCATION, Boolean.FALSE)
                .addToFrame(frameView);

        sivSettingsLocation = new EditTextItemView(getContext(), new UAttributeSet.Build()
                .putInt(UIAttribute.EditTextItem.style, XEditItemView.Style.MULTI_LINE)
                .build());
        sivSettingsLocation.setName("位置信息");
        sivSettingsLocation.setExtendHint("设置位置信息");
        sivSettingsLocation.setOnItemClickListener(view -> {
            // 跳转到地图界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(BuildConfig.APPLICATION_ID, MapActivity.class.getName());
            startActivityForResult(intent, 99);
        });
        sivSettingsLocation.trackBind(XConstant.Key.LOCATION_ADDRESS, "");
        sivSettingsLocation.addToFrame(frameView);
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        getTitleView().setElevation(DisplayUtil.DIP_4);

        showBack();
        setTitle("钉钉助手");
        showMoreMenu();

        // 是否支持版本
        XConfig xConfig = getCoreManager().getVersionManager().getSupportConfig();
        setPromptText(xConfig != null ? "" : "不支持当前版本!");
    }

    @Override
    public void onCreateMoreMenu(Menu menu) {
        super.onCreateMoreMenu(menu);

        menu.add(0, 0, 0, "爱心公益");
        menu.add(0, 1, 0, "关于");
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
                // 打开关于界面
                DialogUtil.showAboutDialog(getContext());
                return true;
            default:
                break;
        }
        return super.onMoreItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            // 保存位置信息
            saveLocationInfo(
                    data.getStringExtra("address"),
                    data.getDoubleExtra("latitude", 0),
                    data.getDoubleExtra("longitude", 0));
        }
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
     * @param address
     * @param latitude
     * @param longitude
     */
    private void saveLocationInfo(String address, double latitude, double longitude) {

        XPreferences preferences = getDefaultPreferences();
        preferences.putString(XConstant.Key.LOCATION_ADDRESS, address);
        preferences.putString(XConstant.Key.LOCATION_LATITUDE, Double.toString(latitude));
        preferences.putString(XConstant.Key.LOCATION_LONGITUDE, Double.toString(longitude));

        // 设置UI信息
        sivSettingsLocation.setExtend(address);
    }
}
