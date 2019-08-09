package com.gm.util;

import java.util.Properties;

/**
 * 属性配置读取工具
 */
public class PropertyUtil {
	public static final String host = "192.169.7.20";
	public static final String kafka_offset_commit_period_ms = "10000";
	public static final String kafka_max_pollrecords = "100";
	public static final String kafka_port = "9092";
	public static final String redis_port = "6379";
	public static final String redis_pwd = "123456";
	public static final String mysql_url = "jdbc:mysql://192.169.7.20:8066/GM?useServerPrepStmts=false&rewriteBatchedStatements=true";
	public static final String mysql_username = "root";
	public static final String mysql_pwd="123456";
	public static final String mysql_driver="com.mysql.cj.jdbc.Driver";
	public static final String mongo_url= "mongodb://root:root@192.169.7.20:27017/gm";

	private static Properties pros = new Properties();

	public static void initProperty(String[] args) {
		pros.setProperty("host", host);
		pros.setProperty("kafka.offset.commit.period.ms", kafka_offset_commit_period_ms);
		pros.setProperty("kafka.max.poll.records", kafka_max_pollrecords);
		pros.setProperty("kafka.port", kafka_port);
		pros.setProperty("redis.port", redis_port);
		pros.setProperty("redis.pwd", redis_pwd);
		pros.setProperty("mysql.url", mysql_url);
		pros.setProperty("mysql.username", mysql_username);
		pros.setProperty("mysql.pwd", mysql_pwd);
		pros.setProperty("mysql.driver", mysql_driver);
		pros.setProperty("mongo.url", mongo_url);
	}

	/**
	 * 读取配置文中的属性值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return pros.getProperty(key);
	}
}
