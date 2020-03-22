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

package com.sky.xposed.rimet.task;

import android.os.AsyncTask;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.ui.base.BaseView;

/**
 * Created by sky on 2019-07-26.
 *
 * 对异常任务进行封装的任务类
 *
 * 回调写成两个主要是为了可以方便使用lambda表达式
 */
public abstract class AbstractTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, AbstractTask.XResult<Result>> {

    private CompleteCallback<Result> mCompleteCallback;
    private ThrowableCallback mThrowableCallback;
    private BaseView mBaseView;

    public CompleteCallback<Result> getCompleteCallback() {
        return mCompleteCallback;
    }

    public void setCompleteCallback(CompleteCallback<Result> completeCallback) {
        mCompleteCallback = completeCallback;
    }

    public ThrowableCallback getThrowableCallback() {
        return mThrowableCallback;
    }

    public void setThrowableCallback(ThrowableCallback throwableCallback) {
        mThrowableCallback = throwableCallback;
    }

    public void setBaseView(BaseView baseView) {
        mBaseView = baseView;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mBaseView != null) {
            mBaseView.showLoading();
        }
    }

    /**
     * 处理需要处理的信息(这个是异步的)
     * @param params
     * @return
     * @throws Exception
     */
    protected abstract Result onHandler(Params... params) throws Exception;

    @SafeVarargs
    @Override
    protected final XResult<Result> doInBackground(Params... params) {
        try {
            // 开始处理
            return new XResult<>(onHandler(params));
        } catch (Throwable tr) {
            return new XResult<>(null, tr);
        }
    }

    @Override
    protected void onPostExecute(XResult<Result> xResult) {
        super.onPostExecute(xResult);

        if (mBaseView != null) {
            mBaseView.cancelLoading();
            mBaseView = null;
        }

        if (xResult.isThrowable()) {
            // 处理异常
            if (!onThrowable(xResult.getThrowable())) {
                callThrowableCallback(xResult.getThrowable());
            }
            return;
        }

        // 处理返回结果
        if (!onComplete(xResult.getResult())) {
            callCompleteCallback(xResult.getResult());
        }
    }

    /**
     * 处理返回的结果(在主线程中)
     * @param result
     */
    protected boolean onComplete(Result result) {
        return false;
    }

    /**
     * 在主线程中
     * @param tr
     */
    protected boolean onThrowable(Throwable tr) {
        Alog.e("任务处理异常", tr);
        return false;
    }

    /**
     * 调用完成的回调接口
     * @param result
     */
    protected void callCompleteCallback(Result result) {
        if (mCompleteCallback != null) mCompleteCallback.onComplete(result);
    }

    /**
     * 调用异常的回调接口
     * @param tr
     */
    protected void callThrowableCallback(Throwable tr) {
        if (mThrowableCallback != null) mThrowableCallback.onThrowable(tr);
    }

    /**
     * 处理完成回调
     */
    public interface CompleteCallback<Result> {

        void onComplete(Result result);
    }

    /**
     * 处理异常回调
     */
    public interface ThrowableCallback {

        void onThrowable(Throwable tr);
    }


    /**
     * 封装了异常处理
     * @param <Result>
     */
    public final static class XResult<Result> {

        private Result mResult;
        private Throwable mThrowable;

        private XResult(Result result) {
            mResult = result;
        }

        private XResult(Result result, Throwable throwable) {
            mResult = result;
            mThrowable = throwable;
        }

        public Result getResult() {
            return mResult;
        }

        public Throwable getThrowable() {
            return mThrowable;
        }

        public boolean isThrowable() {
            return mThrowable != null;
        }
    }
}
