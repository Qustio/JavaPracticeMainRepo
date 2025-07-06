package org.example.services;

import org.example.dao.UserDAO;
import org.example.entities.User;
import org.example.exceptions.EmailExistsError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User saveUser(User user) {
        var u = userDAO.findByEmail(user.getEmail());
        if (u.isPresent()) {
            throw new EmailExistsError(user.getEmail());
        }
        return userDAO.save(user);
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
        userDAO.delete(user);
    }
}
