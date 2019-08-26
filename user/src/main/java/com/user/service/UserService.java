package com.user.service;

import com.user.feign.IUserFeignClient;
import com.user.util.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/8/26 11:34
 */
@Service
public class UserService {
    @Autowired
    private IUserFeignClient userFeignClient;

    public BaseResponse getInfo() {
        return userFeignClient.get();
    }
}
