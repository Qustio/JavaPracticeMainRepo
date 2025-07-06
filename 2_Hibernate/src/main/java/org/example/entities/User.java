package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private Integer age;

    @Column
    private OffsetDateTime created_at;

    public User() {

    }

    public User(String name, String email, Integer age, OffsetDateTime created_at) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.created_at = created_at;
    }
}
