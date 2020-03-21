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

import android.content.Context;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.data.M;
import com.sky.xposed.rimet.event.MessageEvent;

import java.util.Collection;

/**
 * Created by sky on 2019/3/14.
 */
@APlugin
public class DingDingPlugin extends AbstractPlugin {

    public DingDingPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        findMethod(
                M.classz.class_lightapp_runtime_LightAppRuntimeReverseInterfaceImpl,
                M.method.method_lightapp_runtime_LightAppRuntimeReverseInterfaceImpl_initSecurityGuard,
                Context.class)
                .before(param -> param.setResult(null));


        findMethod(
                M.classz.class_defpackage_MessageDs,
                M.method.method_defpackage_MessageDs_handler,
                String.class, Collection.class, boolean.class)
                .after(param -> {
                    notice(MessageEvent.class, observer -> {
                        // 处理消息
                        observer.onHandlerMessage((String) param.args[0], (Collection) param.args[1]);
                    });
                });
    }
}
