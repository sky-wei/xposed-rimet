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

import com.sky.xposed.rimet.data.model.UpdateModel;
import com.sky.xposed.ui.base.BasePresenter;
import com.sky.xposed.ui.base.BaseView;

/**
 * Created by sky on 2019-05-28.
 */
public interface RimetContract {

    interface View extends BaseView {

        /**
         * 提示用户更新
         * @param model
         */
        void onUpdate(UpdateModel model);

        /**
         * 更新失败
         * @param msg
         */
        void onUpdateFailed(String msg);

        /**
         * 更新配置成功
         */
        void onUpdateConfigSucceed();

        /**
         * 更新配置失败
         * @param msg
         */
        void onUpdateConfigFailed(String msg);

        /**
         * 清除成功
         */
        void onClearConfigSucceed();

        /**
         * 清除失败
         * @param msg
         */
        void onClearConfigFailed(String msg);
    }

    interface Presenter extends BasePresenter {

        /**
         * 检测更新
         */
        void checkUpdate(boolean auto);

        /**
         * 更新配置
         */
        void updateConfig(boolean auto);

        /**
         * 清除配置
         */
        void clearConfig();
    }
}
