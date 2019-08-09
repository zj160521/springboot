package com.gm.bolt.financestatement.countbytime;

import org.apache.storm.tuple.Tuple;

public class DiscountsBolt extends HourBaseBolt {

    @Override
    public void execute(Tuple input) {
        if (input.getInteger(1) == 4) {
            jedis = pool.getResource();
            try {
                countValue(input, input.getInteger(1), discounts);
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
