package com.mongo.entity.model;

public class User {
    private String idUser;
    private String name;
    private String login;
    private String role;
    private String password;
    private String passwordRepeat;
    private String key;


    public User() {
    }

    public User(String name, String login, String role, String password, String passwordRepeat) {
        this.name = name;
        this.login = login;
        this.role = role;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String id) {
        this.idUser = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", passwordRepeat='" + passwordRepeat + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
