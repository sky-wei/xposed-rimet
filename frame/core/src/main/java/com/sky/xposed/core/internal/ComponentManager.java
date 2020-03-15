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

package com.sky.xposed.core.internal;

import com.sky.xposed.core.interfaces.XComponent;
import com.sky.xposed.core.interfaces.XComponentManager;
import com.sky.xposed.core.interfaces.XCoreManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sky on 2020-01-11.
 */
final class ComponentManager implements XComponentManager {

    private Map<Class<? extends XComponent>, XComponent> mComponentMap = new HashMap<>();
    private List<ComponentListener> mListeners = new ArrayList<>();

    private final XCoreManager mCoreManager;
    private final XComponentManager.Factory mFactory;

    private ComponentManager(Build build) {
        mCoreManager = build.mCoreManager;
        mFactory = build.mFactory;
    }

    @Override
    public <T extends XComponent> T get(Class<T> name) {

        XComponent component = mComponentMap.get(name);

        if (component == null) {
            synchronized (this) {
                component = mComponentMap.get(name);
                if (component == null) {
                    // 创建组件
                    component = mFactory.create(mCoreManager, name);

                    if (component != null) {
                        // 添加并通知
                        component.initialize();
                        mComponentMap.put(name, component);
                        callCreateListener(component);
                    }
                }
            }
        }
        return name.cast(component);
    }

    @Override
    public List<XComponent> getList() {
        return new ArrayList<>(mComponentMap.values());
    }

    @Override
    public void remove(Class<? extends XComponent> name) {

        XComponent component = mComponentMap.get(name);

        if (component != null) {
            // 先释放再通知
            component.release();
            callReleaseListener(component);
        }
    }

    @Override
    public boolean contains(Class<? extends XComponent> name) {
        return mComponentMap.containsKey(name);
    }

    @Override
    public void release() {

        for (XComponent component : getList()) {
            // 先释放再通知
            component.release();
            callReleaseListener(component);
        }
        mComponentMap.clear();
    }

    @Override
    public void addListener(ComponentListener listener) {
        if (listener != null) mListeners.add(listener);
    }

    @Override
    public void removeListener(ComponentListener listener) {
        if (listener != null) mListeners.remove(listener);
    }

    private void callCreateListener(XComponent component) {
        for (ComponentListener listener : mListeners) {
            listener.onCreate(component.getClass());
        }
    }

    private void callReleaseListener(XComponent component) {
        for (ComponentListener listener : mListeners) {
            listener.onRelease(component.getClass());
        }
    }


    static class Build {

        private XCoreManager mCoreManager;
        private XComponentManager.Factory mFactory;

        Build(XCoreManager coreManager) {
            mCoreManager = coreManager;
        }

        Build setFactory(Factory factory) {
            mFactory = factory;
            return this;
        }

        XComponentManager build() {
            return new ComponentManager(this);
        }
    }
}
