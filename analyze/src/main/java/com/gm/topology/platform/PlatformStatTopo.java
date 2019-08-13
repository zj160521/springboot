package com.gm.topology.platform;

import com.gm.bolt.platformStat.countbymember.MemberCountBolt;
import com.gm.bolt.platformStat.countbyplatform.PlatformCountBolt;
import com.gm.domain.Topology;
import com.gm.spout.platform.stat.PlatformStatSpout;
import com.gm.util.PropertyUtil;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class PlatformStatTopo {

	public static Topology getTopology(String[] args) {
	    //初始化配置文件
        PropertyUtil.initProperty(args);
		String spoutId = "PlatformStatSpout";

		TopologyBuilder builder = new TopologyBuilder();
		//平台报表统计，统计每天平台运营情况
		builder.setSpout(spoutId, new PlatformStatSpout(),1);
        builder.setBolt("PlatformCountBolt", new PlatformCountBolt(),1).shuffleGrouping(spoutId);
		builder.setBolt("MemberCountBolt", new MemberCountBolt(),1).shuffleGrouping(spoutId);

		Config conf = new Config();
		conf.setNumWorkers(2);

		Topology topology = new Topology();
		topology.setConf(conf);
		topology.setStormTopology(builder.createTopology());
		topology.setTopologyName("PlatformStatement");
		return topology;
	}

//	public static void main(String[] args) {
//		//平台运营统计topo
//		Topology topology = PlatformStatTopo.getTopology(args);
//		try {
//			StormSubmitter.submitTopology(topology.getTopologyName(), topology.getConf(), topology.getStormTopology());
//		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
//			e.printStackTrace();
//		}
//	}
	
}
