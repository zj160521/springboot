package com.gm.bolt.financestatement.countbymember;

import com.gm.bolt.financestatement.FinanceBaseBolt;
import com.gm.bolt.financestatement.countbytime.HourBaseBolt;
import com.gm.topology.RedisBaseTopo;
import com.gm.util.KafkaProducerProperty;
import com.gm.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.bson.Document;
import org.bson.types.Decimal128;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.Map;

public class MemberBaseBolt extends FinanceBaseBolt {

    protected final String deposite = "deposit";//存款
    protected final String depositeTimes = "deposit_times";//存款次数
    protected final String drawMoney = "draw_money";//取款
    protected final String drawMoneyTimes = "draw_money_times";//取款次数
    protected final String firstDepositeTime = "first_deposit_time";//首存时间
    protected final String firstDrawMoneyTime = "first_draw_money_time";//首提时间
    protected final String lastDepositeTime = "last_deposit_time";//末存时间
    protected final String lastDrawMoneyTime = "last_draw_money_time";//末提时间
    protected final String bonusTimes = "bonus_times";//红利次数
    protected final String discountsTimes = "discounts_times";//优惠次数
    protected final String bonus = "bonus";//红利总额
    protected final String discounts = "discounts";//优惠总额
    protected final String mongoTableName = "member_stat";
    protected final String firstDepositeTimes = "first";
    protected final String dayPeopleDepositeTimes = "bodyCount";
    protected Jedis jedis;
    protected MongoCollection<Document> collection;
    protected KafkaProducer producer;
    protected final String depositTopic = "depositTotal";

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        this.producer = new KafkaProducer(KafkaProducerProperty.getProducerProps());
        this.collector = collector;
        pool = RedisBaseTopo.getJedisPool();
        collection = MongoUtil.getConnect().getCollection(mongoTableName);
    }

    protected MongoCursor getByMemberID(String memberId){
        FindIterable findIterable = collection.find(Filters.eq("member_id",memberId));
        return findIterable.iterator();
    }

    protected BigDecimal setMongoData(String field1,BigDecimal countValue){
        MongoCursor cursor = getByMemberID(memberId);
        if (cursor.hasNext()) {
            Document document = (Document)cursor.next();
            Decimal128 sum = document.get(field1, Decimal128.class);
            if (sum == null) sum = new Decimal128(0);
            BigDecimal count = sum.bigDecimalValue().add(countValue);
            collection.updateOne(Filters.eq("member_id",memberId),
                    new Document("$set",new Document(field1,count)));
            return count;
        }
        return new BigDecimal("0");
    }

    protected void setFirstTime(String field,String timeValue, Integer type){
        MongoCursor cursor = getByMemberID(memberId);
        if (cursor.hasNext()) {
            Document document = (Document)cursor.next();
            String time = document.get(field, String.class);
            if (time == null) {
                if (type == 1) {
                    jedis = pool.getResource();
                    try {//Redis记录首存次数
                        String dailyKey = HourBaseBolt.deposite.concat("_").concat(timeValue.substring(0,10));
                        String monthKey = HourBaseBolt.deposite.concat("_").concat(timeValue.substring(0,7));
                        String yearKey = HourBaseBolt.deposite.concat("_").concat(timeValue.substring(0,4));
                        saveDateInRedis(dailyKey);
                        saveDateInRedis(monthKey);
                        saveDateInRedis(yearKey);
                    } finally {
                        jedis.close();
                    }
                    //记录首存金额
                    Decimal128 firstDepositMoney = document.get("first_deposit", Decimal128.class);
                    if (firstDepositMoney == null) {
                        collection.updateOne(Filters.eq("member_id", memberId),
                                new Document("$set", new Document("first_deposit", money)));
                    }
                }
                //记录首存时间
                collection.updateOne(Filters.eq("member_id", memberId),
                        new Document("$set", new Document(field, timeValue)));
            }
        }
    }

    protected void setLastTime(String field,String timeValue){
        collection.updateOne(Filters.eq("member_id", memberId),
                        new Document("$set", new Document(field, timeValue)));
    }

    private void saveDateInRedis(String key) {
        String firstTimes = jedis.hget(key, firstDepositeTimes);
        if (firstTimes == null) {
            jedis.hset(key, firstDepositeTimes, new Integer(1).toString());
        } else {
            jedis.hset(key, firstDepositeTimes, (Integer.parseInt(firstTimes) + 1)+"");
        }
    }
}
