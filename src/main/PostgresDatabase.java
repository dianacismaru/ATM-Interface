package main;

import java.sql.*;

public class PostgresDatabase {
	public static Connection connection;
	private final String dbName;
	private final String username;
	private final String password;

	public PostgresDatabase(String dbName, String username, String password) {
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}

	public void connect() {
		String jdbcUrl = "jdbc:postgresql://localhost:5432/" + dbName;

		try {
			// Load the PostgreSQL JDBC driver
			Class.forName("org.postgresql.Driver");

			// Establish the database connection
			connection = DriverManager.getConnection(jdbcUrl, username, password);

			if (connection == null) {
				System.out.println("Database connection failed");
				throw new SQLException();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public void createUsersTable() {
		try {
			String query = "CREATE TABLE users (" +
					"uid VARCHAR(10) PRIMARY KEY," +
					"first_name VARCHAR(50)," +
					"lastName VARCHAR(50)," +
					"pinHash VARCHAR(16)" +
					")";

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			System.out.println("Table Created");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void addRecord(String uid, String firstName,
						  String lastName, String pinHash) {
		try {
			String query = String.format("INSERT INTO %s " +
							"(uid, first_name, last_name, pin_hash)" +
							"VALUES ('%s', '%s', '%s', '%s');",
					"users", uid, firstName, lastName, pinHash);

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			
			System.out.printf("users.User '%s %s' with ID '%s' has been created.\n",
					lastName, firstName, uid);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public ResultSet getQueryResult(String query) {
		try {
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}