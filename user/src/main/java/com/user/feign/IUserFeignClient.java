package com.user.feign;

import com.core.util.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/8/26 11:28
 */
@FeignClient("web-service")
public interface IUserFeignClient {
    @PostMapping(value = "/test/get")
    BaseResponse get();
}
