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

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ToastUtil;

/**
 * Created by sky on 2019/3/26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Alog.setDebug(BuildConfig.DEBUG);

        // 初始化
        ToastUtil.getInstance().init(getApplicationContext());
    }
}
