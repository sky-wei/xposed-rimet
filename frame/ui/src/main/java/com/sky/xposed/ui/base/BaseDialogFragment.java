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

package com.sky.xposed.ui.base;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XPreferences;
import com.sky.xposed.core.interfaces.XResourceManager;
import com.sky.xposed.ui.util.CoreUtil;

/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化View
        initView(view, getArguments());
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(View view, Bundle args);

    public XCoreManager getCoreManager() {
        return CoreUtil.getCoreManager();
    }

    public XPreferences getDefaultPreferences() {
        return getCoreManager().getDefaultPreferences();
    }

    public XPreferences getPreferencesByName(String name) {
        return getDefaultPreferences().getPreferences(name);
    }

    public XResourceManager getResourceManager() {
        return getCoreManager().getResourceManager();
    }

    public Uri getPluginResource(int resId) {
        return getResourceManager().getPluginResource(resId);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
