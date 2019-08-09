package com.gm.bolt.financestatement.countbymember;

import com.alibaba.fastjson.JSON;
import com.gm.bolt.financestatement.countbytime.HourBaseBolt;
import com.gm.domain.finance.MemberStatement;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.bson.Document;

import java.math.BigDecimal;

public class MemberPreBolt extends MemberBaseBolt {

    @Override
    public void execute(Tuple input) {
        int isSuccess = getData(input);
        if (isSuccess == 1)
            return;
        if (type == 1 && state != null && state == 0){

        } else {
            //会员id插入MongoDB
            MongoCursor cursor = getByMemberID(memberId);
            if (!cursor.hasNext()) {
                collection.insertOne(new Document().append("member_id",memberId));
            }

            //会员每天数据累计
            String day = time.substring(0,10);
            FindIterable findIterable = collection.find(Filters.eq("member_id",memberId));
            MongoCursor iterator = findIterable.iterator();
            if (iterator.hasNext()) {
                Document document = (Document)iterator.next();
                String dayValue = document.get(day, String.class);
                MemberStatement memberStatement;
                if (StringUtils.isEmpty(dayValue)) {
                    memberStatement = new MemberStatement.Builder()
                            .bonus(BigDecimal.valueOf(0))
                            .deposit(BigDecimal.valueOf(0))
                            .discounts(BigDecimal.valueOf(0))
                            .drawMoney(BigDecimal.valueOf(0))
                            .build();
                } else {
                    memberStatement = JSON.parseObject(dayValue, MemberStatement.class);
                }
                //数据累加
                switch (type) {
                    case 1:
                        //redis记录每天存款人数统计
                        if (memberStatement.getDeposit().compareTo(BigDecimal.ZERO)==0) {
                            jedis = pool.getResource();
                            try {
                                String key = HourBaseBolt.deposite.concat("_").concat(day);
                                String count = jedis.hget(key, dayPeopleDepositeTimes);
                                Integer value = StringUtils.isEmpty(count) ? 1:Integer.parseInt(count)+1;
                                jedis.hset(key, dayPeopleDepositeTimes, value.toString());
                            } finally {
                                jedis.close();
                            }
                        }
                        //会员每天存款累计
                        memberStatement.setDeposit(memberStatement.getDeposit().add(money));
                        break;
                    case 2://会员每天取款累计
                        memberStatement.setDrawMoney(memberStatement.getDrawMoney().add(money));
                        break;
                    case 3://会员每天红利累计
                        memberStatement.setBonus(memberStatement.getBonus().add(money));
                        break;
                    case 4://会员每天优惠累计
                        memberStatement.setDiscounts(memberStatement.getDiscounts().add(money));
                        break;
                }
                collection.updateOne(Filters.eq("member_id",memberId),
                        new Document("$set",new Document(day,JSON.toJSON(memberStatement).toString())));
            }
        }

        collector.emit(new Values(input.getString(0),type));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("data","type"));
    }
}
