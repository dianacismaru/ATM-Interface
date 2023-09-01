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

        while (resultSet.next()) {
            String uid = resultSet.getString("uid");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String pinHash = resultSet.getString("pin_hash");
            User user = new User(uid, firstName, lastName, pinHash);
            users.add(user);
        }
    }

    public static String generateUID() throws SQLException {
        Random random = new Random();
        boolean isUidUnique = true;
        String uid;

        do {
            // Create a random ID
            uid = "";
            int uidLen = 6;
            for (int c = 0; c < uidLen; c++) {
                uid += ((Integer)random.nextInt(10));
            }

            // Make sure the ID is unique
            String query = String.format("SELECT * FROM users WHERE uid = '%s'", uid);
            ResultSet resultSet = Main.database.getQueryResult(query);
            if (resultSet.next()) {
                isUidUnique = false;
            }
        } while(!isUidUnique);

        return uid;
    }

    public void createAccount() throws NoSuchAlgorithmException, SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input the number of users you want to create: ");
        int numberOfUsers = scanner.nextInt();
        
        if (numberOfUsers < 1) {
            System.out.println("-------------------------------------------------");
            return;
        }
        
        for (int i = 0; i < numberOfUsers || numberOfUsers > users.size(); i++) {
            System.out.println("\nIntroduce data for a new user.");
            requestUserData();
        }
        System.out.println("-------------------------------------------------");
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
        createUser(firstName, lastName, scanner.next());
    }

    private void createUser(String firstName, String lastName, String pin) throws NoSuchAlgorithmException, SQLException {
        User user = new User(firstName, lastName, pin);
        if (user.firstName != null) {
            this.users.add(user);
        }
    }

    public User userLogin(String uid, String pin, int attempts) throws NoSuchAlgorithmException {
        for (User user: this.users) {
            // Check if the user exists in the system
            if (user.getUID().equals(uid) && user.validatePin(pin)) {
                return user;
            }
        }

        if (attempts != 0){
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
        for (User user : users) {
            if (user.getUID().equals(uid)) {
                return user;
            }
        }
        System.out.println("The user with the specified ID could not be found in the system.\n");
        return null;
    }
}
