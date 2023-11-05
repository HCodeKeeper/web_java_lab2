package com.epam.rd.java.basic.topic07.task02.db;


import com.epam.rd.java.basic.topic07.task02.Constants;
import com.epam.rd.java.basic.topic07.task02.db.entity.Team;
import com.epam.rd.java.basic.topic07.task02.db.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBManager {

	private static final String INSERT_USER_SQL = "INSERT INTO users(login) VALUES(?)";
	private static final String INSERT_TEAM_SQL = "INSERT INTO teams(name) VALUES(?)";
	private static final String FIND_ALL_USERS_SQL = "SELECT * FROM users";
	private static final String FIND_ALL_TEAMS_SQL = "SELECT * FROM teams";
	private static final String ASSIGN_TEAMS_SQL = "INSERT INTO users_teams(user_id, team_id) VALUES(?, ?)";

	private static DBManager instance;
	private String connectionUrl;

	private DBManager() {
		try (InputStream input = DBManager.class.getClassLoader().getResourceAsStream(Constants.SETTINGS_FILE)) {
			Properties prop = new Properties();
			if (input == null) {
				throw new IllegalStateException("Sorry, unable to find config.properties");
			}
			prop.load(input);
			connectionUrl = prop.getProperty("connection.url");
		} catch (Exception ex) {
			throw new RuntimeException("Error loading properties file", ex);
		}
	}

	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connectionUrl);
	}

	public void insertUser(User user) throws SQLException {
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, user.getLogin());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
		}
	}

	public List<User> findAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_ALL_USERS_SQL);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				User user = new User(rs.getString("login"));
				user.setId(rs.getInt("id"));
				users.add(user);
			}
		}
		return users;
	}

	public void insertTeam(Team team) throws SQLException {
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement(INSERT_TEAM_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, team.getName());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating team failed, no rows affected.");
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					team.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating team failed, no ID obtained.");
				}
			}
		}
	}

	public List<Team> findAllTeams() throws SQLException {
		List<Team> teams = new ArrayList<>();
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_ALL_TEAMS_SQL);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Team team = new Team(rs.getString("name"));
				team.setId(rs.getInt("id"));
				teams.add(team);
			}
		}
		return teams;
	}

	public void setTeamsForUser(User user, Team... teams) throws SQLException {
		Connection connection = null;
		PreparedStatement psInsertUT = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false); // Start transaction

			// Delete existing user_team relationships
			try (PreparedStatement psDeleteUT = connection.prepareStatement("DELETE FROM users_teams WHERE user_id = ?")) {
				psDeleteUT.setInt(1, user.getId());
				psDeleteUT.executeUpdate();
			}

			// Insert new user_team relationships
			String insertUserTeamSQL = "INSERT INTO users_teams (user_id, team_id) VALUES (?, ?)";
			psInsertUT = connection.prepareStatement(insertUserTeamSQL);

			for (Team team : teams) {
				psInsertUT.setInt(1, user.getId());
				psInsertUT.setInt(2, team.getId());
				psInsertUT.executeUpdate();
			}

			connection.commit(); // Commit the transaction
		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback(); // Rollback the transaction on error
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			throw e; // Re-throw the exception to be handled elsewhere
		} finally {
			if (psInsertUT != null) {
				try {
					psInsertUT.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.setAutoCommit(true); // Reset auto-commit to true
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public User getUser(String login) throws SQLException {
		User user = null;
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE login = ?")) {
			ps.setString(1, login);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					user = new User(rs.getString("login"));
					user.setId(rs.getInt("id"));
				}
			}
		}
		return user;
	}

	public Team getTeam(String name) throws SQLException {
		Team team = null;
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement("SELECT * FROM teams WHERE name = ?")) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					team = new Team(rs.getString("name"));
					team.setId(rs.getInt("id"));
				}
			}
		}
		return team;
	}

	public List<Team> getUserTeams(User user) throws SQLException {
		List<Team> teams = new ArrayList<>();
		try (Connection connection = getConnection();
			 PreparedStatement ps = connection.prepareStatement(
					 "SELECT t.id, t.name FROM teams t " +
							 "JOIN users_teams ut ON t.id = ut.team_id " +
							 "WHERE ut.user_id = ?")) {
			ps.setInt(1, user.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Team team = new Team(rs.getString("name"));
					team.setId(rs.getInt("id"));
					teams.add(team);
				}
			}
		}
		return teams;
	}

}
