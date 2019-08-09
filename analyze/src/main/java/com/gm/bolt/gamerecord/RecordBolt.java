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

public class RecordBolt extends BaseRichBolt{

	/**
	 * 解析记录
	 */
	private static final long serialVersionUID = -2908568744349987384L;
	private OutputCollector collector;
	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//会员id,单词净输赢,单次投注金额,日期,key
		declarer.declare(new Fields("memberName","betAmount","gainAmount","date","key"));
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		 this.collector=collector;
	}

	@Override
	public void execute(Tuple tuple) {
		String msg = tuple.getString(0);
		JSONObject inputObject1 = JSON.parseObject(msg);
		JSONObject inputObject = JSON.parseObject(inputObject1.getString("message"));
		LocalDate date =LocalDateTime.ofInstant( new Date(inputObject.getLong("timestamp")).toInstant(),ZoneOffset.ofHours(8)).toLocalDate();
		collector.emit(new Values(inputObject.get("userId"),inputObject.getLong("betAmount"),inputObject.getLong("gainAmount"),inputObject.getLong("timestamp"),inputObject.get("userId")+":"+date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
	}

}
