package com.gm.spout.gamerecord;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import com.gm.util.KafkaConsumerProperty;

public class GameRecordSpout extends BaseRichSpout {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KafkaConsumer consumer;
    private ConsumerRecords<String, String> msgList;
    private String topic;
    private static final String GROUPID = "data_analyze";
    SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        Properties props = KafkaConsumerProperty.getConsumerProps();
        props.setProperty("group.id", GROUPID);
        this.collector = collector;
        this.consumer = new KafkaConsumer<String, String>(props);
        this.topic = "gameRecord";
        this.consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void nextTuple() {
        msgList = consumer.poll(Duration.ofMillis(1000));
        if(null !=msgList && msgList.count() > 0){
            for (ConsumerRecord<String, String> record : msgList) {
                collector.emit(new Values(record.value()));
            }
            consumer.commitSync();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("value"));
    }
}
