package com.gm.bolt.activitystat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gm.domain.activity.ActivityStat;
import com.gm.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.bson.Document;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ActivityStatBolt extends BaseRichBolt {

    protected OutputCollector collector;
    protected String activityModelName;
    protected String activityModelId;
    protected BigDecimal amount;
    protected BigDecimal prizeAmount;
    protected String type;
    protected Date time;
    protected String timeStr;
    protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected final String mongoTableName = "activity_stat";
    protected MongoCollection mongoCollection;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        mongoCollection = MongoUtil.getConnect().getCollection(mongoTableName);
    }

    @Override
    public void execute(Tuple input) {
        try {
            String msg = input.getString(0);
//        System.out.println(">>>>>>>>"+msg);
            JSONObject dataObj = JSON.parseObject(msg);
            JSONObject msgObj = JSON.parseObject(dataObj.getString("message"));
            type = msgObj.getString("type");
            amount = new BigDecimal(msgObj.getString("amount"));
            prizeAmount = new BigDecimal(msgObj.getString("prizeAmount"));
            long timeL = msgObj.getLong("time");
            time = new Date(timeL);
            timeStr = df.format(time).substring(0,10);
            activityModelName = msgObj.getString("activityModelName");
            activityModelId = msgObj.getString("activityModelId");
        } catch (Exception e) {
            return;
        }

        FindIterable findIterable = mongoCollection.find(Filters.eq("time",timeStr));
        MongoCursor cursor = findIterable.iterator();
        ActivityStat as;
        if (!cursor.hasNext()) {
            mongoCollection.insertOne(new Document().append("time",timeStr));
            as = new ActivityStat.Builder()
                    .times(1)
                    .activityModelName(activityModelName)
                    .amount(amount)
                    .prizeAmount(prizeAmount)
                    .type(type)
                    .build();

        } else {
            Document document = (Document)cursor.next();
            String actValue = document.get(activityModelId, String.class);
            if (StringUtils.isEmpty(actValue)) {
                as = new ActivityStat.Builder()
                        .times(1)
                        .activityModelName(activityModelName)
                        .amount(amount)
                        .prizeAmount(prizeAmount)
                        .type(type)
                        .build();
            } else {
                as = JSON.parseObject(actValue, ActivityStat.class);
                as.setTimes(as.getTimes() + 1);
                as.setAmount(as.getAmount().add(amount));
                as.setPrizeAmount(as.getPrizeAmount().add(prizeAmount));
            }
        }
        mongoCollection.updateOne(Filters.eq("time",timeStr),
                new Document("$set",new Document(activityModelId,JSON.toJSON(as).toString())));
        collector.emit(new Values(input.getString(0)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("data"));
    }
}
