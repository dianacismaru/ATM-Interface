package main;

import users.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    /**
     * The list of users that are registered in the bank
     */
    private final ArrayList<User> users = new ArrayList<>();

    public Bank() throws SQLException {
        String query = "SELECT * FROM users;";
        ResultSet resultSet = Main.database.getQueryResult(query);

        // Create a list of all the users from the bank's database
        while (resultSet.next()) {
            String uid = resultSet.getString("uid");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String pinHash = resultSet.getString("pin_hash");

            User user = new User(this, uid, firstName, lastName, pinHash);
            users.add(user);
        }
    }

    public static String generateUID() throws SQLException {
        Random random = new Random();
        boolean isUidUnique = true;
        StringBuilder uid;

        do {
            // Create a random ID
            uid = new StringBuilder();
            int uidLen = 6;
            for (int c = 0; c < uidLen; c++) {
                uid.append((Integer) random.nextInt(10));
            }

            // Make sure the ID is unique
            String query = String.format("SELECT * FROM users WHERE uid = '%s'", uid);
            ResultSet resultSet = Main.database.getQueryResult(query);
            if (resultSet.next()) {
                isUidUnique = false;
            }
        } while(!isUidUnique);

        return uid.toString();
    }

    public void createAccount() throws NoSuchAlgorithmException, SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input the number of users you want to create: ");
        int numberOfUsers = scanner.nextInt();
        
        if (numberOfUsers < 1) {
            System.out.println(Main.SEPARATOR);
            return;
        }
        
        for (int i = 0; i < numberOfUsers || numberOfUsers > users.size(); i++) {
            System.out.println("\nIntroduce data for a new user.");
            requestUserData();
        }
        System.out.println(Main.SEPARATOR);
    }
    
    public void requestUserData() throws NoSuchAlgorithmException, SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("First Name: ");
        String firstName = scanner.next();

        while (!firstName.matches("^[A-Za-z]+")) {
            System.out.println("The first name may be typed wrong. Try again!");
            firstName = scanner.next();
        }

        System.out.print("Last Name: ");
        String lastName = scanner.next();

        while (!firstName.matches("^[A-Za-z]+")) {
            System.out.println("The last name may be typed wrong. Try again!");
            firstName = scanner.next();
        }

        System.out.print("PIN code: ");
        String pin = generatePinHash(scanner.next());

        if (pin != null) {
            User user = new User(firstName, lastName, pin, this);
            users.add(user);
        }
    }

    public String generatePinHash(String pin) {
        // Check if the introduced PIN has exactly 4 non-repetitive digits
        if (pin.length() != 4) {
            System.out.println("The PIN code should have exactly 4 digits.\n");
            return null;
        }

        if (!pin.matches("^(?!(.)\\1{3})\\d{4}$")) {
            System.out.println("The introduced PIN may not be safe enough!\n");
            return null;
        }
        return encryptPIN(pin);
    }

    public String encryptPIN(String pin) {
        String encryptedPin = null;

        try {
            // For security reasons, encrypt the PIN with SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] pinHashBytes = md.digest(pin.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder(2 * pinHashBytes.length);
            for (byte b: pinHashBytes) {
                hexStringBuilder.append(String.format("%02X", b));
            }

            encryptedPin = hexStringBuilder.toString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return encryptedPin;
    }

    public User userLogin(String uid, String pin, int attempts) {
        for (User user: this.users) {
            // Check if the user exists in the system
            if (user.getUid().equals(uid) && validatePin(pin, user.getPinHash())) {
                return user;
            }
        }

        if (attempts != 0) {
            System.out.println("The introduced data is wrong. Please, try again!\n"
                               + attempts + " tries left!");
        } else {
            System.out.println("You have no more attemps left. Goodbye!");
            System.out.println(Main.SEPARATOR);
            System.exit(1);
        }

        return null;
    }

    public User findUser(String uid) {
        for (User user: users) {
            if (user.getUid().equals(uid)) {
                return user;
            }
        }

        System.out.println("The user with the specified ID could not be found in the system.\n");
        return null;
    }

    /**
     * Check if the introduced PIN code corresponds to the given pin hash
     */
    public boolean validatePin(String pin, String pinHash) {
        return encryptPIN(pin).equals(pinHash);
    }
}
