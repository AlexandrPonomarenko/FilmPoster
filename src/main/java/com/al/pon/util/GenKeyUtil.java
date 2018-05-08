package com.al.pon.util;

import org.apache.log4j.Logger;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

// Класс синглтон, служит для генирирования публичного ключа в который добаляется как айди сгенирирования
// секретного ключа для каждого пользователя при вводе логина и пароля
public class GenKeyUtil {
    final static Logger logger = Logger.getLogger(GenKeyUtil.class);
    private static List<JsonWebKey> jwkList = null;
    private static GenKeyUtil genKeyUtil = null;
    private static Random random;
    private static String key;
    private static final char[] ABS = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f',
            'g','h','j','k','l','z','x','c','v','b','n','m','1','2','3','4','5','6',
            '7','8','9','0',};

    private GenKeyUtil(){
        random = new Random();
    }

    public static GenKeyUtil getInstance(){
        if(genKeyUtil == null){
            genKeyUtil = new GenKeyUtil();
            jwkList = new LinkedList<>();
        }

        return genKeyUtil;
    }

    public static List getJWKList(){
        return jwkList;
    }

    public static JsonWebKey genKey(String useLogin){
        logger.info("build key");
        JsonWebKey jwk = null;
        key = "";
        for (int i = 0; i < ABS.length / 2; i++){
            key += ABS[random.nextInt(ABS.length)];
        }
        logger.info("key : " + key);
        try {
            jwk = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            logger.trace("error genKey " + e);
            e.printStackTrace();
        }
        jwk.setKeyId(String.valueOf(key));
        jwk.setUse(useLogin);
        jwkList.add(jwk);
        logger.info("return jwk : " + jwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY));
        return jwk;
    }

}
