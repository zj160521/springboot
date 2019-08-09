package com.gm.bolt.financestatement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gm.topology.RedisBaseTopo;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class FinanceBaseBolt extends BaseRichBolt {

    protected JedisPool pool;
    protected OutputCollector collector;
    protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected Integer type;//事件类型，1存款2提款3红利4优惠5微调6转账7佣金8手续费
    protected BigDecimal money;//金额
    protected String time;//记录发生时间
    protected String payment;//存款支付线路
    protected String paymentId;//存款支付线路id
    protected String memberId;//会员id
    protected Integer state;//存款记录状态，0所有存款记录（包含成功与失败），1存款成功记录
    protected String platformName;//支付平台名
    protected String platformCode;//支付平台code

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        pool = RedisBaseTopo.getJedisPool();
    }

    @Override
    public void execute(Tuple input) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    //原始数据解析
    protected int getData(Tuple input){
        try {
            String msg = input.getString(0);
            JSONObject dataObj = JSON.parseObject(msg);
            JSONObject msgObj = JSON.parseObject(dataObj.getString("message"));
            type = msgObj.getInteger("event");
            money = new BigDecimal(msgObj.getString("money"));
            time = msgObj.getString("eventTime");
            payment = msgObj.getString("paymentName");
            memberId = msgObj.getString("memberId");
            state = msgObj.getInteger("state");
            platformName = msgObj.getString("platformName");
            platformCode = msgObj.getString("platformCode");
            paymentId = msgObj.getString("paymentId");
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}
