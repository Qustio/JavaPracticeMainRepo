package org.example.services;

import org.example.dao.UserDAO;
import org.example.dao.impl.UserDAOImpl;
import org.example.entities.User;
import org.example.exceptions.DataAccessException;
import org.example.exceptions.EmailExistsError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User saveUser(User user) {
        var u = userDAO.findByEmail(user.getEmail());
        if (u != null) {
            throw new EmailExistsError(user.getEmail());
        }
        return userDAO.save(user);
    }

    public User getUser(long id) {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void updateUser(User user) {
        var u = userDAO.findByEmail(user.getEmail());
        if (u != null) {
            throw new EmailExistsError(user.getEmail());
        }
        userDAO.update(user);
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }
}
