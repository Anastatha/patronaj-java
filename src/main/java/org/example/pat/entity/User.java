package org.example.pat.entity;

import org.example.pat.dto.user.CreateUserInput;
import org.example.pat.entity.consts.Categorie;
import org.example.pat.entity.consts.Label;
import org.example.pat.entity.consts.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String userTelegramId;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String organizationName;
    private String inn;
    private String phone;
    private String code;
    private Status status;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private Categorie category;
    private Long curatorId;
    private List<Label> label;
    private String okved;
    private Curator curator;

    public User() {
    }

    public void setCurator(Curator curator) {
        this.curator = curator;
    }

    public Curator getCurator() {
        return curator;
    }

    public User(CreateUserInput request) {
        this.name = request.getName(); // может быть null
        this.surname = request.getSurname(); // может быть null
        this.patronymic = request.getPatronymic(); // может быть null
        this.organizationName = request.getOrganizationName(); // может быть null
        this.inn = request.getInn();
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.category = request.getCategory();
        this.curatorId = request.getCuratorId();
        this.label = request.getLabel() != null ? request.getLabel() : new ArrayList<>();
    }

    public User(Long id, String userTelegramId, String name, String surname, String patronymic, String email, String organizationName, String inn, String phone, String code, Status status, LocalDateTime deletedAt, LocalDateTime updatedAt, Categorie category, Long curatorId, List<Label> label, String okved) {
        this.id = id;
        this.userTelegramId = userTelegramId;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.email = email;
        this.organizationName = organizationName;
        this.inn = inn;
        this.phone = phone;
        this.code = code;
        this.status = status;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.curatorId = curatorId;
        this.label = label;
        this.okved = okved;
    }

    public void setCode() {
        this.code = String.valueOf(System.currentTimeMillis()).substring(7);
    }

    public void setStatusDisabled() {
        this.status = Status.DISABLED;
    }

    public boolean isOkvedCategory() {
        return Categorie.INDIVIDUAL_ENTREPRENEUR.equals(category)
                || Categorie.ORGANIZATION.equals(category)
                || Categorie.INDIVIDUAl_INDIVIDUAL_ENTREPRENEUR.equals(category);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserTelegramId() {
        return userTelegramId;
    }

    public void setUserTelegramId(String userTelegramId) {
        this.userTelegramId = userTelegramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Categorie getCategory() {
        return category;
    }

    public void setCategory(Categorie category) {
        this.category = category;
    }

    public Long getCuratorId() {
        return curatorId;
    }

    public void setCuratorId(Long curatorId) {
        this.curatorId = curatorId;
    }

    public List<Label> getLabel() {
        return label;
    }

    public void setLabel(List<Label> label) {
        this.label = label;
    }

    public String getOkved() {
        return okved;
    }

    public void setOkved(String okved) {
        this.okved = okved;
    }
}
