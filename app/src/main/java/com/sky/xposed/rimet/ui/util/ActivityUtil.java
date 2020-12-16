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

package com.sky.xposed.rimet.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ToastUtil;


/**
 * Created by sky on 18-3-12.
 */

public class ActivityUtil {

    static final String ALI_PAY_URI = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=";

    public static boolean startActivity(Context context, Intent intent) {

        try {
            // 获取目标包名
            String packageName = intent.getPackage();

            // 设置启动参数
            if (!TextUtils.isEmpty(packageName)
                    && !TextUtils.equals(packageName, context.getPackageName())) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            // 启动Activity
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Alog.e("启动Activity异常", e);
        }
        return false;
    }

    public static boolean startActivityForResult(Activity activity, Intent intent, int requestCode) {

        try {
            // 获取目标包名
            String packageName = intent.getPackage();

            // 设置启动参数
            if (!TextUtils.isEmpty(packageName)
                    && !TextUtils.equals(packageName, activity.getPackageName())) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            // 启动Activity
            activity.startActivityForResult(intent, requestCode);
            return true;
        } catch (Exception e) {
            Alog.e("启动Activity异常", e);
        }
        return false;
    }

    public static boolean startActivity(Context context, Class<? extends Activity> classActivity) {

        Intent intent = new Intent(context, classActivity);
        intent.setAction(Intent.ACTION_VIEW);

        return startActivity(context, intent);
    }

    public static boolean startActivityForResult(Activity activity, Class<? extends Activity> classActivity, int requestCode) {

        Intent intent = new Intent(activity, classActivity);
        intent.setAction(Intent.ACTION_VIEW);

        return startActivityForResult(activity, intent, requestCode);
    }

    public static boolean openUrl(Context context, String url) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // 调用系统浏览器打开
            context.startActivity(intent);
            return true;
        } catch (Throwable tr) {
            ToastUtil.show("打开浏览器异常");
        }
        return false;
    }

    /**
     * 启动支付宝
     * @param context
     * @param payUrl
     * @return
     */
    public static boolean startAlipay(Context context, String payUrl) {

        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(ALI_PAY_URI + payUrl));

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                intent.setData(Uri.parse(payUrl));
                context.startActivity(intent);
            }
            return true;
        } catch (Throwable tr) {
            Alog.e("启动失败", tr);
            return false;
        }
    }
}
