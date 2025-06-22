package org.example.dao.impl;

import org.example.Main;
import org.example.dao.UserDAO;
import org.example.entityes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.List;

public class UserDAOImpl implements org.example.dao.UserDAO {
    private final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.info("Created UserDAO");
    }

    @Override
    public void save(User user) {
        log.info("Saving user {}", user);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            log.error("Error saving user", e);
        }
    }

    @Override
    public User findById(long id) {
        log.info("Getting user by id {}", id);
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        } catch (Exception e) {
            log.error("Error getting user", e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        log.info("Getting all users");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            log.error("Error getting all users", e);
        }
        return null;
    }

    @Override
    public void update(User user) {
        log.info("Updating user {}", user);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user); // or session.update(user)
            tx.commit();
        } catch (Exception e) {
            log.error("Error updating user", e);
        }
    }

    @Override
    public void delete(User user) {
        log.info("Deleting user {}", user);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(user);
            tx.commit();
        } catch (Exception e) {
            log.error("Error deleting user", e);
        }
    }
}