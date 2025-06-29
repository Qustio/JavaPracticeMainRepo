package org.example;

import org.example.dao.UserDAO;
import org.example.dao.impl.UserDAOImpl;
import org.example.entities.User;
import org.example.exceptions.EmailExistsError;
import org.example.services.UserService;
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
        UserDAO userDAO = new UserDAOImpl(sessionFactory);
        var userService = new UserService(userDAO);
        tui(userService);
    }

    static void tui(UserService userService) {
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
                case "1" -> createUser(userService);
                case "2" -> listUsers(userService);
                case "3" -> findUserById(userService);
                case "4" -> updateUser(userService);
                case "5" -> deleteUser(userService);
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    static void createUser(UserService userService) {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Age: ");
        Integer age = Integer.parseInt(scanner.nextLine());

        var user = new User(name, email, age, OffsetDateTime.now());
        try {
            var u = userService.saveUser(user);
            System.out.println("User created with ID: " + u.getId());
        } catch (EmailExistsError e) {
            System.out.println("Email "+ e.email +" already exists");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    static void listUsers(UserService userService) {
        List<User> users;
        try {
            users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }
            for (User u : users) {
                System.out.printf("%d: %s (%s), age %d, created at %s%n",
                        u.getId(), u.getName(), u.getEmail(), u.getAge(), u.getCreated_at());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    static void findUserById(UserService userService) {
        System.out.print("Enter user ID: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userService.getUser(id);
        if (user == null) {
            System.out.println("User not found.");
        } else {
            System.out.printf("%d: %s (%s), age %d, created at %s%n",
                    user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreated_at());
        }
    }

    static void updateUser(UserService userService) {
        System.out.print("Enter user ID to update: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = null;
        try {
            user = userService.getUser(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

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

        try {
            userService.updateUser(user);
            System.out.println("User updated.");
        } catch (EmailExistsError e) {
            System.out.println("Email "+ e.email +" already exists");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    static void deleteUser(UserService userService) {
        System.out.print("Enter user ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());
        try {
            User user = userService.getUser(id);
            if (user == null) {
                System.out.println("User not found.");
                return;
            }
            userService.deleteUser(user);
            System.out.println("User deleted.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    static void setUp() {
        log.info("Setting up...");

        final StandardServiceRegistry registry =
            new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .buildMetadata()
                .buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}