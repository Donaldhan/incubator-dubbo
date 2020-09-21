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
 * RegistryService. (SPI, Prototype, ThreadSafe)
 * 注册服务
 * @see org.apache.dubbo.registry.Registry
 * @see org.apache.dubbo.registry.RegistryFactory#getRegistry(URL)
 */
public interface RegistryService {

    /**
     * Register data, such as : provider service, consumer address, route rule, override rule and other data.
     * 注册数据，比如：服务提供者，消费地址，路由规则
     * <p>
     * Registering is required to support the contract:<br>
     *     注册需要遵循如下约定：
     * 1. When the URL sets the check=false parameter. When the registration fails,
     * the exception is not thrown and retried in the background. Otherwise, the exception will be thrown.<br>
     *     当Url设置check=false参数，当注册失败时，不会抛出异常，并在后端重新尝试，否则异常将会抛出
     * 2. When URL sets the dynamic=false parameter, it needs to be stored persistently,
     * otherwise, it should be deleted automatically when the registrant has an abnormal exit.<br>
     *     当url设置dynamic=false，则需要持久化存储，否则，当注册者非正常退出是，将会自动删除。
     * 3. When the URL sets category=routers, it means classified storage,
     * the default category is providers, and the data can be notified by the classified section. <br>
     *     当url设置category=routers时，意味着分类存储，默认为提供者，在分类节点，将会通知数据变更
     * 4. When the registry is restarted, network jitter, data can not be lost,
     * including automatically deleting data from the broken line.<br>
     *     当注册器重新启动，网络抖动，数据将不会丢失，包括从断点处，自动删除的数据
     * 5. Allow URLs which have the same URL but different parameters to coexist,they can't cover each other.<br>
     *    允许相同url不同的参数共存的场景，不会相互覆盖
     * @param url  Registration information , is not allowed to be empty, e.g: dubbo://10.20.153.10/org.apache.dubbo.foo.BarService?version=1.0.0&application=kylin
     */
    void register(URL url);

    /**
     * Unregister
     * <p>
     * Unregistering is required to support the contract:<br>
     *     注销，需要遵循以下契约：
     * 1. If it is the persistent stored data of dynamic=false, the registration data can not be found,
     * then the IllegalStateException is thrown, otherwise it is ignored.<br>
     *     如果为持久化存储，注册数据将不能被发现时，将会排除非法状态异常，否则忽略
     * 2. Unregister according to the full url match.<br>
     *     根据全路径匹配注销
     * @param url Registration information , is not allowed to be empty,
     *           e.g: dubbo://10.20.153.10/org.apache.dubbo.foo.BarService?version=1.0.0&application=kylin
     */
    void unregister(URL url);

    /**
     * Subscribe to eligible registered data and automatically push when the registered data is changed.
     * 订阅合格的注册数据，当注册数据变更是，自动推送
     * <p>
     * Subscribing need to support contracts:<br>
     *     订阅需要支持一下约定
     * 1. When the URL sets the check=false parameter. When the registration fails, the exception is not thrown and retried in the background. <br>
     *      当Url设置check=false参数，当注销失败时，不会抛出异常，并在后端重新尝试
     * 2. When URL sets category=routers, it only notifies the specified classification data.
     * Multiple classifications are separated by commas, and allows asterisk to match, which indicates that all categorical data are subscribed.<br>
     *     当url设置category=routers时，将会通知特定分类的数据。多分类将会以逗号隔开，允许星号匹配，这意味着订阅所有分类的数据。
     * 3. Allow interface, group, version, and classifier as a conditional query,
     * e.g.: interface=org.apache.dubbo.foo.BarService&version=1.0.0<br>
     *     允许接口，分组，版本，分类作为查询条件
     * 4. And the query conditions allow the asterisk to be matched, subscribe to all versions of all the packets of all interfaces,
     * e.g. :interface=*&group=*&version=*&classifier=*<br>
     *     查询条件允许型号匹配，订阅所有包中的所有版本接口
     * 5. When the registry is restarted and network jitter, it is necessary to automatically restore the subscription request.<br>
     *     当注册器重启时，需要重新自动存储订阅请求
     * 6. Allow URLs which have the same URL but different parameters to coexist,they can't cover each other.<br>
     *     允许相同url不同的参数共存的场景，不会相互覆盖
     * 7. The subscription process must be blocked, when the first notice is finished and then returned.<br>
     *     在一个通知完成，订阅进程必须被阻塞，然后返回
     *
     * @param url      Subscription condition, not allowed to be empty,
     *                e.g. consumer://10.20.153.10/org.apache.dubbo.foo.BarService?version=1.0.0&application=kylin
     * @param listener A listener of the change event, not allowed to be empty
     */
    void subscribe(URL url, NotifyListener listener);

    /**
     * Unsubscribe
     * <p>
     * Unsubscribing is required to support the contract:<br>
     *     取消订阅需要支持一下约定
     * 1. If don't subscribe, ignore it directly.<br>
     *     如果不需要订阅，则直接忽略
     * 2. Unsubscribe by full URL match.<br>
     *    根据全路径匹配取消订阅
     * @param url      Subscription condition, not allowed to be empty,
     *                 e.g. consumer://10.20.153.10/org.apache.dubbo.foo.BarService?version=1.0.0&application=kylin
     * @param listener A listener of the change event, not allowed to be empty
     */
    void unsubscribe(URL url, NotifyListener listener);

    /**
     * Query the registered data that matches the conditions.
     * Corresponding to the push mode of the subscription, this is the pull mode and returns only one result.
     * 查询匹配给定条件的注册数据。对应于订阅的推模式，此为拉模式，并返回一个结果
     * @param url Query condition, is not allowed to be empty, e.g.
     *            consumer://10.20.153.10/org.apache.dubbo.foo.BarService?version=1.0.0&application=kylin
     * @return The registered information list, which may be empty,
     * the meaning is the same as the parameters of {@link org.apache.dubbo.registry.NotifyListener#notify(List<URL>)}.
     * @see org.apache.dubbo.registry.NotifyListener#notify(List)
     */
    List<URL> lookup(URL url);

}