package com.maggie.userapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

@Document("Users")
public class User {

    @Id
    private String id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;

    static final String USERNAME_ERROR_MESSAGE = "Username must have at least 2 characters.";
    static final String PASSWORD_ERROR_MESSAGE = "Password must have minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.";
    static final String EMAIL_ERROR_MESSAGE = "Invalid email format.";

    public User(String username, String email, String password) {
        if (username.trim().length() < 2) {
            throw new IllegalArgumentException(USERNAME_ERROR_MESSAGE);
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException(EMAIL_ERROR_MESSAGE);
        }

        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
        if (!passwordPattern.matcher(password).matches()) {
            throw new IllegalArgumentException(PASSWORD_ERROR_MESSAGE);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        this.username = username;
        this.email = email;
        this.password = encodedPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
