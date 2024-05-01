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

import java.util.regex.Pattern;

record SignupForm(String username, String email, String password) {
}

record SignupResponse(String token, User user) {
}

@RestController
public class SignupController {
    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping("/signup")
    @ResponseBody
    public SignupResponse signup(@RequestBody SignupForm signupForm) {
        if (userRepository.findByEmail(signupForm.email()) != null) {
            // Email already being used
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        
        if (!emailPattern.matcher(signupForm.email()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String password = encoder.encode(signupForm.password());
        User newUser = userRepository.save(new User(signupForm.username(), signupForm.email(), password));
        return new SignupResponse("token", newUser);
    }
}
