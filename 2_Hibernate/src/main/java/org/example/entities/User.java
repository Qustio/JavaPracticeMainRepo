package org.example.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public OffsetDateTime getCreatedAt() {
        return created_at;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", created_at=" + created_at +
                '}';
    }
}
