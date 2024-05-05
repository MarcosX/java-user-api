package com.maggie.userapi.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maggie.userapi.models.User;
import com.maggie.userapi.repositories.UserRepository;
import com.maggie.userapi.session.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

record UserResponse(String email, String username) {

}

record UpdateForm(String username, String password) {

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

                if (!user.getActiveSessions().contains(token)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }

                return new UserResponse(user.getEmail(), user.getUsername());
            } catch (JWTVerificationException exception) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/user")
    public UserResponse updateUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody UpdateForm updateForm) {
        if (authHeader.contains("Bearer")) {
            String token = authHeader.split(" ")[1];
            try {
                DecodedJWT decodedJWT = jwtService.validateToken(token);

                String email = decodedJWT.getSubject();

                User userToBeUpdated = userRepository.findByEmail(email);

                if (!userToBeUpdated.getActiveSessions().contains(token)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }

                if (updateForm.username().trim().length() < 2) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must have at least 2 characters.");
                }

                if (updateForm.password() != null && !updateForm.password().isEmpty()) {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                    Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");

                    if (!passwordPattern.matcher(updateForm.password()).matches()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must have minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.");
                    }

                    String password = encoder.encode(updateForm.password());
                    userToBeUpdated.setPassword(password);
                }

                userToBeUpdated.setUsername(updateForm.username());

                userRepository.save(userToBeUpdated);

                return new UserResponse(userToBeUpdated.getEmail(), userToBeUpdated.getUsername());

            } catch (JWTVerificationException exception) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
