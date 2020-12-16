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

/**
 * Created by sky on 2020/3/25.
 */
public class StationModel implements Serializable {

    private String mName;

    private int mMcc;
    private int mMnc;

    private int mLac;
    private int mCellId;

    public StationModel() {
    }

    public StationModel(String name, int mcc, int mnc, int lac, int cellId) {
        mName = name;
        mMcc = mcc;
        mMnc = mnc;
        mLac = lac;
        mCellId = cellId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getMcc() {
        return mMcc;
    }

    public void setMcc(int mcc) {
        mMcc = mcc;
    }

    public int getMnc() {
        return mMnc;
    }

    public void setMnc(int mnc) {
        mMnc = mnc;
    }

    public int getLac() {
        return mLac;
    }

    public void setLac(int lac) {
        mLac = lac;
    }

    public int getCellId() {
        return mCellId;
    }

    public void setCellId(int cellId) {
        mCellId = cellId;
    }

    public String getDesc() {
        return "mcc: " + getMnc() +
                "\tmnc: " + getMnc() + "\n" +
                "lac: " + getLac() +
                "\tcellId: " + getCellId();
    }
}
