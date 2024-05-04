package com.maggie.userapi.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maggie.userapi.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    @Value("${jwtsecret}")
    String jwtSecret;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(60 * 60 * 6); // 6 hours
        String token = JWT.create()
                .withClaim("name", user.getUsername())
                .withSubject(user.getEmail())
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
