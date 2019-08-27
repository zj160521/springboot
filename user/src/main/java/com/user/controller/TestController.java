package com.user.controller;

import com.user.service.UserService;
import com.user.util.response.BaseResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/6/21 16:17
 */
@RestController
@RequestMapping("/user")
@Api(value="测试接口",description = "测试接口")
public class TestController {
    @Autowired
    private UserService service;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public BaseResponse get() {
        BaseResponse info = service.getInfo();
        return BaseResponse.success(info);
    }
}