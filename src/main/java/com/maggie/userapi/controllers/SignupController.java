package com.maggie.userapi.controllers;

import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

record SignupForm(String username, String email, String password) {
}

record SignupResponse(String token, User user) {
}

@RestController
public class SignupController {
    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    @ResponseBody
    public SignupResponse signup(@RequestBody SignupForm signupForm) {
        String password = encoder.encode(signupForm.password());
        User newUser = userRepository.save(new User(signupForm.username(), signupForm.email(), password));
        return new SignupResponse("token", newUser);
    }
}