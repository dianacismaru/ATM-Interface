package transactions;

import java.util.Date;
import users.*;

public abstract class Transaction {
    public double amount;

    // The time and date of this transaction
    private final Date date;

    // The user that has made this transaction
    public final User user;

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
        return "transactions.Transaction type: " + transactionType + "\nAmount: " + amount
                + "EUR\n" + date + "\n";
    }
}
