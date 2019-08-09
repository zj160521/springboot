/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gm.bolt.gamerecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.TupleUtils;
import org.bson.Document;

import com.gm.util.mongo.SystemMongoDBClient;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

public class CountBolt extends BaseRichBolt{

	private String url;
	private String collectionName;

	protected OutputCollector collector;
	protected SystemMongoDBClient mongoClient;
	private static final long serialVersionUID = -2879816950003629733L;
	private Map<String, Long> userProfit=new HashMap<String, Long>();
	private Map<String, Long> userBet = new HashMap<>();
	public CountBolt(String url, String collectionName) {
		Validate.notEmpty(url, "url can not be blank or null");
	    Validate.notEmpty(collectionName, "collectionName can not be blank or null");
	    this.url = url;
	    this.collectionName = collectionName;
    }

    @Override
    public void execute(Tuple tuple) {
        if (TupleUtils.isTick(tuple)) {
            return;
        }

        try{
        	String userId = tuple.getString(0);
    		Long betAmount = tuple.getLong(1);
    		Long gainAmount = tuple.getLong(2);
    		Long profitAmount = gainAmount-betAmount;
    		//1.判断记录是否在内存中
    		if(userProfit.get(tuple.getValueByField("key"))==null||userBet.get(tuple.getValueByField("key"))==null) {
    			//查询document
            	MongoCollection<Document> mongoCollection=mongoClient.getCollection();
            	BasicDBObject queryObject = new BasicDBObject("key",tuple.getValueByField("key"));
            	Document record = mongoCollection.find(queryObject).first();
            	
            	userProfit.put(tuple.getValueByField("key").toString(), record ==null?0:record.getLong("profitAmount"));
            	userBet.put(tuple.getValueByField("key").toString(),  record ==null?0:record.getLong("betAmount"));
    		}
        	Long totalProfitAmount =profitAmount;
        	Long totalBetAmount =betAmount;
        	
        	totalProfitAmount += userProfit.get(tuple.getValueByField("key"));
        	System.out.println(Thread.currentThread().getId()+":"+totalProfitAmount);
        	userProfit.put(tuple.getValueByField("key").toString(),totalProfitAmount);
        	totalBetAmount += userBet.get(tuple.getValueByField("key"));
        	userBet.put(tuple.getValueByField("key").toString(),totalBetAmount);
        	LocalDate date =LocalDateTime.ofInstant( new Date(tuple.getLong(3)).toInstant(),ZoneOffset.ofHours(8)).toLocalDate();
        	collector.emit(new Values(userId,totalProfitAmount,totalBetAmount,date.format(DateTimeFormatter.ofPattern("yyyyMMdd")),tuple.getString(4)));
        } catch (Exception e) {
            this.collector.reportError(e);
            this.collector.fail(tuple);
        }
    }
    
    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {
        this.collector = collector;
        this.mongoClient = new SystemMongoDBClient(url, collectionName);
        
    }

    @Override
    public void cleanup() {
       this.mongoClient.close();
    }
    @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//会员id,累计净输赢,累计投注金额,日期
		declarer.declare(new Fields("memberName","profitAmount","betAmount","date","key"));
	}

}
