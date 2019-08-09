package com.gm.bolt.platformStat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gm.topology.RedisBaseTopo;
import com.gm.util.MongoUtil;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class PlatformBaseBolt extends BaseRichBolt {

    protected JedisPool pool;
    protected OutputCollector collector;
    protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected String number;
    protected Long betAmount;
    protected Long gainAmount;
    protected String game;
    protected String userId;//用户名
    protected Date time;
    protected String platformCode;//平台Code
    protected final String tableName = "platform_stat";
    protected final String memberTableName = "member_platform_stat";
    protected MongoCollection memberPlatformCollection;
    protected MongoCollection platformCollection;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        pool = RedisBaseTopo.getJedisPool();
        memberPlatformCollection = MongoUtil.getConnect().getCollection(memberTableName);
        platformCollection = MongoUtil.getConnect().getCollection(tableName);
    }

    @Override
    public void execute(Tuple input) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    protected int getData(Tuple input) {
        try {
            String msg = input.getString(0);
            JSONObject dataObj = JSON.parseObject(msg);
            JSONObject msgObj = JSON.parseObject(dataObj.getString("message"));
            number = msgObj.getString("number");
            betAmount = msgObj.getLong("betAmount");
            if (betAmount == null)
                betAmount = 0L;
            gainAmount = msgObj.getLong("gainAmount");
            if (gainAmount == null)
                gainAmount = 0L;
            game = msgObj.getString("game");
            Jedis redis = pool.getResource();
            try {
                userId = msgObj.getString("userId");
                //去掉平台用户名中的前缀
                Set<String> set = redis.keys("*_api");
                if (set != null && set.size() > 0) {
                    for (String str : set) {
                        String prefix = redis.hget(str, "prefix").replaceAll("\"", "");
                        if (!StringUtils.isEmpty(prefix) && userId.startsWith(prefix)) {
                            userId = userId.replaceFirst(prefix, "");
                            break;
                        }
                    }
                }
            } finally {
                redis.close();
            }
            Long timestamp = msgObj.getLong("timestamp");
            if (timestamp == null) timestamp = 0L;
            time = new Date(timestamp);
            platformCode = msgObj.getString("platformCode");
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}
