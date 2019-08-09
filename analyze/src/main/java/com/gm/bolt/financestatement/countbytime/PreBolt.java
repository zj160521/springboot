package com.gm.bolt.financestatement.countbytime;

import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class PreBolt extends HourBaseBolt {

    @Override
    public void execute(Tuple input) {
        int isSuccess = getData(input);
        if (isSuccess == 1)
            return;
        collector.emit(new Values(input.getString(0),type));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("data","type"));
    }
}
