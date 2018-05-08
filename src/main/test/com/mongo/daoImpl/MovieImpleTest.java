package com.mongo.daoImpl;

import com.mongo.entity.model.Movie;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

import org.bson.BSONObject;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static com.mongodb.client.model.Filters.eq;

public class MovieImpleTest {

    private static MongoClient client;
    private static MongoDatabase database;
    private static MongoCollection<Document> mongoCollection;
    private static CodecRegistry codecRegistry;
    private Movie movie;

    @BeforeClass
    public static void testSetSetting(){
        client = new MongoClient("localhost", 27017);
    }

    @AfterClass
    public static void testDisableSetting(){
        client.close();
    }

    @Test
    public void testGetAll() throws IOException {
        System.out.println("Test - testGetAll STArt ");
        ObjectMapper mapper = new ObjectMapper();
        List<Movie> movies = new ArrayList<>();
        Movie movie;
        MongoCursor<Document> cur = mongoCollection.find().iterator();
        while (cur.hasNext()){
            movie = getConvertMovie(cur.next());
            movies.add(movie);
            System.out.println(movie.toString());
        }
        System.out.println("Test - testGetAll STArt ");
    }

    @Test
    public void testUpdate() {
        Movie movie = new Movie();
        movie.setDescriptionMovie("trtrtrtrtrtrtrtrt");
        movie.setTypeMovie("sex");
        movie.setNameMovie("Bigtre");


        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("$set", new BasicDBObject().append("descriptionMovie", movie.getDescriptionMovie())
                .append("nameMovies", movie.getNameMovie()).append("type", movie.getTypeMovie()));
        BasicDBObject searchQuery = new BasicDBObject().append("nameMovies", "Black");

        mongoCollection.updateOne(searchQuery, basicDBObject);

        System.out.println(mongoCollection.find(new BasicDBObject().append("nameMovies", movie.getNameMovie())).toString());
    }

    @Test
    public void testGetEntityById() {
        MongoDatabase mongoDatabase = client.getDatabase("movies");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("movies");
        BasicDBObject basicDBObject = new BasicDBObject("_id", new ObjectId("5aec02eda05fc4c94fc6f192"));
        Document document = mongoCollection.find(eq("_id", new ObjectId("5aec02eda05fc4c94fc6f192"))).first();
        System.out.println(document.toJson());
        assertEquals("5aec02eda05fc4c94fc6f192",document.get("_id").toString());
    }

    @Test
    public void testGetEntityByName() {

        MongoDatabase mongoDatabase = client.getDatabase("movies");
        MongoCollection mongoCollection = mongoDatabase.getCollection("movies");
        BasicDBObject basicDBObject = new BasicDBObject("nameMovies", "red");
        Document document = (Document) mongoCollection.find(basicDBObject).first();
        System.out.println(document.toJson());
        assertEquals("red",document.get("nameMovies"));
    }

    @Test
    public void testDelete() {

        mongoCollection.deleteOne(new Document().append("nameMovies", "Bigtre"));

    }

    @Ignore
    @Test
    public void testCreate() {
        database = client.getDatabase("movies").withCodecRegistry(buildCodecRegistry());
        MongoCollection<Movie> movieMongoCollection =  database.getCollection("movies", Movie.class);
        movie = new Movie();
        movie.setNameMovie("IronMan");
        movie.setTypeMovie("Hero");
        movie.setDescriptionMovie("SuperHero");
        movieMongoCollection.insertOne(movie);
    }

    public static CodecRegistry buildCodecRegistry(){
        codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return codecRegistry;
    }

    public Movie getConvertMovie(Document document){
        Movie movie = new Movie();
        movie.setIdMovie(document.getObjectId("_id").toString());
        movie.setNameMovie(document.getString("nameMovies"));
        movie.setTypeMovie(document.getString("type"));
        movie.setDescriptionMovie(document.getString("description"));
        return movie;
    }
}