package com.epam.rd.java.basic.topic07.task02.db.entity;


import java.util.Objects;

public class Team {

	private int id;

	private String name;

	public Team(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Team(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Team{" +
				"name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Team team = (Team) o;
		return Objects.equals(name, team.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public static Team createTeam(String name) {
		return new Team(0, name);
	}
}