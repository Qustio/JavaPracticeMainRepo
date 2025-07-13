package org.example.services;

import events.UserEvent;
import org.example.dao.UserDAO;
import org.example.entities.User;
import org.example.exceptions.EmailExistsError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserDAO userDAO;

    @Autowired
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserService(UserDAO userDAO, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.userDAO = userDAO;
        this.kafkaTemplate = kafkaTemplate;
    }

    public User saveUser(User user) {
        var u = userDAO.findByEmail(user.getEmail());
        if (u.isPresent()) {
            throw new EmailExistsError(user.getEmail());
        }
        userDAO.save(user);
        var userEvent = new UserEvent(UserEvent.Operation.Created, user.getEmail());
        kafkaTemplate.send("user-notification-topic", userEvent);
        return user;
    }

    public Optional<User> getUser(long id) {
        return userDAO.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User updateUser(User user) {
        var existing = userDAO.findByEmail(user.getEmail());
        if (existing.isPresent() && existing.get().getId() != user.getId()) {
            throw new EmailExistsError(user.getEmail());
        }
        return userDAO.save(user);
    }

    public void deleteUser(User user) {
        var email = user.getEmail();
        userDAO.delete(user);
        var userEvent = new UserEvent(UserEvent.Operation.Deleted, email);
        kafkaTemplate.send("user-notification-topic", userEvent);
    }
}
