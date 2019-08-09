package com.gm.bolt.financestatement.countbymember;

import org.apache.storm.tuple.Tuple;

import java.math.BigDecimal;

public class MemberBonusBolt extends MemberBaseBolt {

    @Override
    public void execute(Tuple input) {
        try {
            //按小时统计红利优惠数据
            if (input.getInteger(1) == 3){
                getData(input);
//                System.out.println(input.getString(0)+"+++++");
                //插入MongoDB
                setMongoData(bonus,money);//会员领取的总红利
                setMongoData(bonusTimes,new BigDecimal(1));//会员领取红利次数
            }
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
