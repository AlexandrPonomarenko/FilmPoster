package com.mongo.daoImpl;

import com.mongo.dao.AbstractDao;
import com.mongo.entity.model.Movie;
import com.mongo.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MovieImple extends AbstractDao<Movie, String> {
    final static Logger logger = Logger.getLogger(MovieImple.class);
    // достаю базу из коннекшена и из базы коллекцию по имени
    private MongoDatabase mongoDatabase = MongoUtil.getMongoClientConnection().getDatabase("filmposter");
    private MongoCollection<Document> collection = mongoDatabase.getCollection("movies");

    public MovieImple(){

    }

    // достать из базы все фильмы
    @Override
    public List<Movie> getAll() {
        logger.info("get all movies");
        List<Movie> movies = new ArrayList<>();
        Movie movie;
        try {
            MongoCursor<Document> cur = collection.find().iterator();
            while (cur.hasNext()){
                movie = getConvertMovie(cur.next());
                movies.add(movie);
            }
        }catch (Exception e){
            logger.error("error in getAll" + e.getMessage());
        }
        return movies;
    }

    // обновить все поля фильма кроме айди и вернуть обновленный фильм
    @Override
    public Movie update(Movie entity, String name) {
        logger.info("update movies ");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("$set", new BasicDBObject().append("descriptionMovie", entity.getDescriptionMovie())
                .append("nameMovie", entity.getNameMovie()).append("typeMovie", entity.getTypeMovie()));
        BasicDBObject searchQuery = new BasicDBObject().append("nameMovie", name);

        collection.updateOne(searchQuery, basicDBObject);
        return getConvertMovie(collection.find(new BasicDBObject().append("nameMovie", entity.getNameMovie())).first());
    }

    // достать фильм по айди
    @Override
    public Movie getEntityById(String id) {
        logger.info("getEntityById movie ");
        Document document = collection.find(eq("_id", new ObjectId(id))).first();
        logger.info(document.toJson());
        return getConvertMovie(document);
    }

    // достать фильм по имени
    @Override
    public Movie getEntityByName(String name) {
        logger.info("getEntityByName movie ");
        BasicDBObject basicDBObject = new BasicDBObject("nameMovie", name);
        Document document = collection.find(basicDBObject).first();
        logger.info(document.toJson());
        return getConvertMovie(document);
    }

    // удалить по имени фильма
    @Override
    public boolean delete(String name) {
        if(name == null)
            return false;
        collection.deleteOne(new Document().append("nameMovie", name));
        logger.info("delete movie " + name);
        return true;
    }

    // не понадобился
    @Override
    public boolean create(Movie entity) {
        return false;
    }

    // конвертировать из типа документа в  тип фильм
    private Movie getConvertMovie(Document document){
        Movie movie = new Movie();
        movie.setIdMovie(document.getObjectId("_id").toString());
        movie.setNameMovie(document.getString("nameMovie"));
        movie.setTypeMovie(document.getString("typeMovie"));
        movie.setDescriptionMovie(document.getString("descriptionMovie"));
        logger.info("getConvertMovie " + movie.toString());
        return movie;
    }
}
