package com.cmput301f21t34.habittrak;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

import com.cmput301f21t34.habittrak.user.User;

/**
 * Class for testing Habit Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-11-03
 * @see DatabaseManager
 */
public class DatabaseManagerTest {
    /**
     * mockUser
     *
     * @return User
     * Returns a new User object to perform reading from and writing to the databasae
     */
    private User mockUser(String email, String username, String password) {
        User mockUser = new User(email);
        mockUser.setUsername(username);
        return mockUser;
    }

    /**
     * createNewUser
     *
     * @author Henry
     */
    @Test
    public void getUserTest() {
        DatabaseManager db = new DatabaseManager();
        User user = db.getUser("ddvu@ualberta.ca");
        assertEquals("henry", user.getUsername());
    }
}

