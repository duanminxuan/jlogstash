/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtstack.jlogstash.filters;

import com.dtstack.jlogstash.callback.ClassLoaderCallBackMethod;

import java.util.Map;

/**
 * company: www.dtstack.com
 * author: toutian
 * create: 2019/5/29
 */
public class FilterProxy implements IBaseFilter {

    private IBaseFilter baseFilter;

    public FilterProxy(IBaseFilter baseFilter) {
        this.baseFilter = baseFilter;
    }

    @Override
    public void prepare() {
        try {
            ClassLoaderCallBackMethod.callbackAndReset(() -> {
                baseFilter.prepare();
                return null;
            }, baseFilter.getClass().getClassLoader(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map process(Map event) {
        try {
            return ClassLoaderCallBackMethod.callbackAndReset(() -> {
                return baseFilter.process(event);
            }, baseFilter.getClass().getClassLoader(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postProcess(Map event, boolean ifSuccess) {
        try {
            ClassLoaderCallBackMethod.callbackAndReset(() -> {
                baseFilter.postProcess(event, ifSuccess);
                return null;
            }, baseFilter.getClass().getClassLoader(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
