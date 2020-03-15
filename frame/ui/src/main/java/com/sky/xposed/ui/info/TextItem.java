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

package com.sky.xposed.ui.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2019-09-02.
 */
public class TextItem implements Serializable {

    private final int id;
    private final String name;

    private TextItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static TextItem create(int id, String name) {
        return new TextItem(id, name);
    }

    public static List<TextItem> create(String... items) {

        if (items == null) return null;

        List<TextItem> textItems = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            textItems.add(create(i, items[i]));
        }
        return textItems;
    }
}
