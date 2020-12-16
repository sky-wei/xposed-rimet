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

package tk.anysoft.xposed.lark;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.javax.MethodHook;
import com.sky.xposed.javax.XposedPlus;
import com.sky.xposed.javax.XposedUtil;
import tk.anysoft.xposed.lark.plugin.PluginManager;
import tk.anysoft.xposed.lark.plugin.interfaces.XPluginManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by sky on 2019/3/14.
 */
public class Main implements IXposedHookLoadPackage, MethodHook.ThrowableCallback {

    @Override
    public void onThrowable(Throwable tr) {
        Alog.e("Throwable", tr);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpParam) throws Throwable {

        if (!Constant.Rimet.PACKAGE_NAME.equals(lpParam.packageName)) return;
        Log.d("LarkHelper","found com.ss.android.lark");
        // 设置默认的参数
        XposedPlus.setDefaultInstance(new XposedPlus.Builder(lpParam)
                .throwableCallback(this)
                .build());

        // Hook
        Log.d("LarkHelper","init");
        XposedUtil
                .findMethod(
                        "com.ss.android.lark.app.LarkApplication",
                        "onCreate")
                .before(param -> {
                    Log.d("LarkHelper","com.ss.android.lark.app.LarkApplication onCreate");

                    Application application = (Application) param.thisObject;
                    Context context = application.getApplicationContext();
                    Log.d("LarkHelper application",application.getClass().getName());
                    if (TextUtils.equals(
                            "com.ss.android.lark.app.LarkApplication",
                            application.getClass().getName())) {
                        Log.d("LarkHelper ","com.ss.android.lark.main.app.MainActivity");
                        XPluginManager pluginManager = new PluginManager
                                .Build(context)
                                .setLoadPackageParam(lpParam)
                                .build();

                        // 开始处理加载的包
                        pluginManager.handleLoadPackage();
                    }
                });
    }
}
