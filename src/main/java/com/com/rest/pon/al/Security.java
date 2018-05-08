package com.com.rest.pon.al;

import com.al.pon.util.GenKeyUtil;
import com.al.pon.util.Validate;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/security")
public class Security {
    final static Logger logger = Logger.getLogger(Security.class);
    private Validate validate;

    public Security() {
        validate = new Validate();
    }

    // слушает гет запрос если логин , пароль не нулл и есть такой пользователь в базе то он аунтифицируется
    // возвращает токен
    @Path("/authenticate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@HeaderParam("login") String login,
                                 @HeaderParam ("password") String password) throws JsonGenerationException, JsonMappingException, IOException{
        if(login == null){
            logger.info("login == null");
            return validate.buildMessage("Username value is missing!!!");
        }else if(password == null){
            logger.info("password == null");
            return validate.buildMessage("Password value is missing!!!");
        }

        if(validate.checkUser(login, password) == null){
            logger.info("user == null, password or login no exist ");
            return validate.buildMessage("Access Denied for this functionality !!!");
        }

        String jwt = validate.buildClaims(validate.checkUser(login, password));
        logger.info("user authenticate ok");
        return Response.status(200).entity(jwt).build();

    }
}
