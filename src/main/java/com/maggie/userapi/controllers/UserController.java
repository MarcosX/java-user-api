package com.maggie.userapi.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import com.maggie.userapi.session.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

record UserResponse(String email, String username) {

}

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @GetMapping("/user")
    public UserResponse getUser(@RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader.contains("Bearer")) {
            String token = authHeader.split(" ")[1];
            try {
                DecodedJWT decodedJWT = jwtService.validateToken(token);

                String email = decodedJWT.getSubject();

                User user = userRepository.findByEmail(email);

                return new UserResponse(user.getEmail(), user.getUsername());

            } catch (JWTVerificationException exception) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
