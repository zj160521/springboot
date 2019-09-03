package com.user.fallback;

import com.core.domain.TestPara;
import com.core.util.request.SystemRequestParam;
import com.core.util.response.BaseResponse;
import com.user.feign.IUserFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/8/29 10:21
 */
@Component
public class UserFallback implements FallbackFactory<IUserFeignClient> {
    @Override
    public IUserFeignClient create(Throwable throwable) {

        return new IUserFeignClient() {
            @Override
            public BaseResponse get(SystemRequestParam<TestPara> requestParam) {
                return BaseResponse.failed();
            }
        };
    }
}
