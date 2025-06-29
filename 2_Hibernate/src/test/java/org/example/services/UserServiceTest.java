package org.example.services;

import org.example.dao.UserDAO;
import org.example.entities.User;
import org.example.exceptions.EmailExistsError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserService(userDAO);
    }

    @Test
    void saveUserEmailFree() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userDAO.findByEmail("test@example.com")).thenReturn(null); // email свободен

        userService.saveUser(user);

        verify(userDAO).save(user);
    }

    @Test
    void saveUserEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userDAO.findByEmail("test@example.com")).thenReturn(new User());

        assertThrows(EmailExistsError.class, () -> userService.saveUser(user));

        verify(userDAO, never()).save(any());
    }

    @Test
    void getUser() {
        User user = new User();

        long userId = 1L;

        when(userDAO.findById(userId)).thenReturn(user);

        User result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userDAO).findById(userId);
    }

    @Test
    void getAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userDAO.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userDAO).findAll();
    }

    @Test
    void updateUser() {
        User user = new User();

        userService.updateUser(user);

        verify(userDAO).update(user);
    }

    @Test
    void deleteUser() {
        User user = new User();
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userDAO.findAll()).thenAnswer(invocation -> new ArrayList<>(users));
        doAnswer(invocation -> {
            users.remove(user);
            return null;
        }).when(userDAO).delete(user);

        userService.deleteUser(user);
        var result = userService.getAllUsers();

        assertEquals(0, result.size());
    }
}