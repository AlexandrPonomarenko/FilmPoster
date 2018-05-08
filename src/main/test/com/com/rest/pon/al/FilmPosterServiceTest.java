package com.com.rest.pon.al;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class FilmPosterServiceTest {

    private static List<JsonWebKey> jwkList = null;

    @BeforeClass
    public static void setSetting(){
        jwkList = new LinkedList<>();
    }

    @Test
    public void testGenRSAKey(){
        System.out.println("init... ");
        for (int kid = 1; kid <= 3; kid++) {
            JsonWebKey jwk = null;
            try{
                jwk = RsaJwkGenerator.generateJwk(2048);
                System.out.println("PUBLIC KEY (" + kid + "): " + jwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY));
            }catch (JoseException e){
                e.printStackTrace();
            }
            jwk.setKeyId(String.valueOf(kid));
            jwkList.add(jwk);
            System.out.println();
        }
        for(JsonWebKey s : jwkList){
            System.out.println(s);
        }
    }
}