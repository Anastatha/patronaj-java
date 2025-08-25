package org.example.pat.entity;

import org.example.pat.entity.consts.Roles;

import java.time.LocalDateTime;

public class Curator {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String patronymic;
    private String surname;
    private String phone;
    private LocalDateTime deletedAt;
    private Roles role;
    private User user;

    public Curator() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Curator(Long id, String email, String password, String name, String patronymic, String surname, String phone, LocalDateTime deletedAt, Roles role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.deletedAt = deletedAt;
        this.role = role;
        this.surname = surname;
    }

    public Curator(String email, String password, String name, String patronymic, String surname, String phone, Roles role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.role = role;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
