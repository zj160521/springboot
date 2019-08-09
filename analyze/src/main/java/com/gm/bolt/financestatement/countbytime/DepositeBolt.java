package com.gm.bolt.financestatement.countbytime;

import com.gm.domain.finance.IPaymentStatInfo;
import com.gm.domain.finance.PaymentStatement;
import com.gm.topology.RedisBaseTopo;
import com.gm.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Tuple;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepositeBolt extends HourBaseBolt {

    private MongoCollection<Document> collection;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        pool = RedisBaseTopo.getJedisPool();
        collection = MongoUtil.getConnect().getCollection(paymentMongoDBTableName);
    }

    @Override
    public void execute(Tuple input) {
        if (input.getInteger(1) == 1) {
            getData(input);
            String timeValue = time.substring(0,10);
            Bson baseQuery = Filters.and(Filters.eq(IPaymentStatInfo.paymentId,paymentId),
                    Filters.eq(IPaymentStatInfo.platformCode,platformCode));
            Bson dayValueQuery = Filters.and(baseQuery,
                    Filters.eq(IPaymentStatInfo.type,IPaymentStatInfo.dayType),
                    Filters.eq(IPaymentStatInfo.time,timeValue));
            Bson unpaidQuery = Filters.and(baseQuery,
                    Filters.eq(IPaymentStatInfo.type,IPaymentStatInfo.unpaidType));
            BigDecimal zero = new BigDecimal(0);
            if (state != null && state == 0) {
                //累计总存款请求次数
                countTotalRequestTimes(zero, dayValueQuery, unpaidQuery, timeValue);
            } else if (state != null && state == 1) {// 存款成功的数据处理
                jedis = pool.getResource();
                try {
                    //按日月年统计存款数据，存redis
                    countValue(input, 1, deposite);
                    setCountTimesData(dayKey, timesCount);
                    setCountTimesData(yearKey, timesCount);
                    setCountTimesData(monthKey, timesCount);
                    jedis.sadd(deposite.concat("_people_").concat(time.substring(0,7)),memberId);
                    jedis.sadd(deposite.concat("_people_").concat(time.substring(0,4)),memberId);
                    //成功存款的支付数据累计
                    successDeposit(zero, dayValueQuery, unpaidQuery, timeValue);
                } finally {
                    jedis.close();
                }
            }
            collector.ack(input);
        }
    }

    /**
     * 更新指定日统计数据
     * @param collection 表连接
     * @param dayValueQuery 日数据统计查询条件
     * @param ps 更新数据
     */
    public static void updateDayValue(MongoCollection<Document> collection, Bson dayValueQuery, PaymentStatement ps) {
        collection.updateOne(dayValueQuery,
                new Document("$set",
                        new Document(IPaymentStatInfo.deposit, ps.getDeposit())
                                .append(IPaymentStatInfo.paymentName, ps.getPaymentName())
                                .append(IPaymentStatInfo.serviceCharge, ps.getServiceCharge())
                                .append(IPaymentStatInfo.successCount, ps.getSuccessCount())
                                .append(IPaymentStatInfo.totalCount, ps.getTotalCount())));
    }

    /**
     * 统计总的存款请求次数
     * @param zero
     * @param dayValueQuery 日数据统计查询条件
     * @param unpaidQuery 未支付统计查询条件
     * @param timeValue date日期
     */
    private void countTotalRequestTimes(BigDecimal zero, Bson dayValueQuery, Bson unpaidQuery, String timeValue) {
        // 总存款次数累计
        MongoCursor dayValueCursor = getMongoCursor(collection, dayValueQuery);
        PaymentStatement ps;
        if (dayValueCursor.hasNext()) {
            Document document = (Document) dayValueCursor.next();
            // 只对总存款请求次数进行累加，其他统计值不变
            ps = new PaymentStatement.Builder()
                    .deposit(document.get(IPaymentStatInfo.deposit, Decimal128.class).bigDecimalValue())
                    .paymentName(payment)
                    .serviceCharge(document.get(IPaymentStatInfo.serviceCharge, Decimal128.class).bigDecimalValue())
                    .successCount(document.getInteger(IPaymentStatInfo.successCount))
                    .totalCount(document.getInteger(IPaymentStatInfo.totalCount) + 1)
                    .build();
        } else {
            insertDayValueDoc(timeValue);
            ps = setPaymentStatement(zero, payment, zero, 0, 1);
        }
        updateDayValue(collection, dayValueQuery, ps);
        // 支付线路未支付订单累计
        MongoCursor unpaidCursor = getMongoCursor(collection, unpaidQuery);
        Integer unpaid; // 未支付订单变量
        if (unpaidCursor.hasNext()) {
            Document document = (Document) unpaidCursor.next();
            unpaid = document.getInteger(IPaymentStatInfo.unpaidType) + 1;
            String unpaidTime = document.getString(IPaymentStatInfo.time);
            if (StringUtils.isEmpty(unpaidTime)) {// 没有未支付的首单时间，更新为此次订单时间
                unpaidTime = this.time;
            }
            collection.updateOne(unpaidQuery, new Document("$set",
                    new Document(IPaymentStatInfo.unpaidType, unpaid)
                            .append(IPaymentStatInfo.time, unpaidTime)));
        } else {
            insertUnpaidDoc(1, this.time);
        }

    }

    /**
     * 日数据统
     * @param zero
     * @param dayValueQuery 日数据统计查询条件
     * @param unpaidQuery 未支付统计查询条件
     * @param timeValue date日期
     */
    private void successDeposit(BigDecimal zero, Bson dayValueQuery, Bson unpaidQuery, String timeValue) {
        MongoCursor cursor = getMongoCursor(collection, dayValueQuery);
        PaymentStatement ps;
        if (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            ps = new PaymentStatement.Builder()
                    .deposit(money.add(document.get(IPaymentStatInfo.deposit, Decimal128.class).bigDecimalValue()))
                    .paymentName(payment)
                    .serviceCharge(document.get(IPaymentStatInfo.serviceCharge, Decimal128.class).bigDecimalValue())
                    .successCount(document.getInteger(IPaymentStatInfo.successCount) + 1)
                    .totalCount(document.getInteger(IPaymentStatInfo.totalCount))
                    .build();
        } else {
            insertDayValueDoc(timeValue);
            ps = setPaymentStatement(money, payment, zero, 1, 0);
        }
        updateDayValue(collection, dayValueQuery, ps);
        // 只要支付线路支付成功就清除待付款订单数及时间
        MongoCursor unpaidCursor = getMongoCursor(collection, unpaidQuery);
        if (unpaidCursor.hasNext()) {
            collection.updateOne(unpaidQuery, new Document("$set",
                    new Document(IPaymentStatInfo.unpaidType, 0).append(IPaymentStatInfo.time, null)));
        }
    }

    private MongoCursor getMongoCursor(MongoCollection<Document> collection, Bson query) {
        FindIterable findIterable = collection.find(query);
        return  findIterable.iterator();
    }

    private PaymentStatement setPaymentStatement(BigDecimal money, String payment, BigDecimal serviceCharge,
                                                 int success, int totalCount) {
        return new PaymentStatement.Builder()
                .deposit(money)
                .paymentName(payment)
                .serviceCharge(serviceCharge)
                .successCount(success)
                .totalCount(totalCount)
                .build();
    }

    private void insertUnpaidDoc(Integer unpaid, String dateTime) {
        Document unpaidDoc = new Document()
                .append(IPaymentStatInfo.paymentId,paymentId)
                .append(IPaymentStatInfo.paymentName,payment)
                .append(IPaymentStatInfo.platformCode,platformCode)
                .append(IPaymentStatInfo.platformName,platformName)
                .append(IPaymentStatInfo.type,IPaymentStatInfo.unpaidType)
                .append(IPaymentStatInfo.unpaidType, unpaid)
                .append(IPaymentStatInfo.time,dateTime);
        collection.insertOne(unpaidDoc);
    }

    private void insertDayValueDoc(String timeValue) {
        Document dayValueDoc = new Document()
                .append(IPaymentStatInfo.paymentId,paymentId)
                .append(IPaymentStatInfo.paymentName,payment)
                .append(IPaymentStatInfo.platformCode,platformCode)
                .append(IPaymentStatInfo.platformName,platformName)
                .append(IPaymentStatInfo.type,IPaymentStatInfo.dayType)
                .append(IPaymentStatInfo.time,timeValue);
        collection.insertOne(dayValueDoc);
    }
}
