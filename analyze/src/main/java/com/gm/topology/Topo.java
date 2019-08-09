package com.gm.topology;

import com.gm.topology.finance.StatementTopo;
import com.gm.topology.platform.PlatformStatTopo;
import org.apache.storm.LocalCluster;

import com.gm.domain.Topology;

public class Topo {

	public static void main(String[] args) {
		//财务统计和活动统计topo
//		Topology stat = StatementTopo.getTopology(args);
//		Topology stat = ActivityStatTopo.getTopology(args);
		Topology stat = PlatformStatTopo.getTopology(args);
		//游戏记录的topo
//		Topology gameRecord = GameRecordTopo.getTopology(args);
		//LocalCluster用来将topology提交到本地模拟器运行，方便开发调试
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(stat.getTopologyName(), stat.getConf(), stat.getStormTopology());
//		cluster.submitTopology(activityStat.getTopologyName(), activityStat.getConf(), activityStat.getStormTopology());
//		try {
//			StormSubmitter.submitTopology(financeStat.getTopologyName(), financeStat.getConf(), financeStat.getStormTopology());
//			StormSubmitter.submitTopology(activityStat.getTopologyName(), activityStat.getConf(), activityStat.getStormTopology());
//		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
//			e.printStackTrace();
//		}
	}

}
