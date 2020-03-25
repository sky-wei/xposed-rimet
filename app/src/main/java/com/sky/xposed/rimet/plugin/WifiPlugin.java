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

package com.sky.xposed.rimet.plugin;

import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.CollectionUtil;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.data.model.WifiModel;
import com.sky.xposed.rimet.plugin.base.BaseDingPlugin;
import com.sky.xposed.rimet.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by sky on 2020/3/25.
 */
@APlugin
public class WifiPlugin extends BaseDingPlugin {

    public WifiPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        findMethod(WifiManager.class, "isWifiEnabled")
                .before(param -> {
                    if (isEnable(XConstant.Key.ENABLE_VIRTUAL_WIFI)) {
                        // 只在虚拟Wifi开启才处理
                        param.setResult(true);
                    }
                });

        findMethod(WifiManager.class, "getScanResults")
                .before(this::handlerGetScanResults);

        findMethod(WifiManager.class, "getConnectionInfo")
                .before(this::handlerGetConnectionInfo);
    }

    private void handlerGetScanResults(XC_MethodHook.MethodHookParam param) throws Exception {

        if (!isEnable(XConstant.Key.ENABLE_VIRTUAL_WIFI)) {
            // 没有启用不需要处理
            return;
        }

        String value = getPString(XConstant.Key.WIFI_SCAN_RESULT, "");

        if (TextUtils.isEmpty(value)) {
            // 不需要处理
            return;
        }

        // 获取当前保存的记录信息
        List<WifiModel.ScanResult> list = GsonUtil.fromJson(value,
                new TypeToken<List<WifiModel.ScanResult>>() {}.getType());

        if (CollectionUtil.isEmpty(list)) {
            // 不需要处理
            return;
        }

        List<ScanResult> scanResults = new ArrayList<>();

        for (WifiModel.ScanResult result : list) {

            ScanResult scanResult = ScanResult.class.newInstance();
            scanResult.SSID = result.getSsId();
            scanResult.BSSID = result.getBssId();

            scanResults.add(scanResult);
        }

        param.setResult(scanResults);

        Alog.d(">>>>>>>>>>>>>>> 设置ScanResults " + scanResults);
    }

    private void handlerGetConnectionInfo(XC_MethodHook.MethodHookParam param) {

        if (!isEnable(XConstant.Key.ENABLE_VIRTUAL_WIFI)) {
            // 没有启用不需要处理
            return;
        }

        int state = getPInt(XConstant.Key.WIFI_STATE, -99);

        if (state == -99) {
            // 暂未获取信息
            return;
        }

        String ssId = getPString(XConstant.Key.WIFI_SS_ID);
        String bssId = getPString(XConstant.Key.WIFI_BSS_ID);
        String macAddress = getPString(XConstant.Key.WIFI_MAC_ADDRESS);

        WifiInfo wifiInfo = (WifiInfo) XposedHelpers.newInstance(WifiInfo.class);

        XposedHelpers.setIntField(wifiInfo, "mNetworkId", 68); // MAX_RSSI
        XposedHelpers.setObjectField(wifiInfo, "mSupplicantState", SupplicantState.COMPLETED);
        XposedHelpers.setObjectField(wifiInfo, "mBSSID", bssId);
        XposedHelpers.setObjectField(wifiInfo, "mMacAddress", macAddress);
        XposedHelpers.setIntField(wifiInfo, "mLinkSpeed", 433);  // Mbps
        XposedHelpers.setIntField(wifiInfo, "mFrequency", 5785); // MHz
        XposedHelpers.setIntField(wifiInfo, "mRssi", -49); // MAX_RSSI

        Class tClass = findClass("android.net.wifi.WifiSsid");
        Object wifiSsId = XposedHelpers.callStaticMethod(tClass, "createFromAsciiEncoded", ssId);
        XposedHelpers.setObjectField(wifiInfo, "mWifiSsid", wifiSsId);

        param.setResult(wifiInfo);

        Alog.d(">>>>>>>>>>>>>>> 设置ConnectionInfo " + wifiInfo);
    }
}
