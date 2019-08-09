package com.gm.util;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class KafkaConsumerProperty {
    public static Properties getConsumerProps(){
        Properties props = new Properties();
        props.put("bootstrap.servers", PropertyUtil.host.concat(":").concat(PropertyUtil.kafka_port));
        props.put("enable.auto.commit", "false");
        props.put("max.poll.records", PropertyUtil.kafka_max_pollrecords);
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        return props;
    }
}
