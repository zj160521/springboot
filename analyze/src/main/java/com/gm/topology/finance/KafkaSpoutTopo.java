package com.gm.topology.finance;

import com.gm.bolt.financestatement.countbytime.BonusBolt;
import com.gm.bolt.financestatement.countbytime.DepositeBolt;
import com.gm.bolt.financestatement.countbytime.DrawMoneyBolt;
import com.gm.bolt.financestatement.countbytime.ServiceChargeBolt;
import com.gm.domain.Topology;
import com.gm.spout.fiance.FinanceSpout;
import com.gm.util.PropertyUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.kafka.spout.ByTopicRecordTranslator;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class KafkaSpoutTopo {

	public static Topology getTopology(String[] args) {
	    //初始化配置文件
        PropertyUtil.initProperty(args);
//	    //初始化Redis连接池
//		initRedis();
		String kafkaTopic = "financeRec";
		String spoutId = "FinanceStatementSpout";
		//将kafka记录转化成storm的tuple
        ByTopicRecordTranslator<String,String> trans =
                new ByTopicRecordTranslator<>( (r) -> new Values(r.value(),r.topic()),new Fields("values","topic"));
        //设置要消费的topic
        trans.forTopic(kafkaTopic, (r) -> new Values(r.value(),r.topic()), new Fields("values","topic"));
        //类似之前的SpoutConfig
        KafkaSpoutConfig<String,String> ksc = KafkaSpoutConfig
                //bootstrapServers 以及topic
                .builder(PropertyUtil.host.concat(":").
						concat(PropertyUtil.kafka_port), kafkaTopic)
                //设置consumer的group.id
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "financeStatement")
				.setProcessingGuarantee(KafkaSpoutConfig.ProcessingGuarantee.AT_LEAST_ONCE)
                //设置开始消费的起始位置
                .setFirstPollOffsetStrategy(FirstPollOffsetStrategy.LATEST)
                //设置提交消费边界的时长间隔
                .setOffsetCommitPeriodMs(
                		Long.parseLong(PropertyUtil.kafka_offset_commit_period_ms))
                //Translator
                .setRecordTranslator(trans)
                .build();

		TopologyBuilder builder = new TopologyBuilder();
		//使用框架内置KafkaSpout
//		builder.setSpout(spoutId, new KafkaSpout<>(ksc),4);
		builder.setSpout(spoutId, new FinanceSpout(),4);
		builder.setBolt("deposite", new DepositeBolt(),1).shuffleGrouping(spoutId);
		builder.setBolt("drawMoney", new DrawMoneyBolt(),1).shuffleGrouping(spoutId);
		builder.setBolt("bonus", new BonusBolt(),1).shuffleGrouping(spoutId);
		builder.setBolt("serviceCharge", new ServiceChargeBolt(),1).shuffleGrouping(spoutId);
		Config conf = new Config();
		conf.setNumWorkers(4);
//		conf.setNumAckers(0);
//		conf.setDebug(false);

		Topology topology = new Topology();
		topology.setConf(conf);
		topology.setStormTopology(builder.createTopology());
		topology.setTopologyName("FinanceStatement");

		return topology;
	}

}
