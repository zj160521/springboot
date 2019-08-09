package com.gm.topology.activity;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

import com.gm.bolt.activitystat.ActivityStatBolt;
import com.gm.domain.Topology;
import com.gm.spout.activity.stat.ActivityStatSpout;
import com.gm.util.PropertyUtil;

public class ActivityStatTopo {

	public static Topology getTopology(String[] args) {
	    //初始化配置文件
        PropertyUtil.initProperty(args);
		String spoutId = "ActivityStatSpout";

		TopologyBuilder builder = new TopologyBuilder();
		//财务报表统计，统计每天的财务情况
		builder.setSpout(spoutId, new ActivityStatSpout(),1);
        builder.setBolt("preBolt", new ActivityStatBolt(),1).shuffleGrouping(spoutId);

		Config conf = new Config();
		conf.setNumWorkers(1);

		Topology topology = new Topology();
		topology.setConf(conf);
		topology.setStormTopology(builder.createTopology());
		topology.setTopologyName("ActivityStatement");
		return topology;
	}

	public static void main(String[] args) {
		//活动统计topo
		Topology activityStat = ActivityStatTopo.getTopology(args);
		try {
			StormSubmitter.submitTopology(activityStat.getTopologyName(), activityStat.getConf(), activityStat.getStormTopology());
		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
			e.printStackTrace();
		}
	}
	
}
