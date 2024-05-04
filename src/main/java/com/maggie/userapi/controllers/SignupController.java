package com.maggie.userapi.controllers;

import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import com.maggie.userapi.session.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

record SignupForm(String username, String email, String password) {
}

record SignupResponse(String token, String username) {
}

@RestController
public class SignupController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/signup")
    @ResponseBody
    public SignupResponse signup(@RequestBody SignupForm signupForm) {
        if (userRepository.findByEmail(signupForm.email()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already being used.");
        }

        try {
            User newUser = userRepository.save(new User(signupForm.username(), signupForm.email(), signupForm.password()));

            String token = jwtService.generateToken(newUser);

            return new SignupResponse(token, newUser.getUsername());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
}
