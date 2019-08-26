package com.user.util;

import java.util.UUID;

/**
 * ID工具类
 */
public final class IDGenerator {
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toLowerCase();
    }
}
