package org.example.dao;

import org.example.entities.User;

import java.util.List;

public interface UserDAO {
    void save(User user);

    User findById(long id);

    List<User> findAll();

    void update(User user);

    void delete(User user);
}
