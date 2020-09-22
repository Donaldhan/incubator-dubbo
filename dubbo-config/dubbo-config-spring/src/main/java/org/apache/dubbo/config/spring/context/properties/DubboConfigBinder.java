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
package org.apache.dubbo.config.spring.context.properties;

import org.apache.dubbo.config.AbstractConfig;

import org.springframework.context.EnvironmentAware;

/**
 * {@link AbstractConfig DubboConfig} Binder
 * dubbo 配置绑定器
 * @see AbstractConfig
 * @see EnvironmentAware
 * @since 2.5.11
 */
public interface DubboConfigBinder extends EnvironmentAware {

    /**
     * Set whether to ignore unknown fields, that is, whether to ignore bind
     * parameters that do not have corresponding fields in the target object.
     * <p>Default is "true". Turn this off to enforce that all bind parameters
     * must have a matching field in the target object.
     * 设置是否忽略未知的字段，也就是说，是否忽略那些在目标对象，没有对应字段的绑定参数。
     * 默认为true，打开开关，强制所有绑定的参数必须匹配目标对象中字段
     * @see #bind
     */
    void setIgnoreUnknownFields(boolean ignoreUnknownFields);

    /**
     * Set whether to ignore invalid fields, that is, whether to ignore bind
     * parameters that have corresponding fields in the target object which are
     * not accessible (for example because of null values in the nested path).
     * <p>Default is "false".
     * 设置是否忽略无效的字段，也就是说，是否忽略那些在目标对象，没有对应字段的绑定参数，这些字段是不可访问，
     * 比如为null
     * @see #bind
     */
    void setIgnoreInvalidFields(boolean ignoreInvalidFields);

    /**
     * Bind the properties to Dubbo Config Object under specified prefix.
     * 绑定基于给定前缀的dubbo 配置对象的属性
     * @param prefix
     * @param dubboConfig
     */
    <C extends AbstractConfig> void bind(String prefix, C dubboConfig);
}
