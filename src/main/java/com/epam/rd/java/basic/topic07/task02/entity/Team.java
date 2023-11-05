package com.epam.rd.java.basic.topic07.task02.entity;

import java.util.Objects;

public class Team {
    private int id;
    private String name;

    // Constructor made private to enforce the use of the factory method
    private Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    // ID setter, if needed.
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Factory method for creating a Team
    public static Team createTeam(String name) {
        return new Team(0, name); // Assuming id = 0 for a non-persisted team
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
