package com.maggie.userapi.controllers;

import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

record LoginForm(String username, String password) { }
record LoginResponse(String token) { }

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepository;


    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginForm loginForm) {
        User user = userRepository.findByUsername(loginForm.username());

        if (user!= null && loginForm.password().equals(user.getPassword())) {
            return new LoginResponse("token");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
