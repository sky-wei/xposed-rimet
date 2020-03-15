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

package com.sky.xposed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface APlugin {

    /**
     * 起始版本号(-1时不做判断)
     * @return
     */
    int begin() default -1;

    /**
     * 结束版本号(-1时不做判断)
     * @return
     */
    int end() default -1;

    /**
     * 进程过滤(默认只处理主进程)
     * @return
     */
    int[] filter() default { Process.MAIN };

    /**
     * Hook的包名
     * @return
     */
    String packageName() default "";

    /**
     * 优先级
     * @return
     */
    int priority() default 0;

    /**
     * 是否Debug测试的
     * @return
     */
    boolean debug() default false;


    /**
     * 进程
     */
    interface Process {

        int ALL = 0x00;

        int MAIN = 0x01;

        int OTHER = 0x02;
    }
}
