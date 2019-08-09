package com.gm.util;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.mongodb.client.MongoDatabase;

//mongodb 连接数据库工具类
public class MongoUtil {
    public static MongoDatabase getConnect(){
        MongoClientURI uri = new MongoClientURI(PropertyUtil.mongo_url);
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        return db;
    }
}


