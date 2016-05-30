/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
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
package core;

/**
 * 错误码
 *
 */
public class ErrorEvent {
    static final public String PARAM_NULL = "PARAM_NULL"; // 参数为空
    static final public String PARAM_ILLEGAL = "PARAM_ILLEGAL"; // 参数不合法
    static final public String SEVER_ERROR = "SEVER_ERROR"; // 服务器问题
    static final public String SEVER_ILLEGAL = "SEVER_ILLEGAL"; // 服务器认为参数不合法
    static final public String NETWORK_ERROR = "NETWORK_ERROR"; // 网络问题


    static final public String NETWORK_ERROR_MSG = "网络异常"; // 网络问题 提示
    static final public String SEVER_ERROR_MSG = "服务器异常"; // 服务器问题 提示
}
