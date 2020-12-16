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

package tk.anysoft.xposed.lark.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import tk.anysoft.xposed.lark.base.BaseView;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.TitleView;
import com.sky.xposed.common.util.ToastUtil;
import tk.anysoft.xposed.lark.Constant;
import tk.anysoft.xposed.lark.R;
import tk.anysoft.xposed.lark.ui.base.BaseDialog;
import tk.anysoft.xposed.lark.ui.util.UriUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by sky on 2019/3/13.
 */
public abstract class CommonDialog extends BaseDialog implements BaseView {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 初始化View
        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();
        mToolbar.setBackgroundColor(Constant.Color.TOOLBAR);

        // 创建相关的View
        createView(mCommonFrameLayout);

        return mCommonFrameLayout;
    }

    public TitleView getTitleView() {
        return mToolbar;
    }

    public CommonFrameLayout getCommonFrameLayout() {
        return mCommonFrameLayout;
    }

    public abstract void createView(CommonFrameLayout frameView);

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.showBack();
        // 设置监听
        mToolbar.setOnBackEventListener(view1 -> onCloseDialog());

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_clear))
                .into(getTitleView().getBackView());
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    /**
     * 关闭Dialog
     */
    public void onCloseDialog() {
        // 关闭界面
        dismiss();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void cancelLoading() {

    }

    @Override
    public void showMessage(String msg) {
        ToastUtil.show(msg);
    }
}
