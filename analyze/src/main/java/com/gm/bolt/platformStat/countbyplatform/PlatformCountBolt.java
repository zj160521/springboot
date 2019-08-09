package com.gm.bolt.platformStat.countbyplatform;

import com.alibaba.fastjson.JSON;
import com.gm.bolt.platformStat.PlatformBaseBolt;
import com.gm.domain.platform.PlatformStat;
import com.gm.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.tuple.Tuple;
import org.bson.Document;
import org.bson.conversions.Bson;
import redis.clients.jedis.Jedis;


public class PlatformCountBolt extends PlatformBaseBolt {

    @Override
    public void execute(Tuple input) {
        int isSuccess = getData(input);
        if (isSuccess == 1)
            return;
        String year = df.format(time).substring(0,4);
        String month = df.format(time).substring(0,7);
        String day = df.format(time).substring(0,10);
        // 查询条件
        Bson query = Filters.and(Filters.eq("stat_id", platformCode), Filters.eq("year", year));
        FindIterable findIterable = platformCollection.find(query);
        MongoCursor cursor = findIterable.iterator();
        Document document;
        if (cursor.hasNext()) {
            document = (Document)cursor.next();
        } else {
            platformCollection.insertOne(new Document().append("stat_id",platformCode).append("year", year));
            document = new Document();
        }
        countValue(document, year, query, platformCollection);
        countValue(document, month, query, platformCollection);
        countValue(document, day, query, platformCollection);
        collector.ack(input);
    }

    public void countValue (Document document, String time, Bson query, MongoCollection collection) {
        String timeValue = document.get(time, String.class);
        PlatformStat platformStat;
        if (StringUtils.isEmpty(timeValue)) {
            platformStat = new PlatformStat.Builder()
                    .gainAmount(0)
                    .betAmount(0)
                    .memberCount(0)
                    .build();
        } else {
            platformStat = JSON.parseObject(timeValue, PlatformStat.class);
        }
        platformStat.setBetAmount(platformStat.getBetAmount() + betAmount);
        platformStat.setGainAmount(platformStat.getGainAmount() + gainAmount);
        // 平台活跃玩家数统计
        Jedis redis = pool.getResource();
        try {
            String key = "platform_member_bet_count:".concat(platformCode).concat("_").concat(time);
            String count = redis.hget(key, userId);
            if (StringUtils.isEmpty(count)) {
                platformStat.setMemberCount(platformStat.getMemberCount() + 1);
                redis.hset(key, userId, "1");
            }
        } finally {
            redis.close();
        }
        collection.updateOne(query, new Document("$set",new Document(time,JSON.toJSON(platformStat).toString())));
    }
}
