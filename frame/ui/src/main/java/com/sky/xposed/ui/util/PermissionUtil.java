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

package com.sky.xposed.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

/**
 * Created by sky on 2018/8/8.
 */
public final class PermissionUtil {

    private PermissionUtil() {

    }

    public static int checkPermission(Context context, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 请求权限
            return context.checkPermission(permission, Process.myPid(), Process.myUid());
        }
        return PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 请求权限
            activity.requestPermissions(permissions, requestCode);
        }
    }
}
