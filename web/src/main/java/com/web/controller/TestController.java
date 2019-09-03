package com.web.controller;

import com.core.domain.TestDO;
import com.core.domain.TestPara;
import com.core.util.IDGenerator;
import com.core.util.request.SystemRequestPageBean;
import com.core.util.request.SystemRequestParam;
import com.core.util.response.BaseResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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
    @ApiOperation(value = "获取测消息")
    public Object getMsg() {
        String msg = "测试消息！";
        System.out.println(msg);
        return msg;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "插入测试记录", notes = "参数传name(测试名称，必传)、logTime(记录时间)")
    public BaseResponse insert(@RequestBody SystemRequestParam<TestPara> requestParam) {
        TestDO testDO = new TestDO();
        testDO.setId(IDGenerator.uuid());
        testDO.setName(requestParam.getBody().getName());
        testDO.setLogTime(LocalDateTime.now());
        service.insert(testDO);
        return BaseResponse.success();
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ApiOperation(value = "获取测试记录", notes = "参数传name(测试名称，必传)、logTime(记录时间)")
    public BaseResponse<PageInfo> get(@RequestBody SystemRequestParam<TestPara> requestParam) {
        TestPara para = requestParam.getBody();
        SystemRequestPageBean pageBean = requestParam.getPageBean();
        PageHelper.startPage(pageBean.getPage(),pageBean.getPageSize());
        List<TestDO> testDOList = service.get();
        PageInfo pageInfo = new PageInfo(testDOList);
        return BaseResponse.success(pageInfo);
//        throw new BizException("asdfg");
    }
}
