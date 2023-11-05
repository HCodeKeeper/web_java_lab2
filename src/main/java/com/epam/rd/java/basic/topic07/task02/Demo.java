package com.epam.rd.java.basic.topic07.task02;

import com.epam.rd.java.basic.topic07.task02.db.DBException;
import com.epam.rd.java.basic.topic07.task02.db.DBManager;
import com.epam.rd.java.basic.topic07.task02.db.entity.Team;
import com.epam.rd.java.basic.topic07.task02.db.entity.User;

import java.sql.SQLException;

public class Demo {

	public static void main(String[] args) throws DBException, SQLException {
		// users  ==> [ivanov petrov obama]
		// teams  ==> [teamA teamB teamC ]

		DBManager dbManager = DBManager.getInstance();
		dbManager.deleteAllUsers();
		dbManager.deleteAllTeams();
		// Create some test users
		User user1 = User.createUser("petrov");
		User user2 = User.createUser("ivanov");
		User user3 = User.createUser("obama");

		// Create some test teams
		Team team1 = Team.createTeam("teamA");
		Team team2 = Team.createTeam("teamB");
		Team team3 = Team.createTeam("teamC");

		// Insert users
		dbManager.insertUser(user1);
		dbManager.insertUser(user2);
		dbManager.insertUser(user3);

		// Insert teams
		dbManager.insertTeam(team1);
		dbManager.insertTeam(team2);
		dbManager.insertTeam(team3);
		User userPetrov = dbManager.getUser("petrov");
		User userIvanov = dbManager.getUser("ivanov");
		User userObama = dbManager.getUser("obama");

		Team teamA = dbManager.getTeam("teamA");
		Team teamB = dbManager.getTeam("teamB");
		Team teamC = dbManager.getTeam("teamC");

		// method setTeamsForUser must implement transaction!
		dbManager.setTeamsForUser(userIvanov, teamA);
		dbManager.setTeamsForUser(userPetrov, teamA, teamB);
		dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

		for (User user : dbManager.findAllUsers()) {
			System.out.println((dbManager.getUserTeams(user)));
		}
		// teamA
		// teamA teamB
		// teamA teamB teamC

		dbManager.deleteAllUsers();
		dbManager.deleteAllTeams();
	}

}
