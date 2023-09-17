package users;

import main.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {
    private final String firstName;
    private final String lastName;
    private final String uid;
    private final Bank bank;

    /**
     *     The SHA-256 hash of the user's PIN
     */
    private String pinHash;

    private double balance;

    public void printBalance() {
        System.out.println("Your balance is: " + this.balance + "EUR");
    }

    public User(Bank bank, String uid, String firstName, String lastName, String pinHash, Double balance) {
        this.bank = bank;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pinHash = pinHash;
        this.balance = balance;
    }

    public User(String firstName, String lastName, String pin, Bank bank) throws SQLException {
        this.uid = Bank.generateUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.bank = bank;
        this.pinHash = pin;

        Main.database.addUser(this);
    }

    public void changePin() {
        Scanner input = new Scanner(System.in);
        System.out.print("Type your current PIN code: ");
        String oldPin = input.next();

        if (!bank.validatePin(oldPin, pinHash)) {
            System.out.println("The PIN code is incorrect. You may try again.\n");
            return;
        }

        System.out.print("Type your new PIN code: ");
        String newPin = bank.generatePinHash(input.next());

        try {
            String query = String.format("UPDATE users SET pin_hash  = '%s' " +
                    "WHERE uid = '%s'", newPin, uid);

            Statement statement = PostgresDatabase.connection.createStatement();
            statement.executeUpdate(query);
            this.pinHash = newPin;

            System.out.println("The PIN code has been changed.\n");
        } catch (Exception e) {
            System.out.println("The PIN code could not be changed.\n");
        }
    }

    public void updateBalance(double amount) {
        this.balance += amount;

        try {
            String query = String.format("UPDATE users SET balance  = '%f' " +
                    "WHERE uid = '%s'", balance, uid);

            Statement statement = PostgresDatabase.connection.createStatement();
            statement.executeUpdate(query);

            System.out.println("The balance has been updated.\n");
        } catch (Exception e) {
            System.out.println("The balance has NOT been updated.\n");
        }
    }

    public void printTransactionHistory() {
/*        if (transactions.isEmpty()) {
            System.out.println("No transaction has been made yet.\n");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }*/
    }

    public String getPinHash() {
        return pinHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUid() {
        return uid;
    }

    public double getBalance() {
        return balance;
    }

}
