package com.gm.bolt.financestatement.countbytime;

import org.apache.storm.tuple.Tuple;

public class DrawMoneyBolt extends HourBaseBolt {

    @Override
    public void execute(Tuple input) {
        if (input.getInteger(1) == 2) {
            jedis = pool.getResource();
            try {
                countValue(input, 2, drawMoney);
                setCountTimesData(dayKey, timesCount);
                setCountTimesData(yearKey, timesCount);
                setCountTimesData(monthKey, timesCount);
            } finally {
                jedis.close();
            }
            collector.ack(input);
        }
    }
}
