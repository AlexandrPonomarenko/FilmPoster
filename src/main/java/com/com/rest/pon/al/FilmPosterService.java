package com.com.rest.pon.al;

import com.al.pon.util.Validate;
import com.mongo.daoImpl.MovieImple;
import com.mongo.entity.model.Movie;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

 //корневой путь
@Path("/movies")
public class FilmPosterService {
    final static Logger logger = Logger.getLogger(FilmPosterService.class);

    private MovieImple movieImple = new MovieImple();
    private Validate validate;
    public FilmPosterService(){
        validate = new Validate();
    }

    // слушает гет запрос принимает джейсон возвращает все фильмы
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Movie> getMovies(){
        List<Movie> movies = movieImple.getAll();
        System.out.println(movies.size());
        logger.info("get all movies");
        return movies;
    }

    // слушает на пут запрос возвращает одновленный фильм
    @PUT
    @Path("/{nameMovie}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateMovie(@PathParam("nameMovie") String nameMovie, Movie movie, @HeaderParam("token") String token, @HeaderParam("login") String login){
        if(token == null){
            logger.info("token == null");
            return validate.buildMessage("Access Denied for this functionality !!!");
        }

        if(!validate.checkValidateToken(token, login)){
            logger.info("token or login == null");
            return validate.buildMessage("Access Denied for this functionality !!!");
        }
        logger.info("update movies " + nameMovie);
        return Response.status(200).entity(movieImple.update(movie, nameMovie)).build();
    }

    // гет возвращает один фильм
    @GET
    @Path("/{nameMovie}")
    @Produces({MediaType.APPLICATION_JSON})
    public Movie getMovie(@PathParam("nameMovie") String nameMovie){
        logger.info("get one movie");
        return movieImple.getEntityByName(nameMovie);
    }

    // удаляет один фильм
    @DELETE
    @Path("/{nameMovie}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteMovie(@PathParam("nameMovie") String nameMovie, @HeaderParam("token") String token, @HeaderParam("login") String login){
        if(token == null){
            logger.info("token == null");
            return validate.buildMessage("Access Denied for this functionality !!!");
        }
        if(!validate.checkValidateToken(token, login)){
            logger.info("token or login == null");
            return validate.buildMessage("Access Denied for this functionality !!!");
        }
        movieImple.delete(nameMovie);
        logger.info("delete movie " +nameMovie);
        return Response.status(200).entity("success").build();
    }

}
