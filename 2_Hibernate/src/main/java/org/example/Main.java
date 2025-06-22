package org.example;

import org.example.dao.UserDAO;
import org.example.dao.impl.UserDAOImpl;
import org.example.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        setUp();

        var user = new User("maxine", "max@email.com", 22, OffsetDateTime.now());
        UserDAO userDAO = new UserDAOImpl(sessionFactory);
        //userDAO.findAll().forEach(System.out::println);
        tui(userDAO);
    }

    static void tui(UserDAO userDAO) {
        while (true) {
            System.out.println("\nUser Management Console");
            System.out.println("1. Create user");
            System.out.println("2. List all users");
            System.out.println("3. Find user by ID");
            System.out.println("4. Update user");
            System.out.println("5. Delete user");
            System.out.println("0. Exit");
            System.out.print("Enter option: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> createUser(userDAO);
                case "2" -> listUsers(userDAO);
                case "3" -> findUserById(userDAO);
                case "4" -> updateUser(userDAO);
                case "5" -> deleteUser(userDAO);
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    static void createUser(UserDAO userDAO) {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Age: ");
        Integer age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age, OffsetDateTime.now());
        userDAO.save(user);
        System.out.println("User created with ID: " + user.getId());
    }

    static void listUsers(UserDAO userDAO) {
        List<User> users = userDAO.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (User u : users) {
            System.out.printf("%d: %s (%s), age %d, created at %s%n",
                    u.getId(), u.getName(), u.getEmail(), u.getAge(), u.getCreatedAt());
        }
    }

    static void findUserById(UserDAO userDAO) {
        System.out.print("Enter user ID: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userDAO.findById(id);
        if (user == null) {
            System.out.println("User not found.");
        } else {
            System.out.printf("%d: %s (%s), age %d, created at %s%n",
                    user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
        }
    }

    static void updateUser(UserDAO userDAO) {
        System.out.print("Enter user ID to update: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userDAO.findById(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("New name (" + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) user.setName(name);

        System.out.print("New email (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isBlank()) user.setEmail(email);

        System.out.print("New age (" + user.getAge() + "): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isBlank()) user.setAge(Integer.parseInt(ageStr));

        userDAO.update(user);
        System.out.println("User updated.");
    }

    static void deleteUser(UserDAO userDAO) {
        System.out.print("Enter user ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userDAO.findById(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        userDAO.delete(user);
        System.out.println("User deleted.");
    }

    static void setUp() {
        log.info("Setting up...");

        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(User.class)
                            .buildMetadata()
                            .buildSessionFactory();

        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we
            // had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            System.out.println("AAAAAAAAAAAAAAA");
        }
    }
}