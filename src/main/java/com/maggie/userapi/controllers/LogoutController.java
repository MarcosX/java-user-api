package com.maggie.userapi.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import com.maggie.userapi.session.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LogoutController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/logout")
    @ResponseBody
    public String logout(@RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader.contains("Bearer")) {
            String token = authHeader.split(" ")[1];
            try {
                DecodedJWT decodedJWT = jwtService.validateToken(token);

                String email = decodedJWT.getSubject();

                User user = userRepository.findByEmail(email);
                user.getActiveSessions().remove(token);
                userRepository.save(user);
                return "User logged out";
            } catch (JWTVerificationException exception) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
