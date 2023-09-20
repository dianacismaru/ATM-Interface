package users;

import main.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class User {
    // The user's credentials
    private final String firstName;
    private final String lastName;
    private final String uid;
    private final Bank bank;

    // The SHA-256 hash of the user's PIN
    private String pinHash;

    // The account's balance
    private double balance;

    public User(Bank bank, String uid, String firstName, String lastName,
                String pinHash, Double balance) {
        this.bank = bank;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pinHash = pinHash;
        this.balance = balance;
    }

    /**
     * Change the current user's PIN code
     */
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

    /**
     * Modify the user's balance by 'amount' euro
     * @param amount the amount of euros that will be added or deducted from the balance
     */
    public void updateBalance(double amount) {
        this.balance += amount;

        try {
            String query = String.format("UPDATE users SET balance  = '%f' " +
                    "WHERE uid = '%s'", balance, uid);

            Statement statement = PostgresDatabase.connection.createStatement();
            statement.executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Print the user's balance
     */
    public void printBalance() {
        System.out.println("Your balance is: " + this.balance + "EUR");
    }

    /**
     * Print the Bank Statement of the current user
     */
    public void getBankStatement() {
        System.out.println(Main.SEPARATOR);
        System.out.println("BANK STATEMENT");
        System.out.println(Main.SEPARATOR);

        System.out.println("User: " + firstName + " " + lastName);
        System.out.println("UID: " + uid);
        System.out.println("Date: " + new Date());
        System.out.println("Balance: " + balance + "\n");

        System.out.println("Transaction history:");
        System.out.println(Main.SEPARATOR);

        String query = String.format("SELECT * FROM transactions WHERE user_id = '%s' OR recipient_id = '%s'", uid, uid);
        ResultSet resultSet = Main.database.getQueryResult(query);

        int ct = 1;
        try {
            while (resultSet.next()) {
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                String userId = resultSet.getString("user_id");
                String recipientId = resultSet.getString("recipient_id");
                Date date = new Date(resultSet.getTimestamp("date").getTime());

                System.out.println(ct++ + ". Date: " + date);
                System.out.println("Type: " + transactionType);

                if (transactionType.equals("transfer")) {
                    if (userId.equals(uid)) {
                        System.out.println("Recipient: " + recipientId);
                    } else {
                        System.out.println("From: " + userId);
                    }
                }

                System.out.println("Amount: " + amount + " EUR\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(Main.SEPARATOR);
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
