package com.epam.rd.java.basic.topic07.task02.db.entity;

import java.util.Objects;

public class User {

	private int id;

	private String login;

	public String getLogin(){
		return login;
	}

	public User(String login) {
		this.login = login;
	}

	public User(int id, String login) {
		this.id = id;
		this.login = login;
	}

	@Override
	public String toString() {
		return "User{" +
				"login='" + login + '\'' +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(login, user.login);
	}

	public static User createUser(String login){
		return new User(0, login);

	}
}