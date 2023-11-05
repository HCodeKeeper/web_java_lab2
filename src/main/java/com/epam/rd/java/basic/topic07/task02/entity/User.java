package com.epam.rd.java.basic.topic07.task02.entity;

import java.util.Objects;

public class User {
    private int id;
    private String login;

    // Constructor made private to enforce the use of the factory method
    private User(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    // ID setter, if you need to change the ID after creation, though it's not typical for an ID to change.
    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // Factory method for creating a User
    public static User createUser(String login) {
        return new User(0, login); // Assuming id = 0 for a non-persisted user
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }


    // getters and setters for id if needed
}
