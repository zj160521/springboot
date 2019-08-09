package com.gm.bolt.financestatement.countbymember;

import org.apache.storm.tuple.Tuple;
import java.math.BigDecimal;

public class MemberDiscountsBolt extends MemberBaseBolt {

    @Override
    public void execute(Tuple input) {
        try {
            //按小时统计手续费数据
            if (input.getInteger(1) == 4){
                getData(input);
//                System.out.println(input.getString(0)+"+++++");
                //插入MongoDB
                setMongoData(discounts,money);//会员总优惠金额
                setMongoData(discountsTimes,new BigDecimal(1));//会员领取优惠次数
            }
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
