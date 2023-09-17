package transactions;

import java.util.Date;
import users.*;

public abstract class Transaction {
    public double amount;

    // The time and date of this transaction
    public final Date date;

    // The user that has made this transaction
    public final User user;

    // The user that has received this transaction
    public String recipientId;

    public String transactionType;

    public Transaction(User user) {
        this.user = user;
        this.date = new Date();
    }

    public static Transaction createTransaction(int option, User user) {
        switch (option) {
            case 1 -> {
                return new Deposit(user);
            }
            case 2 -> {
                return new Withdrawal(user);
            }
            default -> {
                return new Transfer(user);
            }
        }
    }

    public abstract void performTransaction();

    @Override
    public String toString() {
        return "Transaction type: " + transactionType + "\nAmount: " + amount
                + "EUR\n" + date + "\n";
    }

    public String getUserId() {
        return user.getUid();
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getRecipientId() {
        return recipientId;
    }
}
