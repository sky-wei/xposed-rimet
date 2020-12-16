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

package tk.anysoft.xposed.lark.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sky.xposed.common.ui.util.LayoutUtil;
import com.sky.xposed.common.util.DisplayUtil;

/**
 * Created by sky on 18-3-27.
 */

public class CategoryItemView extends FrameLayout {

    private TextView tvName;

    public CategoryItemView(@NonNull Context context) {
        this(context, null);
    }

    public CategoryItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayoutParams(LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getContext(), 30)));
        setPadding(0, 10, 0, 10);

        tvName = new TextView(getContext());
        tvName.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvName.setTextSize(13);

        LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.leftMargin = DisplayUtil.dip2px(getContext(), 15);
        params.gravity = Gravity.CENTER_VERTICAL;

        addView(tvName, params);
    }

    public void setName(String title) {
        tvName.setText(title);
    }

    public String getName() {
        return tvName.getText().toString();
    }
}
