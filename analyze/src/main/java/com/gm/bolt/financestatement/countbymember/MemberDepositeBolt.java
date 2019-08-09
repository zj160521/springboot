package com.gm.bolt.financestatement.countbymember;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.storm.tuple.Tuple;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MemberDepositeBolt extends MemberBaseBolt {

    @Override
    public void execute(Tuple input) {
        try {
            //统计会员存款相关数据
            if (input.getInteger(1) == 1) {
                getData(input);
                if (state != null && state == 1) {//只计算存款成功的数据
                    // 插入MongoDB
                    BigDecimal total = setMongoData(deposite, money);//会员总存款
                    setMongoData(depositeTimes, new BigDecimal(1));//会员总存款次数
                    setFirstTime(firstDepositeTime, time, 1);//会员首存时间
                    setLastTime(lastDepositeTime, time);//会员末存时间
                    // 向kafka发送会员总存款信息
                    Map<String, Object> map = new HashMap<>(10);
                    map.put("memberId", memberId);
                    map.put("money", total.intValue());
                    ProducerRecord record = new ProducerRecord(depositTopic, JSON.toJSON(map).toString());
                    producer.send(record);
                }
            }
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
