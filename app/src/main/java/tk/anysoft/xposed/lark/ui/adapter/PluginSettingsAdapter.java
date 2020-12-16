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

package tk.anysoft.xposed.lark.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.xposed.common.ui.base.BaseListAdapter;
import com.sky.xposed.common.ui.view.CommentItemView;
import com.sky.xposed.common.util.DisplayUtil;
import tk.anysoft.xposed.lark.plugin.interfaces.XPlugin;

/**
 * Created by sky on 2018/12/30.
 */
public class PluginSettingsAdapter extends BaseListAdapter<XPlugin.Info> {

    public PluginSettingsAdapter(Context context) {
        super(context);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {

        CommentItemView commentItemView = new CommentItemView(getContext());
        TextView textView = commentItemView.getContentView();
        textView.setTextSize(16);
        textView.setMinLines(1);
        textView.setMaxLines(1);
        textView.setHeight(DisplayUtil.dip2px(getContext(), 32));

        return commentItemView;
    }

    public ViewHolder<XPlugin.Info> onCreateViewHolder(View view, int viewType) {
        return new PluginListHolder(view, this);
    }

    private final class PluginListHolder extends ViewHolder<XPlugin.Info> {

        private CommentItemView mCommentItemView;

        public PluginListHolder(View itemView, BaseListAdapter<XPlugin.Info> baseListAdapter) {
            super(itemView, baseListAdapter);
        }

        public void onInitialize() {
            super.onInitialize();

            mCommentItemView = (CommentItemView) mItemView;
        }

        public void onBind(int position, int viewType) {

            XPlugin.Info item = getItem(position);

            mCommentItemView.setContent(item.getName());
        }
    }
}
