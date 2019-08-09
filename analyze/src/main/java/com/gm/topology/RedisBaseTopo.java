package com.gm.topology;

import com.gm.util.PropertyUtil;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisBaseTopo {
    private static JedisPool jedisPool;
    public static synchronized JedisPool getJedisPool() {
        if(jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(1000 * 100);
            config.setMaxWaitMillis(30);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            jedisPool = new JedisPool(config, PropertyUtil.host,
                    Integer.parseInt(PropertyUtil.redis_port),
                    2000,
                    PropertyUtil.redis_pwd);
        }
        return jedisPool;
    }
}
