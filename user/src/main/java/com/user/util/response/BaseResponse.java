/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Create Date : 2017-3-16
 */

package com.user.util.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 通用响应实现类
 * @author Wu Ren Jun
 * @param <T>
 * @version 1.0
 */
@ApiModel(value="平台统一响应对象",description="平台统一响应对象")
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应消息
     */
    @ApiModelProperty(value="响应消息,",name="account",required=true)
    private String message;

    /**
     * 响应状态码
     */
    @ApiModelProperty(value="响应状态码,状态码2000表示业务处理成功.其他表示失败,根据返回message提示用户",name="code",required=true)
    private int code;

    /**
     * 响应具体业务对象
     */
    @ApiModelProperty(value="响应具体业务对象",name="data",required=true)
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse() {
        super();
    }

    public BaseResponse(String message) {
        this.message = message;
    }

    public BaseResponse(T data) {
        this(ResponseMessage.SUCCESS.getCode(), ResponseMessage.SUCCESS.getMessage(), data);
    }


    public BaseResponse(final int code, T data) {
        this(ResponseMessage.SUCCESS.getMessage());
        this.code = code;
        this.data = data;
    }

    public BaseResponse(final int code, String message, T data) {
        this(code, data);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 创建具备默认成功状态码和信息的全量响应对象
     *
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return successByCode(ResponseMessage.SUCCESS.getCode(), data);
    }
    
    public static BaseResponse success() {
        return successByCode(ResponseMessage.SUCCESS.getCode(), null);
    }

    /**
     * 创建具备自定义成功状态码和默认信息的全量响应对象
     *
     * @param code
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> successByCode(int code, T data) {
        return success(code, ResponseMessage.SUCCESS.getMessage(), data);
    }

    /**
     * 创建具备默认成功状态码和自定义信息的全量响应对象
     *
     * @param message
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> successByMessage(String message, T data) {
        return success(ResponseMessage.SUCCESS.getCode(), message, data);
    }

    /**
     * 创建具备自定义成功状态码和信息的全量响应对象
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> success(int code, String message, T data) {
        return new BaseResponse<T>(code, message, data);
    }

    /**
     * 创建具备自定义失败状态码和默认信息的全量响应对象
     *
     * @param code
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> failedByCode(int code, T data) {
        return failed(code, ResponseMessage.FAILURE.getMessage(), data);
    }

    /**
     * 创建具备默认失败状态码和自定义信息的全量响应对象
     *
     * @param message
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> failedByMessage(String message, T data) {
        return failed(ResponseMessage.FAILURE.getCode(), message, data);
    }

    /**
     * 创建具备自定义失败状态码和信息的全量响应对象
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> failed(int code, String message, T data) {
        return new BaseResponse<T>(code, message, data);
    }

    /**
     * 创建具备自定义失败状态码和信息的全量响应对象
     *
     * @param code
     * @param message
     * @return
     */
    public static <T> BaseResponse<T> failed(int code, String message) {
        return new BaseResponse<T>(code, message, null);
    }

    /**
     * 创建具备自定义失败状态码和信息的全量响应对象
     *
     * @return
     */
    public static <T> BaseResponse<T> failed() {
        return new BaseResponse<T>(ResponseMessage.FAILURE.getCode(), ResponseMessage.FAILURE.getMessage(), null);
    }

    /**
     * 创建具备自定义失败状态码和信息的全量响应对象
     *
     * @param message
     * @return
     */
    public static <T> BaseResponse<T> failed(String message) {
        return new BaseResponse<T>(ResponseMessage.FAILURE.getCode(), message, null);
    }

    /**
     * 创建具备默认异常状态码和信息的全量响应对象
     *
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> exception(T data) {
        return exceptionByCode(ResponseMessage.EXCEPTION.getCode(), data);
    }

    /**
     * 创建具备自定义异常状态码和默认信息的全量响应对象
     *
     * @param code
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> exceptionByCode(int code, T data) {
        return exception(code, ResponseMessage.EXCEPTION.getMessage(), data);
    }

    /**
     * 创建具备默认异常状态码和自定义信息的全量响应对象
     *
     * @param message
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> exceptionByMessage(String message, T data) {
        return exception(ResponseMessage.EXCEPTION.getCode(), message, data);
    }

    /**
     * 创建具备自定义异常状态码和信息的全量响应对象
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> exception(int code, String message, T data) {
        return new BaseResponse<T>(code, message, data);
    }

}
