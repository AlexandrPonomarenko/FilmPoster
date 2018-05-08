package com.mongo.dao;

import com.mongo.entity.model.User;

public interface InterfaceDaoUser {
    User getUserByLogin(String login);
    String getSecretKeyByLogin(String key);
}
