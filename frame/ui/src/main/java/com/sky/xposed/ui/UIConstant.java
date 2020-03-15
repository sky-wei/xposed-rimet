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

package com.sky.xposed.ui;

/**
 * Created by sky on 2020-03-13.
 */
public interface UIConstant {

    interface InputType {

        int NUMBER = 0;

        int NUMBER_SIGNED = 1;

        int NUMBER_DECIMAL = 2;

        int TEXT = 3;

        int PHONE = 4;

        int TEXT_PASSWORD = 5;

        int NUMBER_PASSWORD = 6;
    }

    interface Color {

        int TITLE_BACKGROUND = 0xFF161823;

        int DEFAULT_PRESSED = 0XFFD5D5D5;

        int DEFAULT_BACKGROUND = 0xFFFFFFFF;

        int LINE_BACKGROUND = 0xFFDFDFDF;

        int ITEM_TEXT = 0xFF000000;

        int ITEM_TEXT_EXTEND = 0xFF888888;

        int ITEM_TEXT_DISABLE = 0xFF888888;

        int ITEM_STATE_CHECKED = 0xFFE91E63;

        int ITEM_STATE_NORMAL = 0XFFC5C5C5;

        int SORT_ITEM_BACKGROUND = 0xFFEEEEEE;

        int SORT_ITEM_TEXT = 0xFF666666;
    }

    interface Key {

        String TITLE = "title";                 // 界面传递的标题
    }
}
