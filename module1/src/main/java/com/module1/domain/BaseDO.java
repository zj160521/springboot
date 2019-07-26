package com.module1.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/7/4 16:42
 */
@ApiModel(value = "数据库对象基类")
public class BaseDO {
    @Id
    @ApiModelProperty(value = "id")
    private String id;
}
