package com.core.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/8/29 10:28
 */
@Configuration
public class FeignDisableHystrixCfg {
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
