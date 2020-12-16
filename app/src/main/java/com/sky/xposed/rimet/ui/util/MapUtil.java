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


import android.location.Location;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

/**
 * Created by sky on 2019/3/26.
 */
public class MapUtil {

    private MapUtil() {

    }

    public static LatLng newLatLng(Location location) {

        if (location == null) return null;

        return newLatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng newLatLng(LatLonPoint location) {

        if (location == null) return null;

        return newLatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng newLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    public static LatLonPoint newLocation(LatLng latLng) {
        return new LatLonPoint(latLng.latitude, latLng.longitude);
    }
}
