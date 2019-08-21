package com.web.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/6/21 17:56
 */
@Table(name = "gm_test")
@ApiModel(value = "测试类")
public class TestDO extends BaseDO{
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "日志时间")
    private LocalDateTime logTime;

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
