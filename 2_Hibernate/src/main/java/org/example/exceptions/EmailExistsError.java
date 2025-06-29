package org.example.exceptions;

public class EmailExistsError extends RuntimeException {
    public final String email;

    public EmailExistsError(String email) {
        super("A user with email " + email + " already exists.");
        this.email = email;
    }
}
