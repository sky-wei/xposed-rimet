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

package com.sky.xposed.rimet.contract;

import com.sky.xposed.rimet.data.model.LocationModel;
import com.sky.xposed.ui.base.BasePresenter;
import com.sky.xposed.ui.base.BaseView;

import java.util.List;

/**
 * Created by sky on 2019-05-28.
 */
public interface LocationContract {

    interface View extends BaseView {

        /**
         * 成功
         * @param models
         */
        void onLoad(List<LocationModel> models);

        /**
         * 失败
         * @param msg
         */
        void onLoadFailed(String msg);

        /**
         * 成功
         */
        void onSaveSucceed();

        /**
         * 失败
         * @param msg
         */
        void onSaveFailed(String msg);
    }

    interface Presenter extends BasePresenter {

        /**
         * 加载
         */
        void load();

        /**
         * 保存
         * @param models
         */
        void save(List<LocationModel> models);
    }
}
