package com.maggie.userapi.controllers;

import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import com.maggie.userapi.session.JwtService;
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

record SignupResponse(String token, String username) {
}

@RestController
public class SignupController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");

    @PostMapping("/signup")
    @ResponseBody
    public SignupResponse signup(@RequestBody SignupForm signupForm) {
        if (userRepository.findByEmail(signupForm.email()) != null) {
            // Email already being used
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already being used.");
        }

        if (!emailPattern.matcher(signupForm.email()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }

        if (!passwordPattern.matcher(signupForm.password()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must have minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.");
        }

        if (signupForm.username().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must have at least 2 characters.");
        }

        String password = encoder.encode(signupForm.password());
        User newUser = userRepository.save(new User(signupForm.username(), signupForm.email(), password));

        String token = jwtService.generateToken(newUser);

        return new SignupResponse(token, newUser.getUsername());
    }
}
