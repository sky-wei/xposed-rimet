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

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.plugin.base.BaseDingPlugin;

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

//        XposedHelpers.findAndHookMethod(android.net.wifi.WifiManager.class, "isWifiEnabled", new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        param.setResult(true);
//                        XposedBridge.log("DingTalkHelper：模拟 wifi开启");
//                    }
//                }
//        );
//
//        XposedHelpers.findAndHookMethod(android.net.wifi.WifiManager.class, "getScanResults", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                try {
//                    Context context = myContext;
//                    final SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Name.RIMET, Context.MODE_PRIVATE);
//                    boolean enbaleWifi = sharedPreferences.getBoolean(Integer.toString(Constant.XFlag.ENABLE_WIFI), false);
//                    String wifiData = sharedPreferences.getString(Integer.toString(Constant.XFlag.WIFIDATA), "{}");
//                    if (enbaleWifi && null != wifiData) {
//                        JSONObject object = new JSONObject(wifiData);
//                        if (object.length() > 0 && object.has("scanResults")) {
//                            JSONArray scanResultsArray = object.getJSONArray("scanResults");
//                            List<ScanResult> wifiList = new ArrayList<>();
//                            for (int i = 0; i < scanResultsArray.length(); i++) {
//                                try {
//                                    ScanResult scanResult = ScanResult.class.newInstance();
//                                    scanResult.SSID = scanResultsArray.getJSONObject(i).getString("ssid");
//                                    scanResult.BSSID = scanResultsArray.getJSONObject(i).getString("bssid");
//                                    ;
//                                    wifiList.add(scanResult);
//                                } catch (Exception ex) {
//                                    XposedBridge.log(ex.getCause());
//                                }
//
//                            }
//                            XposedBridge.log("DingTalkHelper：模拟了" + wifiList.size() + "个WIFI信息");
//                            param.setResult(wifiList);
//                        }
//                    }
//                } catch (Exception ex) {
//                    XposedBridge.log("DingTalkHelper：" + "hook wifi fail！" + ex.getMessage());
//                }
//            }
//        });
//
//        XposedHelpers.findAndHookMethod(android.net.wifi.WifiManager.class, "getConnectionInfo", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                try {
//                    XposedBridge.log("DingTalkHelperaaaa：1");
//                    Context context = myContext;
//                    final SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Name.RIMET, Context.MODE_PRIVATE);
//                    boolean enbaleWifi = sharedPreferences.getBoolean(Integer.toString(Constant.XFlag.ENABLE_WIFI), false);
//                    boolean enbaleWifiCurrent = sharedPreferences.getBoolean(Integer.toString(Constant.XFlag.ENABLE_WIFI_CURRENT), false);
//                    String wifiData = sharedPreferences.getString(Integer.toString(Constant.XFlag.WIFIDATA), "{}");
//                    XposedBridge.log("DingTalkHelper：读取wifi配置信息:" + wifiData);
//                    if (enbaleWifi && enbaleWifiCurrent && null != wifiData) {
//                        JSONObject object = new JSONObject(wifiData);
//                        if (object.length() > 0 && object.has("connectionInfo")) {
//                            JSONObject connectionInfo = object.getJSONObject("connectionInfo");
//                            WifiInfo wifiInfo = (WifiInfo) XposedHelpers.newInstance(WifiInfo.class);
//                            XposedHelpers.setIntField((Object) wifiInfo, "mNetworkId", 68); // MAX_RSSI
//                            XposedHelpers.setObjectField((Object) wifiInfo, "mSupplicantState", SupplicantState.COMPLETED);
//                            XposedHelpers.setObjectField((Object) wifiInfo, "mBSSID", connectionInfo.getString("bssid"));
//                            XposedHelpers.setObjectField((Object) wifiInfo, "mMacAddress", connectionInfo.getString("mac"));
////                            InetAddress ipAddress = (InetAddress) XposedHelpers.newInstance(InetAddress.class);
////                            XposedHelpers.setObjectField((Object) wifiInfo, "mIpAddress", "192.168.3.102");
//                            XposedHelpers.setIntField((Object) wifiInfo, "mLinkSpeed", 433);  // Mbps
//                            XposedHelpers.setIntField((Object) wifiInfo, "mFrequency", 5785); // MHz
//                            XposedHelpers.setIntField((Object) wifiInfo, "mRssi", -49); // MAX_RSSI
//                            try {
//                                Class cls = XposedHelpers.findClass("android.net.wifi.WifiSsid", myContext.getClassLoader());
//                                Object wifissid = XposedHelpers.callStaticMethod(cls, "createFromAsciiEncoded", connectionInfo.getString("ssid"));
//                                XposedHelpers.setObjectField((Object) wifiInfo, "mWifiSsid", wifissid);
//                            } // Kitkat
//                            catch (Error e) {
//                                XposedHelpers.setObjectField((Object) wifiInfo, "mSSID", connectionInfo.getString("ssid"));
//                            }
//                            XposedBridge.log("DingTalkHelper：模拟了已连接wifi:" + connectionInfo.getString("ssid"));
//                            param.setResult(wifiInfo);
//                        } else {
//                            XposedBridge.log("DingTalkHelper：未采集 已连接wifi信息");
//
//                        }
//                    }
//                } catch (Exception ex) {
//                    XposedBridge.log("DingTalkHelper：" + "hook wifi connect fail！" + ex.getMessage());
//                }
//            }
//        });
    }
}
