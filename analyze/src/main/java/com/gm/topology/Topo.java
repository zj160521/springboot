package com.gm.topology;

import com.gm.topology.activity.ActivityStatTopo;
import com.gm.topology.finance.StatementTopo;
import com.gm.topology.gamerecord.GameRecordTopo;
import com.gm.topology.platform.PlatformStatTopo;
import org.apache.storm.LocalCluster;

import com.gm.domain.Topology;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;

public class Topo {

	public static void main(String[] args) {
		//财务统计和活动统计topo
		Topology stat = StatementTopo.getTopology(args);
		Topology act = ActivityStatTopo.getTopology(args);
		Topology plat = PlatformStatTopo.getTopology(args);
		//游戏记录的topo
		Topology gameRecord = GameRecordTopo.getTopology(args);
		//LocalCluster用来将topology提交到本地模拟器运行，方便开发调试
//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology(stat.getTopologyName(), stat.getConf(), stat.getStormTopology());
//		cluster.submitTopology(activityStat.getTopologyName(), activityStat.getConf(), activityStat.getStormTopology());
		try {
			StormSubmitter.submitTopology(stat.getTopologyName(), stat.getConf(), stat.getStormTopology());
			StormSubmitter.submitTopology(act.getTopologyName(), act.getConf(), act.getStormTopology());
			StormSubmitter.submitTopology(plat.getTopologyName(), plat.getConf(), plat.getStormTopology());
			StormSubmitter.submitTopology(gameRecord.getTopologyName(), gameRecord.getConf(), gameRecord.getStormTopology());
		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
			e.printStackTrace();
		}
	}

}
