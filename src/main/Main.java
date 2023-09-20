package main;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static PostgresDatabase database;
	public static Bank bank;
	public static ATM atm;

	// String used for separating ATM outputs
	public final static String SEPARATOR = "---------------------------------------------------";

	public static void main(String[] args) {
		// Connect to the database
		database = new PostgresDatabase("atm_db", "postgres", "postgres");
		database.connect();

		// Create tables
		// Uncomment when first running the program, then comment again
		/*
		database.createUsersTable();
		database.createTransactionsTable();
		*/

		// Initialize the Bank
		bank = new Bank();

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
