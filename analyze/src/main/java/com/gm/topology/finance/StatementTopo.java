package com.gm.topology.finance;

import com.gm.bolt.financestatement.countbytime.*;
import com.gm.bolt.financestatement.countbymember.*;
import com.gm.domain.Topology;
import com.gm.spout.fiance.FinanceSpout;
import com.gm.util.PropertyUtil;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class StatementTopo {

	public static Topology getTopology(String[] args) {
	    //初始化配置文件
        PropertyUtil.initProperty(args);

		String spoutId = "FinanceStatementSpout";

		TopologyBuilder builder = new TopologyBuilder();
		//财务报表统计，统计每天的财务情况
		builder.setSpout(spoutId, new FinanceSpout(),1);
        builder.setBolt("preBolt", new PreBolt(),1).shuffleGrouping(spoutId);
		builder.setBolt("deposite", new DepositeBolt(),1).shuffleGrouping("preBolt");
		builder.setBolt("drawMoney", new DrawMoneyBolt(),1).shuffleGrouping("preBolt");
		builder.setBolt("bonus", new BonusBolt(),1).shuffleGrouping("preBolt");
		builder.setBolt("discounts", new DiscountsBolt(),1).shuffleGrouping("preBolt");
		builder.setBolt("serviceCharge", new ServiceChargeBolt(),1).shuffleGrouping("preBolt");
		//会员财务情况统计，统计会员总的财务状况、会员每天的财务情况、每天的存款人数和首存人数
		builder.setBolt("memberPreBolt", new MemberPreBolt(),1).shuffleGrouping(spoutId);
		builder.setBolt("memberDeposite", new MemberDepositeBolt(),1).shuffleGrouping("memberPreBolt");
		builder.setBolt("memberDrawMoney", new MemberDrawMoneyBolt(),1).shuffleGrouping("memberPreBolt");
		builder.setBolt("memberBonus", new MemberBonusBolt(),1).shuffleGrouping("memberPreBolt");
		builder.setBolt("memberDiscounts", new MemberDiscountsBolt(),1).shuffleGrouping("memberPreBolt");

		Config conf = new Config();
		conf.setNumWorkers(2);

		Topology topology = new Topology();
		topology.setConf(conf);
		topology.setStormTopology(builder.createTopology());
		topology.setTopologyName("FinanceStatement");

		return topology;
	}

	public static void main(String[] args) {
		//财务统计
		Topology financeStat = StatementTopo.getTopology(args);
		try {
			StormSubmitter.submitTopology(financeStat.getTopologyName(), financeStat.getConf(), financeStat.getStormTopology());
		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
			e.printStackTrace();
		}
	}

}
