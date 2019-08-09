package com.gm.bolt.financestatement.countbymember;

import org.apache.storm.tuple.Tuple;
import java.math.BigDecimal;

public class MemberDrawMoneyBolt extends MemberBaseBolt {

    @Override
    public void execute(Tuple input) {
        try {
            //按小时统计取款数据
            if (input.getInteger(1) == 2){
                getData(input);
//                System.out.println(input.getString(0)+"+++++");

                //插入MongoDB
                setMongoData(drawMoney,money);//会员总取款
                setMongoData(drawMoneyTimes,new BigDecimal(1));//会员总取款次数
                setFirstTime(firstDrawMoneyTime,time, 2);//会员首次取款时间
                setLastTime(lastDrawMoneyTime,time);//会员最后一次取款时间
            }
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
