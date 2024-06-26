package com.maggie.userapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Users")
public class User {

    @Id
    private String id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private List<String> activeSessions;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

    public List<String> getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(List<String> activeSessions) {
        this.activeSessions = activeSessions;
    }
}
