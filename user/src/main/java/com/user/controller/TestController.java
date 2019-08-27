package com.user.controller;

import com.core.domain.TestPara;
import com.core.util.request.SystemRequestParam;
import com.core.util.response.BaseResponse;
import com.user.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private UserService service;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public BaseResponse get(@RequestBody SystemRequestParam<TestPara> requestParam) {
        BaseResponse info = service.getInfo(requestParam);
        return BaseResponse.success(info);
    }
}
