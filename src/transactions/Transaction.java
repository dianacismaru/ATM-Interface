package transactions;

import java.util.Date;
import java.util.Scanner;
import users.*;

public class Transaction {
    private double amount;

    // The time and date of this transaction
    private final Date date;

    // The user that has made this transaction
    private final User user;

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

    public void deposit() {
        if (this.amount <= 0) {
            System.out.println("The amount must be a positive number!\n");
            return;
        }

        this.user.modifyBalance(this.amount);
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
            this.user.modifyBalance(-this.amount);
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
                           + recipient.getFirstName() + " " + recipient.getLastName() + ": ");
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
                           + recipient.getFirstName() + " " + recipient.getLastName() + "?");
        System.out.print("Type YES or NO: ");
        String answer = input.next();

        if (answer.equals("YES")) {
            user.modifyBalance(-amount);
            recipient.modifyBalance(amount);
            System.out.println("The transaction is complete!\n");
            user.transactions.add(this);
        } else {
            System.out.println("The transaction has been canceled!\n");
        }
    }

    @Override
    public String toString() {
        return "transactions.Transaction type: " + transactionType + "\nAmount: " + amount
                + "EUR\n" + date + "\n";
    }
}
