package com.gm.topology.gamerecord;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

import com.gm.bolt.gamerecord.CountBolt;
import com.gm.bolt.gamerecord.PrizeBolt;
import com.gm.bolt.gamerecord.PrizeStorageBolt;
import com.gm.bolt.gamerecord.RecordBolt;
import com.gm.bolt.gamerecord.StorageBolt;
import com.gm.domain.Topology;
import com.gm.spout.gamerecord.GameRecordSpout;
import com.gm.util.PropertyUtil;

public class GameRecordTopo {

	public static Topology getTopology(String[] args) {
		PropertyUtil.initProperty(args);
		TopologyBuilder builder = new TopologyBuilder();
		//设置一个spout用来从kaflka消息队列中读取数据并发送给下一级的bolt组件，此处用的spout组件并非自定义的，而是storm中已经开发好的KafkaSpout
		builder.setSpout("kafkaSpout", new GameRecordSpout(),1);
		builder.setBolt("recordBolt", new RecordBolt(),1).shuffleGrouping("kafkaSpout");
		builder.setBolt("countBolt", new CountBolt(PropertyUtil.getProperty("mongo.url"),"game_record_count"), 1).shuffleGrouping("recordBolt");
		builder.setBolt("storageBolt", new StorageBolt(PropertyUtil.getProperty("mongo.url"),"game_record_count"),1).shuffleGrouping("countBolt");
		builder.setBolt("prizeBolt",new PrizeBolt(),1).shuffleGrouping("recordBolt");
		builder.setBolt("prizeStorageBolt",new PrizeStorageBolt(PropertyUtil.getProperty("mongo.url"),"game_record"),1).shuffleGrouping("prizeBolt");;
		Config conf = new Config();
		conf.setNumWorkers(2);

		Topology topology = new Topology();
		topology.setConf(conf);
		topology.setStormTopology(builder.createTopology());
		topology.setTopologyName("gameRecordAnalyze");

		return topology;
	}
	public static void main(String[] args) {
		//游戏记录的topo
		Topology gameRecord = GameRecordTopo.getTopology(args);
		//游戏记录topo提交到集群
		try {
			StormSubmitter.submitTopology(gameRecord.getTopologyName(), gameRecord.getConf(), gameRecord.getStormTopology());
		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
			e.printStackTrace();
		}
	}

}
