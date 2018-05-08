package com.mongo.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.apache.log4j.Logger;


public class MongoUtil {
    final static Logger logger = Logger.getLogger(MongoUtil.class);
    private static final int PORT = 27017;
    private static final String HOST = "localhost";
    private static MongoClient client;

    public static MongoClient getMongoClientConnection(){
        if(client == null){
            try {
                client = new MongoClient(HOST, PORT);
                logger.info("have a connection");
            }catch (MongoException e){
                logger.error("error connection " + e.getMessage());
            }
        }
        return client;
    }
}
