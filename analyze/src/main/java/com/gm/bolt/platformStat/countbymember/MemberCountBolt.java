package com.gm.bolt.platformStat.countbymember;

import com.gm.bolt.platformStat.PlatformBaseBolt;
import com.gm.domain.platform.PlatformStat;
import com.gm.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.storm.tuple.Tuple;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MemberCountBolt extends PlatformBaseBolt {

    @Override
    public void execute(Tuple input) {
        int isSuccess = getData(input);
        if (isSuccess == 1)
            return;
        String day = df.format(time).substring(0,10);
        // 查询条件
        Bson query = Filters.and(Filters.eq("memberName", userId),
                Filters.eq("platformCode", platformCode),
                Filters.eq("statTime", day));
        FindIterable findIterable = memberPlatformCollection.find(query);
        MongoCursor cursor = findIterable.iterator();
        if (!cursor.hasNext()) {
            memberPlatformCollection.insertOne(new Document().append("memberName",userId).append("platformCode", platformCode).append("statTime", day));
            Document document = new Document();
            countValue(document, memberPlatformCollection, query);
        } else {
            Document document = (Document)cursor.next();
            countValue(document, memberPlatformCollection, query);
        }
        collector.ack(input);
    }

    public void countValue (Document document, MongoCollection collection, Bson query) {

        PlatformStat platformStat = new PlatformStat();
        Long bet = document.getLong("betAmount");
        Long gain = document.getLong("gainAmount");
        if (bet == null)
            bet = 0L;
        if (gain == null)
            gain = 0L;
        platformStat.setBetAmount(bet + betAmount);
        platformStat.setGainAmount(gain + gainAmount);
        collection.updateOne(query,
                new Document("$set", new Document("betAmount", platformStat.getBetAmount()).
                                append("gainAmount", platformStat.getGainAmount())));
    }
}
