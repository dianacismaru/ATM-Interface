package main;

import users.User;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    // A cache of users that are registered in the bank
    private final ArrayList<User> users = new ArrayList<>();

    /**
     * Create the bank and store the users from the database in a cache
    */
    public Bank() {
        String query = "SELECT * FROM users;";
        ResultSet resultSet = Main.database.getQueryResult(query);

        try {
            // Create a list of all the users from the bank's database
            while (resultSet.next()) {
                String uid = resultSet.getString("uid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String pinHash = resultSet.getString("pin_hash");
                Double balance = resultSet.getDouble("balance");

                User user = new User(this, uid, firstName, lastName, pinHash, balance);
                users.add(user);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generate a unique user ID (UID) consisting of a random 6-digit numeric value.
     * The method ensures the generated UID is unique by querying the database to
     * verify its non-existence among existing users
     * @return A unique 6-digit numeric user ID as a String
     */
    public static String generateUID() {
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

            try {
                if (resultSet.next()) {
                    isUidUnique = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while(!isUidUnique);

        return uid.toString();
    }

    /**
     * Create a new user into the system
     */
    public void createAccount() {
        System.out.println("\nIntroduce data for a new user.");
        requestUserData();

        System.out.println(Main.SEPARATOR);
    }
    
    private void requestUserData() {
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
            User user = new User(this, generateUID(), firstName, lastName, pin, 0.0);
            Main.database.addUser(user);
            users.add(user);
        }
    }

    /**
     * Check if the introduced PIN code corresponds to the given pin hash
     */
    public boolean validatePin(String pin, String pinHash) {
        return encryptPIN(pin).equals(pinHash);
    }

    /**
     * Generates a secure hash for a 4-digit PIN code
     * The method verifies that the provided PIN has exactly 4 non-repetitive digits
     * before generating and returning its hash value
     *
     * @param pin a 4-digit PIN code
     * @return a hashed representation of the PIN code if it meets the criteria,
     *         or null if the PIN is invalid or not safe enough
     */
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

    /**
     * Encrypt a PIN code using SHA-256 algorithm
     * @param pin a 4-digit PIN code
     * @return the encrypted PIN code
     */
    private String encryptPIN(String pin) {
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

    /**
     * Logs in a user with the given credentials
     * @param uid the user's unique ID
     * @param pin the user's PIN code
     * @param attempts the remaining attempts for login
     * @return the user that has logged in
     */
    public User userLogin(String uid, String pin, int attempts) {
        for (User user: this.users) {
            // Check if the user exists in the system
            if (user.getUid().equals(uid) && validatePin(pin, user.getPinHash())) {
                return user;
            }
        }

        // If there's no more attempts left
        if (attempts != 0) {
            System.out.println("The introduced data is wrong. Please, try again!\n"
                               + attempts + " tries left!");
        } else {
            System.out.println("You have no more attempts left. Goodbye!");
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
}
