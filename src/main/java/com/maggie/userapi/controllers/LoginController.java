package com.maggie.userapi.controllers;

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
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginForm loginForm) {
        if (loginForm.username().equals("maggie") && loginForm.password().equals("pass123")) {
            return new LoginResponse("token");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
