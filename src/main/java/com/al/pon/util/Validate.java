package com.al.pon.util;

import com.com.rest.pon.al.Security;
import com.mongo.daoImpl.MovieImple;
import com.mongo.daoImpl.UserDaoImpl;
import com.mongo.entity.model.User;
import org.apache.log4j.Logger;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import javax.ws.rs.core.Response;

public class Validate {
    final static Logger logger = Logger.getLogger(Validate.class);
    private StatusMessage statusMessage;
    private UserDaoImpl userDao;


    public Validate() {
        statusMessage = new StatusMessage();
        userDao = new UserDaoImpl();
    }

    // возвращает код ошибки и сообщение или если незаригистрированный пользователь зашел на запрещенный ресурс
    public Response buildMessage(String mes){
        statusMessage.setStatus(Response.Status.PRECONDITION_FAILED.getStatusCode());
        statusMessage.setMessage(mes);
        return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode()).entity(statusMessage).build();

    }

    // проверка пользователя если такой в базе если есть возвращается, так же сравниваются еще пороль
    public User checkUser(String login, String password){
        User user;
        user = userDao.getUserByLogin(login);
        if(user != null && login.equals(user.getLogin()) && password.equals(user.getPassword())){
            logger.info("fin user " + user.toString());
            return user;
        }
        return null;
    }


    public String buildClaims(User user){
        JsonWebKey jwk =  GenKeyUtil.getInstance().genKey(user.getLogin());
        System.out.println(jwk.getKeyId());
        System.out.println(userDao.update(user, jwk.getKeyId()).toString());

        RsaJsonWebKey senderJwk = (RsaJsonWebKey) jwk;
        senderJwk.setKeyId(jwk.getKeyId());

        logger.info("JWK " + senderJwk.toJson());
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("filmposter.com");
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(user.getLogin());
        claims.setStringListClaim("roles", user.getRole());

        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(claims.toJson());

        jws.setKeyIdHeaderValue(senderJwk.getKeyId());
        jws.setKey(senderJwk.getPrivateKey());

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt = null;
        try{
            jwt = jws.getCompactSerialization();
        }catch (JoseException e){
            logger.error("error in buildClaims " + e);
            e.printStackTrace();
        }
        return jwt;
    }

    // проверка токена который пришел от пользователя
    public boolean checkValidateToken(String token, String login){
        if(GenKeyUtil.getJWKList().size() < 1){return false;}
        JsonWebKeySet jwks = new JsonWebKeySet(GenKeyUtil.getJWKList());
        JsonWebKey jwk = jwks.findJsonWebKey(userDao.getSecretKeyByLogin(login), null,login,null);
        logger.info("JWT token " + jwk.toJson());


        JwtConsumer jwtConsumer = new JwtConsumerBuilder().
                setRequireExpirationTime().
                setAllowedClockSkewInSeconds(30).
                setRequireSubject().
                setExpectedIssuer("filmposter.com").
                setVerificationKey(jwk.getKey()).build();

        try{
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            logger.info("JWT validation succeeded! " + jwtClaims);
            System.out.println("JWT validation succeeded! " + jwtClaims);
        }catch (InvalidJwtException e){
            logger.info("error validation token" + e);
            e.printStackTrace();
            return  false;
        }
        return true;

    }
}
