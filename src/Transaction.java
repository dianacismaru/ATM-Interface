/******************************************
 * Copyright (C) 2022 Cismaru Diana-Iuliana
 ******************************************/
import java.util.Date;
import java.util.Scanner;

public class Transaction {
    private double amount;

    /**
     *     The time and date of this transaction
     */
    final Date date;

    /**
     *     The user that has made this transaction
     */
    private User user;

    String transactionType;

    /**
     * Constructor used for deposits or withdrawals
     */
    public Transaction(double amount, User user) {
        this.amount = amount;
        this.date = new Date();
        this.user = user;
    }

    /**
     * Constructor used for transfers
     */
    public Transaction(User user) {
        this.user = user;
        this.date = new Date();
        this.transactionType = "transfer";
    }

    @Override
    public String toString() {
        return "Transaction type: " + transactionType + "\nAmount: " + amount
                + "EUR\n" + date + "\n";
    }

    public void deposit() {
        if (this.amount <= 0) {
            System.out.println("The amount must be a positive number!\n");
            return;
        }

        this.user.setBalance(this.amount);
        this.transactionType = "deposit";
        user.transactions.add(this);
        System.out.println("Your balance increased with " + amount + "EUR.\n");
    }

    public void withdraw() {
        if (this.amount <= 0) {
            System.out.println("The amount must be a positive number!\n");
            return;
        }

        if (this.amount > user.getBalance()) {
            System.out.println("The withdrawal cannot be done. Your balance is too low.\n");
        } else {
            this.user.setBalance(-this.amount);
            this.transactionType = "withdrawal";
            user.transactions.add(this);
            System.out.println("Your balance went down with " + amount + "EUR.\n");
        }
    }

    public void transfer(User recipient) {
        if (recipient == null)
            return;

        if (recipient.equals(user)) {
            System.out.println("You can't transfer money to yourself.\n");
            return;
        }

        Scanner input = new Scanner(System.in);
        System.out.print("Enter the amount of money you would like to transfer to "
                           + recipient.firstName + " " + recipient.lastName + ": ");
        this.amount = input.nextDouble();

        if (this.amount <= 0) {
            System.out.println("The amount must be a positive number!\n");
            return;
        }

        if (this.amount > user.getBalance()) {
            System.out.println("You can't transfer more money than you have.\n");
            return;
        }

        System.out.println("Are you sure you want to transfer " + amount + "EUR to "
                           + recipient.firstName + " " + recipient.lastName + "?");
        System.out.print("Type YES or NO: ");
        String answer = input.next();

        if (answer.equals("YES")) {
            user.setBalance(-amount);
            recipient.setBalance(amount);
            System.out.println("The transaction is complete!\n");
            user.transactions.add(this);
        } else {
            System.out.println("The transaction has been canceled!\n");
        }
    }
}
