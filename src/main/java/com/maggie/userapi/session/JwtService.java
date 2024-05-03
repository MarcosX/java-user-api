package com.maggie.userapi.session;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {

    @Value("${jwtsecret}")
    String jwtSecret;

    public String generateToken(String email, String username) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(60 * 60 * 6); // 6 hours
        String token = JWT.create()
                .withClaim("name", username)
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
        return token;
    }

    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}
