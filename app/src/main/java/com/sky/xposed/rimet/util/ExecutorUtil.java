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

package com.sky.xposed.rimet.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by sky on 2019/1/22.
 */
public final class ExecutorUtil {

    public static final Executor MESSAGE_EXECUTOR;
    public static final Executor BACK_EXECUTOR;

    static {
        // 初始化单个线程池
        MESSAGE_EXECUTOR = Executors.newSingleThreadExecutor();

        // 后台的线程池(暂时写两个)
        BACK_EXECUTOR = Executors.newScheduledThreadPool(3);
    }

    private ExecutorUtil() {
    }

    /**
     * 一个单个的消息线程执行服务
     * @return
     */
    public static Executor getSingleMessageExecutor() {
        return MESSAGE_EXECUTOR;
    }

    /**
     * 一个后台线程执行服务
     * @return
     */
    public static Executor getBackExecutor() {
        return BACK_EXECUTOR;
    }

    /**
     * 执行后台任务
     * @param runnable
     */
    public void executeBack(Runnable runnable) {
        getBackExecutor().execute(runnable);
    }

    public void executeSingle(Runnable runnable) {
        getSingleMessageExecutor().execute(runnable);
    }
}
