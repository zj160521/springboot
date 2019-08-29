package com.user.feign;

import com.core.domain.TestPara;
import com.core.util.request.SystemRequestParam;
import com.core.util.response.BaseResponse;
import com.user.fallback.UserFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/8/26 11:28
 */
@FeignClient(value = "web-service", fallbackFactory = UserFallback.class)
public interface IUserFeignClient {
    @PostMapping(value = "/test/get")
    BaseResponse get(SystemRequestParam<TestPara> requestParam);
}
