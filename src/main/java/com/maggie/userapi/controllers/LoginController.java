package com.maggie.userapi.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import com.maggie.userapi.session.JwtService;

record LoginForm(String email, String password) {
}

record LoginResponse(String token, String username) {
}

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginForm loginForm) {
        if (loginForm.email() == null || loginForm.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(loginForm.email());

        if (user != null && encoder.matches(loginForm.password(), user.getPassword())) {
            try {
                String token = jwtService.generateToken(user.getEmail(), user.getUsername());
                return new LoginResponse(token, user.getUsername());
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
