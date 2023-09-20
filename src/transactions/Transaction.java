package transactions;

import java.util.Date;
import users.*;

public abstract class Transaction {
    private double amount;

    // The time and date of this transaction
    private final Date date;

    // The user that has made this transaction
    private final User user;

    // The user that has received this transaction
    private String recipientId;

    private String transactionType;

    public Transaction(User user) {
        this.user = user;
        this.date = new Date();
        setRecipientId(user.getUid());
    }

    /**
     * Factory method used to create a specific transaction based on the
     * provided option
     * @param option an integer representing the type of the transaction to create
     *               1 for Deposit
     *               2 for Withdrawal
     *               3 for Transfer
     * @param user the user that initiates the transaction
     * @return a transaction object based on the specified option
     * @throws IllegalArgumentException if the provided option is not valid
     */
    public static Transaction createTransaction(int option, User user) {
        switch (option) {
            case 1 -> {
                return new Deposit(user);
            }
            case 2 -> {
                return new Withdrawal(user);
            }
            case 3 -> {
                return new Transfer(user);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    /**
     * Abstract method that does the actual transaction, depending
     * on the dynamic type
     */
    public abstract void performTransaction();

    public User getUser() {
        return user;
    }

    public String getUserId() {
        return user.getUid();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
