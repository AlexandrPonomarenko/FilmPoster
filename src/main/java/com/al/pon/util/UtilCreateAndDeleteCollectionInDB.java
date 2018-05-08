package com.al.pon.util;

import com.mongo.entity.model.Movie;
import com.mongo.entity.model.User;
import com.mongo.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

// Класс создает две коллекции и заполняет их стартовыми значениями, при окончании работы сервера он удаляет их
public class UtilCreateAndDeleteCollectionInDB {
    private MongoDatabase mongoDatabase = MongoUtil.getMongoClientConnection().getDatabase("filmposter").withCodecRegistry(buildCodecRegistry());
    private static CodecRegistry codecRegistry;

    public UtilCreateAndDeleteCollectionInDB(){
        createCollections();
        setMovies();
        setUsers();
    }

    private void createCollections(){
        mongoDatabase.createCollection("users");
        mongoDatabase.createCollection("movies");
    }

    public void dropCollections(){
        MongoCollection collection = mongoDatabase.getCollection("users");
        collection.drop();
        collection = mongoDatabase.getCollection("movies");
        collection.drop();
    }

    private void setMovies(){
        MongoCollection<Movie> movieMongoCollection =  mongoDatabase.getCollection("movies", Movie.class);
        Movie movie;
        for(int i = 0; i < 3; i++){
            movie = new Movie();
            movie.setNameMovie("ironman" + i);
            movie.setTypeMovie("blockbuster" + i);
            movie.setDescriptionMovie("best super-pupper movie" + i);
            movieMongoCollection.insertOne(movie);
        }
    }

    private void setUsers(){
        MongoCollection<User> userMongoCollection =  mongoDatabase.getCollection("users", User.class);
        userMongoCollection.insertOne(new User("user1", "loginu1", "user", "111", "111"));
        userMongoCollection.insertOne(new User("user1", "loginu2", "user", "222", "222"));
        userMongoCollection.insertOne(new User("admin1", "logina1", "admin", "333", "333"));
        userMongoCollection.insertOne(new User("admin2", "logina2", "admin", "444", "444"));
    }

    public static CodecRegistry buildCodecRegistry(){
        codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return codecRegistry;
    }
}
