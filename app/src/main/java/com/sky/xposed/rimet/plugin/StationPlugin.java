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

import android.telephony.TelephonyManager;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.XConstant;
import com.sky.xposed.rimet.plugin.base.BaseDingPlugin;

/**
 * Created by sky on 2020/3/25.
 */
@APlugin
public class StationPlugin extends BaseDingPlugin {

    public StationPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        findMethod(TelephonyManager.class, "getCellLocation")
                .before(param -> {

                    if (isEnable(XConstant.Key.ENABLE_VIRTUAL_STATION)) {


                    }
                });

//        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getCellLocation", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                try {
////                    Context context = myContext;
//                    Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
//
//                    final SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Name.RIMET, Context.MODE_PRIVATE);
//                    boolean enbaleBaseStation = sharedPreferences.getBoolean(Integer.toString(Constant.XFlag.ENABLE_BASESTATION), false);
//                    String baseStationData = sharedPreferences.getString(Integer.toString(Constant.XFlag.BASESTATIONDATA), "{}");
//
//                    if (enbaleBaseStation && null != baseStationData) {
//                        JSONObject object = new JSONObject(baseStationData);
//                        if (object.length() > 0) {
//                            Bundle bundle = new Bundle();
//                            Integer mcc = object.getInt("mcc");
//                            Integer mnc = object.getInt("mnc");
//                            Integer lac = object.getInt("lac");
//                            Integer cellId = object.getInt("cellId");
//
//                            if (mnc != 3 && mnc != 5 && mnc != 11) {
//                                if (null != lac && null != cellId) {
//                                    bundle.putInt("lac", lac);
//                                    bundle.putInt("cid", cellId);
//                                    param.setResult(new GsmCellLocation(bundle));
//                                    XposedBridge.log("DingTalkHelper：模拟为GSM卡，lac:" + lac + "，cid" + cellId);
//                                }
//                            } else {
//                                if (null != lac && null != cellId) {
//                                    bundle.putInt("networkId", lac);
//                                    bundle.putInt("baseStationId", cellId);
//                                    param.setResult(new CdmaCellLocation(bundle));
//                                    XposedBridge.log("DingTalkHelper：模拟为CDMA卡，lac:" + lac + "，cid:" + cellId);
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception ex) {
//                    XposedBridge.log("DingTalkHelper：" + "hook BaseStation fail！" + ex.getMessage());
//                }
//            }
//        });
    }
}
