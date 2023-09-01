import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    String firstName;
    String lastName;

    private final String uid;

    /**
     *     The MD5 hash of the user's PIN
     */
    private String pinHash;

    private byte[] pinHashBytes;

    private double balance;

    List<Transaction> transactions;

    public void printBalance() {
        System.out.println("Your balance is: " + this.balance + "EUR");
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double amount) {
        this.balance += amount;
    }

    public User(String uid, String firstName, String lastName, String pinHash) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pinHash = pinHash;
    }

    public User(String firstName, String lastName, String pin) throws NoSuchAlgorithmException, SQLException {
        this.uid = Bank.generateUID();

        // Check if the introduced PIN has exactly 4 non-repetitive digits
        if (pin.length() != 4) {
            System.out.println("User cannot be created. The PIN code should have exactly 4 digits.\n");
        } else if (!pin.matches("^(?!(.)\\1{3})\\d{4}$")) {
            System.out.println("User cannot be created. The introduced PIN may not be safe enough!\n");
        } else {
            this.firstName = firstName;
            this.lastName = lastName;
            this.transactions = new ArrayList<>();

            // For security reasons, store the PIN's MD5 hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHashBytes = md.digest(pin.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder(2 * pinHashBytes.length);
            for (byte b : pinHashBytes) {
                hexStringBuilder.append(String.format("%02X", b));
            }
            this.pinHash = hexStringBuilder.toString();

            Main.database.addRecord(uid, firstName, lastName, pinHash);

            System.out.printf("User '%s %s' with ID '%s' has been created.\n",
                              lastName, firstName, this.uid);
        }
    }

    public String getUID() {
        return this.uid;
    }

    /**
     * Check if the introduced PIN code corresponds to the user
     */
    public boolean validatePin(String pin) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(pin.getBytes());

        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02X", b));
        }
        return hexStringBuilder.toString().equals(pinHash);
    }

    public void changePin() throws NoSuchAlgorithmException {
        Scanner input = new Scanner(System.in);
        System.out.print("Type your current PIN code: ");
        String oldPin = input.next();

        if (!this.validatePin(oldPin)) {
            System.out.println("The PIN code is incorrect. You may try again.\n");
            return;
        }

        System.out.print("Type your new PIN code: ");
        String newPin = input.next();
        if (newPin.matches("^(?!(.)\\1{3})\\d{4}$")) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHashBytes = md.digest(newPin.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder(2 * pinHashBytes.length);
            for (byte b : pinHashBytes) {
                hexStringBuilder.append(String.format("%02X", b));
            }
            this.pinHash = hexStringBuilder.toString();

            System.out.println("The PIN code has been changed.\n");
            return;
        }
        System.out.println("The PIN code could not be changed.\n");
    }

    public void printTransactionHistory() {
        if (transactions.size() == 0) {
            System.out.println("No transaction has been made yet.\n");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", uid='" + uid + '\'' +
                ", pinHash='" + pinHash + '\'' +
                '}';
    }
}
