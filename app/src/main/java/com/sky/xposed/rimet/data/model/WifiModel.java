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

package com.sky.xposed.rimet.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sky on 2020/3/25.
 */
public class WifiModel implements Serializable {

    private String mName;

    private boolean mEnabled;
    private int mState;

    private String mSsId;
    private String mBssId;
    private String mMacAddress;

    private List<ScanResult> mScanResults;

    public WifiModel(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public String getSsId() {
        return mSsId;
    }

    public void setSsId(String ssId) {
        mSsId = ssId;
    }

    public String getBssId() {
        return mBssId;
    }

    public void setBssId(String bssId) {
        mBssId = bssId;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        mMacAddress = macAddress;
    }

    public List<ScanResult> getScanResults() {
        return mScanResults;
    }

    public void setScanResults(List<ScanResult> scanResults) {
        mScanResults = scanResults;
    }

    public static final class ScanResult implements Serializable {

        private String mSsId;
        private String mBssId;

        public ScanResult() {
        }

        public ScanResult(String ssId, String bssId) {
            mSsId = ssId;
            mBssId = bssId;
        }

        public String getSsId() {
            return mSsId;
        }

        public void setSsId(String ssId) {
            mSsId = ssId;
        }

        public String getBssId() {
            return mBssId;
        }

        public void setBssId(String bssId) {
            mBssId = bssId;
        }
    }

    public String getDesc() {
        return "SSID: " + getSsId() + "\n" +
                "BSSID: " + getBssId() + "\n" +
                "MacAddress: " + getMacAddress();
    }
}
