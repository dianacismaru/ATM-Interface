package main;

import transactions.Transaction;
import users.User;

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

	/**
	 * Connect to the PostgreSQL database
	 */
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

	/**
	 * Method that creates the users table
	 * It is meant to be used one time only
	 */
	public void createUsersTable() {
		try {
			String query = "CREATE TABLE users (" +
					"uid VARCHAR(10) PRIMARY KEY," +
					"first_name VARCHAR(50)," +
					"lastName VARCHAR(50)," +
					"pinHash VARCHAR(64)," +
					"balance DOUBLE PRECISION" +
					")";

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			System.out.println("Table Created");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method that creates the transactions table
	 * It is meant to be used one time only
	 */
	public void createTransactionsTable() {
		try {
			String query = "CREATE TABLE transactions (" +
					"id serial PRIMARY KEY," +
					"user_id VARCHAR(10)," +
					"transaction_type VARCHAR(10)," +
					"amount DOUBLE PRECISION," +
					"recipient_id VARCHAR(16)," +
					"date TIMESTAMP" +
					")";

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			System.out.println("Table Created");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Add a user into the users table
	 * @param user the User object that will be added
	 */
	public void addUser(User user) {
		try {
			String query = String.format("INSERT INTO %s " +
							"(uid, first_name, last_name, pin_hash)" +
							"VALUES ('%s', '%s', '%s', '%s');",
							"users", user.getUid(), user.getFirstName(),
							user.getLastName(), user.getPinHash());

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			
			System.out.printf("User '%s %s' with ID '%s' has been created.\n",
					user.getLastName(), user.getFirstName(), user.getUid());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Add a transaction into the transactions table
	 * @param transaction the Transaction object that will be added
	 */
	public void addTransaction(Transaction transaction) {
		try {
			String query = "INSERT INTO transactions " +
							"(user_id, transaction_type, amount, recipient_id, date)" +
							"VALUES (?, ?, ?, ?, ?);";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, transaction.getUserId());
			preparedStatement.setString(2, transaction.getTransactionType());
			preparedStatement.setDouble(3, transaction.getAmount());
			preparedStatement.setString(4, transaction.getRecipientId());
			preparedStatement.setTimestamp(5, new java.sql.Timestamp(transaction.getDate().getTime()));

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @param query a String SQL formatted query
	 * @return the ResultSet of the query, or null if the query throws an exception
	 */
	public ResultSet getQueryResult(String query) {
		try {
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
