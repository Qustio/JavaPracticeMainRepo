package org.example.services;

import events.UserEvent;
import org.example.dao.UserDAO;
import org.example.entities.User;
import org.example.exceptions.EmailExistsError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserDAO userDAO;
    private UserService userService;
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        userService = new UserService(userDAO, kafkaTemplate);
    }

    @Test
    void saveUserEmailFree() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.empty()); // email свободен

        userService.saveUser(user);

        verify(kafkaTemplate, times(1)).send(anyString(), any(UserEvent.class));

        verify(userDAO).save(user);


    }

    @Test
    void saveUserEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailExistsError.class, () -> userService.saveUser(user));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.saveUser(user); // or your controller method calling it
        });

        assertEquals("A user with email test@example.com already exists.", exception.getMessage());
        verify(userDAO, never()).save(any());
    }

    @Test
    void getUser() {
        User user = new User();

        long userId = 1L;

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        var result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(Optional.of(user), result);
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

        verify(userDAO).save(user);
    }

    @Test
    void deleteUser() {
        User user = new User();

        userService.deleteUser(user);

        verify(userDAO).delete(user);
    }
}