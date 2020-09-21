/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.registry;

import org.apache.dubbo.common.URL;

import java.util.List;

/**
 * NotifyListener. (API, Prototype, ThreadSafe)
 *
 * @see org.apache.dubbo.registry.RegistryService#subscribe(URL, NotifyListener)
 */
public interface NotifyListener {

    /**
     * Triggered when a service change notification is received.
     * 当接收服务变更通知时，触发
     * <p>
     * Notify needs to support the contract: <br>
     *
     * 1. Always notifications on the service interface and the dimension of the data type.
     * that is, won't notify part of the same type data belonging to one service. Users do not need to compare the results of the previous notification.<br>
     * 当服务接口和数据类型，将会通知。同一服务同一数据类型，不会通知。用户不需要比较先前的通知结果。
     * 2. The first notification at a subscription must be a full notification of all types of data of a service.<br>
     *     订阅的一个通知，必须是服务全数据类型的通知
     * 3. At the time of change, different types of data are allowed to be notified separately,
     * e.g.: providers, consumers, routers, overrides. It allows only one of these types to be notified,
     * but the data of this type must be full, not incremental.<br>
     *     随着时间个变更，不同的数据类型，允许分开通知
     *     比如：提供者，消费者，路由，重写。允许这些类型的通知，但数据类型是全量的，不是增量的。
     * 4. If a data type is empty, need to notify a empty protocol with category parameter identification of url data.<br>
     *     如果数据类型为空，需要通知一个url数据的分类参数识别协议
     * 5. The order of notifications to be guaranteed by the notifications(That is, the implementation of the registry).
     * Such as: single thread push, queue serialization, and version comparison.<br>
     *  通知的顺序，依赖注册器的实现，比如单线程推送，队列序列化，版本比较等
     * @param urls The list of registered information , is always not empty. The meaning is the same as the return value of {@link org.apache.dubbo.registry.RegistryService#lookup(URL)}.
     */
    void notify(List<URL> urls);

}