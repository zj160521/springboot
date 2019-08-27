package com.core.util.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 共识快娱统一请求内容对象
 */
@ApiModel(value="统一请求内容对象",description="统一请求内容对象")
public class SystemRequestBody<T> implements Serializable {

    /**
     * 请求body
     */
    @ApiModelProperty(value = "请求body", name = "data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
