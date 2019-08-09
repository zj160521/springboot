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

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.storm.mongodb.common.QueryFilterCreator;
import org.apache.storm.mongodb.common.SimpleQueryFilterCreator;
import org.apache.storm.mongodb.common.mapper.MongoMapper;
import org.apache.storm.mongodb.common.mapper.SimpleMongoUpdateMapper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.TupleUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.gm.util.mongo.SystemMongoDBClient;

/**
 * Basic bolt for updating from MongoDB.
 *
 * Note: Each MongoUpdateBolt defined in a topology is tied to a specific collection.
 *
 */
public class StorageBolt extends BaseRichBolt{

	private String url;
	private String collectionName;

	protected OutputCollector collector;
	protected SystemMongoDBClient mongoClient;
	private static final long serialVersionUID = -2879816950003629733L;
	public StorageBolt(String url, String collectionName) {
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
        	MongoMapper mapper = new SimpleMongoUpdateMapper().withFields("memberName","profitAmount","betAmount","date","key");
            Document doc = mapper.toDocument(tuple);
            //get query filter
            QueryFilterCreator queryCreator = new SimpleQueryFilterCreator()
                    .withField("key");
            Bson filter = queryCreator.createFilter(tuple);
            mongoClient.update(filter, doc, true);
            this.collector.ack(tuple);
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
        
    }

}
