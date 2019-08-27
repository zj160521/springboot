package com.core.util.constant;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "客户端类型")
public interface ClientType {
    int H5 = 1;
    int ADMIN = 2;
    int ANDROID = 3;
    int IOS = 4;
    int SYSTEM =5;
    int PC = 6;
}
