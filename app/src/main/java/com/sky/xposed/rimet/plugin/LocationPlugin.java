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

import android.location.Location;
import android.text.TextUtils;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.rimet.XConstant;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * Created by sky on 2020-03-01.
 */
@APlugin()
public class LocationPlugin extends AbstractPlugin {

    public LocationPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void hook() {

        /****************  位置信息处理 ******************/

        findMethod(
                "com.amap.api.location.AMapLocationClient",
                "getLastKnownLocation")
                .after(param -> param.setResult(getLastKnownLocation(param.getResult())));

        findMethod(
                "com.amap.api.location.AMapLocationClient",
                "setLocationListener",
                "com.amap.api.location.AMapLocationListener")
                .before(param -> param.args[0] = proxyLocationListener(param.args[0]));
    }

    /**
     * 获取最后一次位置信息
     * @param location
     * @return
     */
    private Object getLastKnownLocation(Object location) {
        return isEnableVirtualLocation() ? null : location;
    }

    /**
     * 代理位置监听
     * @param listener
     * @return
     */
    private Object proxyLocationListener(Object listener) {

        if (!Proxy.isProxyClass(listener.getClass())) {
            // 创建代理类
            return Proxy.newProxyInstance(
                    listener.getClass().getClassLoader(),
                    listener.getClass().getInterfaces(),
                    new AMapLocationListenerProxy(listener));
        }
        return listener;
    }

    /**
     * 是否启用虚拟位置
     * @return
     */
    private boolean isEnableVirtualLocation() {
        return isEnable(XConstant.Key.ENABLE_VIRTUAL_LOCATION);
    }


    /**
     * 位置监听代理
     */
    private final class AMapLocationListenerProxy implements InvocationHandler {

        private Object mListener;
        private Random mRandom = new Random();

        private AMapLocationListenerProxy(Object listener) {
            mListener = listener;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

            if (isEnableVirtualLocation()
                    && "onLocationChanged".equals(method.getName())) {
                // 开始处理
                handlerLocationChanged(objects);
            }
            return method.invoke(mListener, objects);
        }

        private void handlerLocationChanged(Object[] objects) {

            if (objects == null || objects.length != 1) return;

            Location location = (Location) objects[0];

            String latitude = getPString(XConstant.Key.LOCATION_LATITUDE);
            String longitude = getPString(XConstant.Key.LOCATION_LONGITUDE);

            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                // 重新修改值
                int number = mRandom.nextInt(15 - 3 + 1) + 3;
                location.setLongitude(Double.parseDouble(longitude) + Double.valueOf(number) / 100000);
                location.setLatitude(Double.parseDouble(latitude) + Double.valueOf(number) / 100000);
            }
        }
    }
}
