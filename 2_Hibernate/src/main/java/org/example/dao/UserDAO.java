package org.example.dao;

import org.example.entities.User;

import java.util.List;

public interface UserDAO {
    User save(User user);

    User findById(long id);

    User findByEmail(String email);

    List<User> findAll();

    void update(User user);

    void delete(User user);
}
