package com.gm.util;

import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerProperty {
    public static Properties getProducerProps(){
        Properties props = new Properties();
        props.put("bootstrap.servers", PropertyUtil.host.concat(":").concat(PropertyUtil.kafka_port));
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        return props;
    }
}
