package com.gm.bolt.financestatement.countbytime;

import com.gm.domain.finance.IPaymentStatInfo;
import com.gm.domain.finance.PaymentStatement;
import com.gm.topology.RedisBaseTopo;
import com.gm.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Tuple;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.Map;

public class ServiceChargeBolt extends HourBaseBolt {

    private MongoCollection<Document> collection;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        pool = RedisBaseTopo.getJedisPool();
        collection = MongoUtil.getConnect().getCollection(paymentMongoDBTableName);
    }

    @Override
    public void execute(Tuple input) {
        if (input.getInteger(1) == 8) {
            // 服务费统计
            jedis = pool.getResource();
            try {
                countValue(input, 8, serviceCharge);
            } finally {
                jedis.close();
            }
            // 服务费计入支付统计
            String timeValue = time.substring(0,10);
            Bson query = Filters.and(Filters.eq(IPaymentStatInfo.paymentId,paymentId),
                    Filters.eq(IPaymentStatInfo.platformCode,platformCode),
                    Filters.eq(IPaymentStatInfo.type,IPaymentStatInfo.dayType),
                    Filters.eq(IPaymentStatInfo.time,timeValue));
            FindIterable findIterable = collection.find(query);
            MongoCursor cursor = findIterable.iterator();
            PaymentStatement ps;
            if (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                ps = new PaymentStatement.Builder()
                        .deposit(document.get(IPaymentStatInfo.deposit, Decimal128.class).bigDecimalValue())
                        .paymentName(document.getString(IPaymentStatInfo.paymentName))
                        .serviceCharge(money.add(document.get(IPaymentStatInfo.serviceCharge, Decimal128.class).bigDecimalValue()))
                        .successCount(document.getInteger(IPaymentStatInfo.successCount))
                        .totalCount(document.getInteger(IPaymentStatInfo.totalCount))
                        .build();
            } else {
                collection.insertOne(new Document()
                        .append(IPaymentStatInfo.paymentId,paymentId)
                        .append(IPaymentStatInfo.paymentName,payment)
                        .append(IPaymentStatInfo.platformCode,platformCode)
                        .append(IPaymentStatInfo.platformName,platformName)
                        .append(IPaymentStatInfo.type,IPaymentStatInfo.dayType)
                        .append(IPaymentStatInfo.time,timeValue));
                ps = new PaymentStatement.Builder()
                        .deposit(new BigDecimal(0))
                        .paymentName(payment)
                        .serviceCharge(money)
                        .successCount(0)
                        .totalCount(0)
                        .build();
            }
            DepositeBolt.updateDayValue(collection, query, ps);
            collector.ack(input);
        }
    }
}
