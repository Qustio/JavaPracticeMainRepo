package org.example.dao.impl;

import org.example.entities.User;
import org.example.exceptions.DataAccessException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    static SessionFactory sessionFactory;
    static UserDAOImpl userDAO;

    @BeforeAll
    static void setup() {
        postgres.start();

        Configuration cfg = new Configuration()
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "true")
                .addAnnotatedClass(User.class);

        sessionFactory = cfg.buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
    }

    @AfterAll
    static void cleanup() {
        sessionFactory.close();
        postgres.stop();
    }

    @BeforeEach
    void cleanDatabase() {
        try (var session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        }
    }

    @org.junit.jupiter.api.Test
    void save() {
        User user = new User();
        user.setEmail("a@example.com");
        user.setName("Test");
        user.setAge(33);

        userDAO.save(user);

        try (Session session = sessionFactory.openSession()) {
            var list = session.createQuery("FROM User", User.class).list();
            assertNotNull(list);
            assertEquals(list.getFirst(), user);

        }
    }

    @org.junit.jupiter.api.Test
    void findById() {
        User user = new User();
        user.setEmail("a@example.com");
        user.setName("Test");
        user.setAge(33);

        var saved_user = userDAO.save(user);

        try (Session session = sessionFactory.openSession()) {
            var result = session.find(User.class, saved_user.getId());
            assertNotNull(result);
            assertEquals(result, saved_user);

        }
    }

    @org.junit.jupiter.api.Test
    void findAll() {
        User user1 = new User();
        user1.setEmail("a@example.com");
        user1.setName("Test");
        user1.setAge(33);

        userDAO.save(user1);

        User user2 = new User();
        user2.setEmail("b@example.com");
        user2.setName("toast");
        user2.setAge(22);

        userDAO.save(user2);

        try (Session session = sessionFactory.openSession()) {
            var list = session.createQuery("FROM User", User.class).list();
            assertNotNull(list);
            assertEquals(list, Arrays.asList(user1, user2));
        }
    }

    @org.junit.jupiter.api.Test
    void update() {
        User user = new User();
        user.setEmail("a@example.com");
        user.setName("Test");
        user.setAge(33);

        var saved_user = userDAO.save(user);

        saved_user.setAge(99);
        userDAO.update(saved_user);

        try (Session session = sessionFactory.openSession()) {
            var result = session.find(User.class, saved_user.getId());
            assertNotNull(result);
            assertEquals(saved_user, result);
        }
    }

    @org.junit.jupiter.api.Test
    void delete() {
        User user1 = new User();
        user1.setEmail("a@example.com");
        user1.setName("Test");
        user1.setAge(33);

        userDAO.save(user1);

        User user2 = new User();
        user2.setEmail("b@example.com");
        user2.setName("toast");
        user2.setAge(22);

        userDAO.save(user2);

        userDAO.delete(user1);

        try (Session session = sessionFactory.openSession()) {
            var list = session.createQuery("FROM User", User.class).list();
            assertNotNull(list);
            assertEquals(list, List.of(user2));
        }
    }
}