package com.maggie.userapi.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {
    @Value("${jwtsecret}")
    String jwtSecret;

    @GetMapping("/hello")
    public String hello(@RequestHeader(value = "Authorization") String authHeader,
            @RequestParam(value = "name", defaultValue = "World") String name) {
        if (authHeader.contains("Bearer")) {
            String token = authHeader.split(" ")[1];
            try {
                DecodedJWT decodedJWT;
                Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
                JWTVerifier verifier = JWT.require(algorithm)
                        .build();

                decodedJWT = verifier.verify(token);

                return "Hello " + name + "!" + " Your email is " + decodedJWT.getClaim("sub").asString() + "!";
            } catch (JWTVerificationException exception) {
                exception.printStackTrace();
                return "Hello " + name + "! Could not read your token.";
            }
        }
        return "Hello " + name + "!";
    }
}
