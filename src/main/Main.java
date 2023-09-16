package main;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static PostgresDatabase database;
	public static Bank bank;
	public static ATM atm;
	public final static String SEPARATOR = "---------------------------------------------------";

	public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
		database = new PostgresDatabase("atm_db", "postgres", "postgres");
		database.connect();
		//database.createUsersTable();

		try {
			bank = new Bank();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		Scanner input = new Scanner(System.in);
		System.out.println(SEPARATOR);

		while (true) {
			System.out.println(
					"""
							Choose what you want to do (press the number and ENTER):
							1. CREATE A NEW ACCOUNT
							2. ACCESS THE ATM
							3. EXIT""");

			int option = input.nextInt();
			switch (option) {
				case 1 -> {
					System.out.println(SEPARATOR);
					bank.createAccount();
				}
				case 2 -> {
					System.out.println(SEPARATOR);
					atm = new ATM();
				}
				case 3 -> {
					System.out.println(SEPARATOR);
					System.exit(0);
				}
			}
		}
	}
}
