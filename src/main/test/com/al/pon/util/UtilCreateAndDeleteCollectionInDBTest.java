package com.al.pon.util;

import com.mongo.entity.model.Movie;
import com.mongo.entity.model.User;
import com.mongo.util.MongoUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.Assert.*;

public class UtilCreateAndDeleteCollectionInDBTest {
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private  CodecRegistry codecRegistry;

    @BeforeClass
    public static void testSetSetting(){
        mongoClient = new MongoClient("localhost", 27017);
        mongoDatabase = MongoUtil.getMongoClientConnection().getDatabase("filmposter");
        mongoDatabase.createCollection("users");
        mongoDatabase.createCollection("movies");
    }

    @AfterClass
    public static void testClearSetting(){
        MongoCollection collection = mongoDatabase.getCollection("users");
        collection.drop();
        collection = mongoDatabase.getCollection("movies");
        collection.drop();
        mongoClient.close();
    }

    @Test
    public void testCreateUser(){
        MongoDatabase mongoDatabase = mongoClient.getDatabase("filmposter").withCodecRegistry(testbuildCodecRegistry());
        MongoCollection<User> mongoCollection = mongoDatabase.getCollection("users", User.class);
        mongoCollection.insertOne(new User("user1", "loginu1", "user", "111", "111"));
        mongoCollection.insertOne(new User("user1", "loginu2", "user", "222", "222"));
        mongoCollection.insertOne(new User("admin1", "logina1", "admin", "333", "333"));
        mongoCollection.insertOne(new User("admin2", "logina2", "admin", "444", "444"));
        MongoCursor<User> cursor = mongoCollection.find().iterator();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toString());
        }
    }

    @Test
    public void testCreateMovies(){
        MongoDatabase mongoDatabase = mongoClient.getDatabase("filmposter").withCodecRegistry(testbuildCodecRegistry());
        MongoCollection<Movie> movieMongoCollection =  mongoDatabase.getCollection("movies", Movie.class);
        Movie movie;
        for(int i = 0; i < 3; i++){
            movie = new Movie();
            movie.setNameMovie("ironman" + i);
            movie.setTypeMovie("blockbuster" + i);
            movie.setDescriptionMovie("best super-pupper movie" + i);
            movieMongoCollection.insertOne(movie);
            System.out.println(movie.toString());
        }
    }

    public CodecRegistry testbuildCodecRegistry() {
        codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return codecRegistry;
    }
}