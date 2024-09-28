package com.se1858.group4.Land_Auction_SWP391;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
@SpringBootApplication
public class LandAuctionSwp391Application {

	public static void main(String[] args) {
		SpringApplication.run(LandAuctionSwp391Application.class, args);
	}

//	public static void main(String[] args) {
//		String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=project_land_auction_swp391";
//		String username = "sa";
//		String password = "123";
//
//		try {
//			// Establishing the connection
//			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
//			System.out.println("Connection successful!");
//
//			// Creating a statement
//			Statement statement = connection.createStatement();
//
//			// Executing a test query to get usernames and roles
//			String query = "SELECT a.username, r.role_name " +
//					"FROM Account a " +
//					"JOIN Role r ON a.role_id = r.role_id;";
//			ResultSet resultSet = statement.executeQuery(query);
//
//			// Processing the results
//			while (resultSet.next()) {
//				String user = resultSet.getString("username");
//				String role = resultSet.getString("role_name");
//				System.out.println("Username: " + user + ", Role: " + role);
//			}
//
//			// Closing resources
//			resultSet.close();
//			statement.close();
//			connection.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
