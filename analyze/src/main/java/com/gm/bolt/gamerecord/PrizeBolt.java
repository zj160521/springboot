package com.gm.bolt.gamerecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 千倍百倍
 */
public class PrizeBolt extends BaseRichBolt{

	/**
	 * 解析记录
	 */
	private static final long serialVersionUID = -2908568744349987384L;
	private OutputCollector collector;
	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//会员名,倍数,投注金额,赢取金额,日期,key
		declarer.declare(new Fields("memberName","multiple","betAmount","gainAmount","date","key"));
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		 this.collector=collector;
	}

	@Override
	public void execute(Tuple tuple) {
		LocalDate date =LocalDateTime.ofInstant( new Date(tuple.getLong(3)).toInstant(),ZoneOffset.ofHours(8)).toLocalDate();
		if(tuple.getLong(1)!=0&&tuple.getLong(2)!=0) {
			collector.emit(new Values(tuple.getString(0),tuple.getLong(2)/tuple.getLong(1),tuple.getLong(1),tuple.getLong(2),date.format(DateTimeFormatter.ofPattern("yyyyMMdd")),tuple.getString(4)));
		}
	}

}
