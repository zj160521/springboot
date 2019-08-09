package com.gm.bolt.financestatement.countbytime;

import com.gm.bolt.financestatement.FinanceBaseBolt;
import com.gm.util.IDGenerator;
import com.gm.util.JDBCUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class HourBaseBolt extends FinanceBaseBolt {

    public static final String deposite = "finance:deposite";
    public static final String drawMoney = "finance:drawMoney";
    public static final String bonus = "finance:bonus";
    public static final String discounts = "finance:discounts";
    public static final String serviceCharge = "finance:service_charge";
    public static final String paymentName = "finance:payment";
    public static final String timesCount = "timesCount";
    protected final String paymentMongoDBTableName = "payment_stat";
    protected String dayKey;
    protected String yearKey;
    protected String monthKey;
    protected Jedis jedis;

    //按时、月累加财务数据
    protected void countValue(Tuple input, Integer type, String typeName){
        getData(input);
        if (type != 1)
            payment = null;
        generateTimeKey(typeName);
        //按小时统计
        setHourValue(dayKey, money, time, payment);
        //按月统计
        setMonthValue(yearKey, money, time, payment);

    }

    //小时数据累加
    protected void setHourValue(String typeKey, BigDecimal money, String time, String payment) {
        Calendar calendar = parseStrToCalendar(time);
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);

        //按小时统计结果存放进Redis
        saveDataInRedis(jedis,typeKey,hour.toString(),money);

        //统计当日线路的存款情况
        if (payment != null) {
            String key = paymentName.concat("_").concat(time.substring(0,10));
            saveDataInRedis(jedis,key,payment,money);
        }
    }

    //月数据累加
    protected void setMonthValue(String typeKey, BigDecimal money, String time, String payment) {
        Calendar calendar = parseStrToCalendar(time);
        Integer month = calendar.get(Calendar.MONTH) + 1;

        //月统计结果存放进Redis
        saveDataInRedis(jedis,typeKey,month.toString(),money);

        if (payment != null) {
            String monthParmentKey = paymentName.concat("_").concat(time.substring(0,7));
            saveDataInRedis(jedis,monthParmentKey,payment,money);
            String yearParmentKey = paymentName.concat("_").concat(time.substring(0,4));
            saveDataInRedis(jedis,yearParmentKey,payment,money);
        }
    }

    //统计总次数和记录时间段内数据的开始结束时间
    protected void setCountTimesData(String typeKey, String field) {
        try {
            saveDataInRedis(jedis,typeKey,field,new BigDecimal(1));
            String createTime = jedis.hget(typeKey, "createTime");
            Long timeLong = df.parse(time).getTime();
            if (null == createTime || timeLong < Long.parseLong(createTime))
                jedis.hset(typeKey, "createTime", timeLong.toString());
            String endTime = jedis.hget(typeKey, "endTime");
            if (null == endTime || timeLong > Long.parseLong(endTime))
                jedis.hset(typeKey, "endTime", timeLong.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void saveDataInRedis(Jedis jedis, String key, String field, BigDecimal money) {
        String count = jedis.hget(key, field);
        BigDecimal sum = StringUtils.isEmpty(count) ? new BigDecimal(0) : new BigDecimal(count);
        jedis.hset(key, field, (money.add(sum).toString()));
    }

    protected BigDecimal isNull(String value) {
        return StringUtils.isEmpty(value) ? BigDecimal.valueOf(0) : new BigDecimal(value);
    }

    /**
     * 数据存进mysql
     * @param jedis
     * @param day
     */
    protected void saveDataInMysql(Jedis jedis, String day) {
        Map<String, String> depositeMap = jedis.hgetAll(deposite);
        Map<String, String> drawMoneyMap = jedis.hgetAll(drawMoney);
        Map<String, String> bonusMap = jedis.hgetAll(bonus);
        Map<String, String> serviceChargeMap = jedis.hgetAll(serviceCharge);
        Map<String, String> financePayment = jedis.hgetAll(paymentName);
        Connection connection = JDBCUtil.getConnection();
        try {
            for (Map.Entry<String, String> entry : financePayment.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                String sql = "INSERT INTO gm_payment_stat(id,stat_date,payment,deposit_total) VALUES (?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, IDGenerator.uuid());
                pstmt.setString(2, day);
                pstmt.setString(3, key);
                pstmt.setString(4, value);
                pstmt.executeUpdate();
                pstmt.close();
            }
            for (int i = 0; i < 24; i++ ) {
                String sql = "INSERT INTO gm_stat_finance(id,type,stat_date,deposit_total,fee_total," +
                        "withdraw_total,bonus_total,bet_total,win_total,stat_hour) VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, IDGenerator.uuid());
                pstmt.setInt(2, 1);
                pstmt.setString(3, day);
                pstmt.setBigDecimal(4, isNull(depositeMap.get(i+"")));
                pstmt.setBigDecimal(5, isNull(serviceChargeMap.get(i+"")));
                pstmt.setBigDecimal(6, isNull(drawMoneyMap.get(i+"")));
                pstmt.setBigDecimal(7, isNull(bonusMap.get(i+"")));
                pstmt.setBigDecimal(8, BigDecimal.valueOf(0));
                pstmt.setBigDecimal(9, BigDecimal.valueOf(0));
                pstmt.setInt(10, i);
                pstmt.executeUpdate();
                pstmt.close();
            }
            jedis.del(deposite,drawMoney,bonus,serviceCharge,paymentName);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    protected void generateTimeKey(String field) {
        dayKey = field.concat("_").concat(time.substring(0,10));
        yearKey = field.concat("_").concat(time.substring(0,4));
        monthKey = field.concat("_").concat(time.substring(0,7));
    }

    public Calendar parseStrToCalendar (String str){
        try {
            Date createTime = df.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createTime);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
