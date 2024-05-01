package com.maggie.userapi.controllers;

import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

record LoginForm(String email, String password) {
}

record LoginResponse(String token, User user) {
}

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginForm loginForm) {
        if (loginForm.email() == null || loginForm.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(loginForm.email());

        if (user != null && encoder.matches(loginForm.password(), user.getPassword())) {
            return new LoginResponse("token", user);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
