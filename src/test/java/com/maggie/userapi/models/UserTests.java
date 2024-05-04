package com.maggie.userapi.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTests {
    @Test
    void createUser() {
        User newUser = new User("Maggie", "maggie@email.com", "@Pass1234");
        assertEquals("Maggie", newUser.getUsername());
        assertEquals("maggie@email.com", newUser.getEmail());
        assertEquals("@Pass1234", newUser.getPassword());
    }

    @Test
    void checkUsernameLength() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("M", "maggie@email.com", "@Pass1234"));
        assertEquals(User.USERNAME_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkEmailFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie", "@Pass1234"));
        assertEquals(User.EMAIL_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkPasswordMinLength() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie@email.com", "@Pass1"));
        assertEquals(User.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkPasswordMaxLength() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie@email.com", "@Pass12222222222222222222222222222222222222222222222222"));
        assertEquals(User.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkPasswordHasUppercase() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie@email.com", "@pass1234"));
        assertEquals(User.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkPasswordHasLowercase() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie@email.com", "@PASS1234"));
        assertEquals(User.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkPasswordHasNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie@email.com", "@PASSpass"));
        assertEquals(User.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void checkPasswordHasSpecialCharacter() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("Maggie", "maggie@email.com", "PASSpass1"));
        assertEquals(User.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }
}
