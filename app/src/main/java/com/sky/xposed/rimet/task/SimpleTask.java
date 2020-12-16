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

/**
 * Created by sky on 2019-10-25.
 */
public class SimpleTask<Params, Result> extends AbstractTask<Params, Void, Result> {

    private OnHandlerCallback<Params, Result> mHandlerCallback;

    public OnHandlerCallback<Params, Result> getHandlerCallback() {
        return mHandlerCallback;
    }

    public void setHandlerCallback(OnHandlerCallback<Params, Result> handlerCallback) {
        mHandlerCallback = handlerCallback;
    }

    @Override
    protected Result onHandler(Params... params) throws Exception {
        return mHandlerCallback.onExec(params != null && params.length == 1 ? params[0] : null);
    }


    /**
     * 创建任务
     * @param callback
     * @param <Params>
     * @param <Result>
     * @return
     */
    public static <Params, Result> SimpleTask<Params, Result> createTask(OnHandlerCallback<Params, Result> callback) {

        SimpleTask<Params, Result> task = new SimpleTask<>();
        task.setHandlerCallback(callback);

        return task;
    }

    /**
     * 处理回调接口
     * @param <Params>
     * @param <Result>
     */
    public interface OnHandlerCallback<Params, Result> {

        Result onExec(Params param) throws Exception;
    }
}
