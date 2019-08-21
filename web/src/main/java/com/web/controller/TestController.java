package com.web.controller;

import com.web.domain.TestDO;
import com.web.service.TestService;
import com.web.util.IDGenerator;
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
@RequestMapping("/test")
@Api(value="测试接口",description = "测试接口")
public class TestController {
    @Autowired
    private TestService service;
    @RequestMapping(value = "/getMsg", method = RequestMethod.POST)
    public Object getMsg() {
        String msg = "测试消息！";
        System.out.println(msg);
        return msg;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Object insert() {
        TestDO testDO = new TestDO();
        testDO.setId(IDGenerator.uuid());
        testDO.setName("手动阀");
        service.insert(testDO);
        return "ok";
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Object get() {
        return service.get();
    }
}
