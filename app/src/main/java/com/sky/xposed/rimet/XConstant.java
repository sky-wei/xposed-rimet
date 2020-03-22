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

package com.sky.xposed.rimet;

/**
 * Created by sky on 2019/3/14.
 */
public interface XConstant {

    interface Service {

        String BASE_URL = "https://coding.net/u/sky_wei/p/xposed-rimet/git/raw/develop/";
    }

    interface Rimet {

        String PACKAGE_NAME = "com.alibaba.android.rimet";
    }

    interface Event {

        int CLICK = 0x01;
    }

    interface Key {

        String DATA = "data";

        String ENABLE_VIRTUAL_LOCATION = "enable_virtual_location";

        String LOCATION_LATITUDE = "location_latitude";

        String LOCATION_LONGITUDE = "location_longitude";

        String LOCATION_ADDRESS = "location_address";

        String ENABLE_FAST_LUCKY = "enable_fast_lucky";

        String ENABLE_LUCKY = "enable_lucky";

        String LUCKY_DELAYED = "lucky_delayed";

        String ENABLE_RECALL = "enable_recall";
    }
}
