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

package com.sky.xposed.frame.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sky.xposed.frame.R;
import com.sky.xposed.frame.ui.dialog.SettingsDialog;
import com.sky.xposed.ui.util.DisplayUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {

        DisplayUtil.init(this);

        SettingsDialog dialog = new SettingsDialog();
        dialog.show(getFragmentManager(), "Settings");
    }
}
