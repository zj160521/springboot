package com.module1.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/6/21 16:17
 */
@RestController
@RequestMapping("/test")
@Api(value="测试接口",description = "测试接口")
public class TestController {
    @RequestMapping(value = "/getMsg", method = RequestMethod.POST)
    public Object getMsg() {
        String msg = "测试消息！";
        System.out.println(msg);
        return msg;
    }
}
