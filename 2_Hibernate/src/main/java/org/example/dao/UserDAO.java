package org.example.dao;

import org.example.Main;
import org.example.entityes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface UserDAO {
    void save(User user);

    User findById(long id);

    List<User> findAll();

    void update(User user);

    void delete(User user);
}
