package com.mongo.entity.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="movie")
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {
    private String idMovie;

    private String nameMovie;


    private String typeMovie;


    private String descriptionMovie;

    public Movie() {
    }

    public Movie(String nameMovie, String typeMovie, String descriptionMovie) {
        this.nameMovie = nameMovie;
        this.typeMovie = typeMovie;
        this.descriptionMovie = descriptionMovie;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String id) {
        this.idMovie = id;
    }

    public String getNameMovie() {
        return nameMovie;
    }

    public void setNameMovie(String nameMovie) {
        this.nameMovie = nameMovie;
    }

    public String getTypeMovie() {
        return typeMovie;
    }

    public void setTypeMovie(String typeMovie) {
        this.typeMovie = typeMovie;
    }

    public String getDescriptionMovie() {
        return descriptionMovie;
    }

    public void setDescriptionMovie(String descriptionMovie) {
        this.descriptionMovie = descriptionMovie;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "idMovie='" + idMovie + '\'' +
                ", nameMovie='" + nameMovie + '\'' +
                ", typeMovie='" + typeMovie + '\'' +
                ", descriptionMovie='" + descriptionMovie + '\'' +
                '}';
    }
}
