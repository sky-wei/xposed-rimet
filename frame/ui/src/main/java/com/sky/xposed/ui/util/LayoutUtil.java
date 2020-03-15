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

package com.sky.xposed.ui.util;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sky.xposed.ui.interfaces.XAttributeSet;
import com.sky.xposed.ui.view.PluginLinearLayout;

/**
 * Created by sky on 2018/8/8.
 */
public class LayoutUtil {

    private LayoutUtil() {

    }

    public static LinearLayout.LayoutParams newLinearLayoutParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    public static LinearLayout.LayoutParams newMatchLinearLayoutParams() {
        return newLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public static LinearLayout.LayoutParams newWrapLinearLayoutParams() {
        return newLinearLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static ViewGroup.LayoutParams newViewGroupParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    public static ViewGroup.LayoutParams newMatchViewGroupParams() {
        return newViewGroupParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static ViewGroup.LayoutParams newWrapViewGroupParams() {
        return newViewGroupParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams newFrameLayoutParams(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    public static FrameLayout.LayoutParams newMatchFrameLayoutParams() {
        return newFrameLayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public static FrameLayout.LayoutParams newWrapFrameLayoutParams() {
        return newFrameLayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static RelativeLayout.LayoutParams newRelativeLayoutParams(int width, int height) {
        return new RelativeLayout.LayoutParams(width, height);
    }

    public static LinearLayout newCommonLayout(Context context) {

        LinearLayout.LayoutParams params = new LayoutUtil.Build().linearParams();

        LinearLayout content = new LinearLayout(context);
        content.setLayoutParams(params);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setBackgroundColor(Color.WHITE);

        return content;
    }

    @Deprecated
    public static LinearLayout newCommonWeightLayout(Context context) {

        LinearLayout layout = newCommonLayout(context);

        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) layout.getLayoutParams();
        params.weight = 1.0f;

        return layout;
    }

    public static PluginLinearLayout newPluginLinearLayout(
            Context context, XAttributeSet attrs) {

        PluginLinearLayout layout = new PluginLinearLayout(context, attrs);

        layout.setLayoutParams(new LayoutUtil
                .Build()
                .setWeight(1.0f)
                .linearParams());

        return layout;
    }

    public static class Build {

        private int width;
        private int height;
        private float weight;
        private int gravity = -1;
        private int leftMargin;
        private int topMargin;
        private int rightMargin;
        private int bottomMargin;

        public Build() {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        public Build setWidth(int width) {
            this.width = width;
            return this;
        }

        public Build setHeight(int height) {
            this.height = height;
            return this;
        }

        public Build setWeight(float weight) {
            this.weight = weight;
            return this;
        }

        public Build setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Build setLeftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
            return this;
        }

        public Build setTopMargin(int topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        public Build setRightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
            return this;
        }

        public Build setBottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
            return this;
        }

        public LinearLayout.LayoutParams linearParams() {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height, weight);
            params.gravity = gravity;
            params.topMargin = topMargin;
            params.bottomMargin = bottomMargin;
            params.leftMargin = leftMargin;
            params.rightMargin = rightMargin;

            return params;
        }

        public FrameLayout.LayoutParams frameParams() {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = gravity;
            params.topMargin = topMargin;
            params.bottomMargin = bottomMargin;
            params.leftMargin = leftMargin;
            params.rightMargin = rightMargin;

            return params;
        }

        public RelativeLayout.LayoutParams relativeParams() {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            params.topMargin = topMargin;
            params.bottomMargin = bottomMargin;
            params.leftMargin = leftMargin;
            params.rightMargin = rightMargin;

            return params;
        }

        public ViewGroup.LayoutParams groupParams() {
            return new ViewGroup.LayoutParams(width, height);
        }
    }
}
