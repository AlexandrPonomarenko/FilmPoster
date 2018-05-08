package com.mongo.daoImpl;

import com.mongo.dao.AbstractDao;
import com.mongo.dao.InterfaceDaoUser;
import com.mongo.entity.model.Movie;
import com.mongo.entity.model.User;
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

public class UserDaoImpl extends AbstractDao<User, String>  implements InterfaceDaoUser{
    final static Logger logger = Logger.getLogger(UserDaoImpl.class);
    private MongoDatabase mongoDatabase = MongoUtil.getMongoClientConnection().getDatabase("filmposter");
    private MongoCollection<Document> collection = mongoDatabase.getCollection("users");

    public UserDaoImpl() {
    }

    // Достать всех юзеров из базы
    @Override
    public List<User> getAll() {
        System.out.println("start getAll");
        logger.info("in getAll Users");
        List<User> users = new ArrayList<>();
        User user;
        try {
            MongoCursor<Document> cur = collection.find().iterator();
            while (cur.hasNext()){
                user = getConvertMovie(cur.next());
                users.add(user);
            }
        }catch (Exception e){
            logger.error(" error getAll " + e.getMessage());
        }
        return users;
    }

    // обновить юзера, добавить юзеру при авторизации ключ
    @Override
    public User update(User entity, String key) {
        logger.info("UPDATE User.. set key " + key);
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("$set", new BasicDBObject().append("key", key));

        BasicDBObject searchQuery = new BasicDBObject().append("login", entity.getLogin());

        collection.updateOne(searchQuery, basicDBObject);
        return getConvertMovie(collection.find(new BasicDBObject().append("login", entity.getLogin())).first());
    }

    // достать по айди
    @Override
    public User getEntityById(String id) {
        logger.info("getEntityById ");
        Document document = collection.find(eq("_id", new ObjectId(id))).first();
        logger.info(document.toJson());
        return getConvertMovie(document);
    }

    // достать по имени
    @Override
    public User getEntityByName(String name) {
        logger.info("getEntityByName ");
        BasicDBObject basicDBObject = new BasicDBObject("name", name);
        Document document = collection.find(basicDBObject).first();
        logger.info(document.toJson());
        return getConvertMovie(document);
    }

    // удалить по имени
    @Override
    public boolean delete(String name) {
        if(name == null)
            return false;
        collection.deleteOne(new Document().append("name", name));
        logger.info("delete users " + name);
        return true;
    }

    // создать юзера но этот метод не понадобился поэтому возвращаяет нулл
    @Override
    public boolean create(User entity) {
        return false;
    }

    // достать по логину
    @Override
    public User getUserByLogin(String login) {
        logger.info("getUserByLogin " + login);
        BasicDBObject basicDBObject = new BasicDBObject("login", login);
        Document document = collection.find(basicDBObject).first();
        logger.info(document.toJson());
        return getConvertMovie(document);
    }

    // достать по логину ключ
    @Override
    public String getSecretKeyByLogin(String login) {
        logger.info("getSecretKeyByLogin " + login);
        BasicDBObject basicDBObject = new BasicDBObject("login", login);
        Document document = collection.find(basicDBObject).first();
        logger.info(document.toJson());
        return document.getString("key");
    }

    // конвертировать из документа в юзера, да это одноразовый вариант, нужно перевести джейсон врапером, спешил
    private User getConvertMovie(Document document){
        User user = new User();
        user.setIdUser(document.getObjectId("_id").toString());
        user.setName(document.getString("name"));
        user.setKey(document.getString("key"));
        user.setLogin(document.getString("login"));
        user.setRole(document.getString("role"));
        user.setPassword(document.getString("password"));
        user.setPasswordRepeat(document.getString("passwordRepeat"));
        logger.info(user.toString());
        return user;
    }


}
