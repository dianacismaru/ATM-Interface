/******************************************
 * Copyright (C) 2022 Cismaru Diana-Iuliana
 ******************************************/
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    /**
     * The list of users that are registered in the bank
     */
    private ArrayList<User> users;

    public Bank() throws NoSuchAlgorithmException {
        this.users = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------------------------------------");
        System.out.println("---------------bank-initialization---------------\n");

        System.out.print("Input the number of users you want to add: ");
        int numberOfUsers = scanner.nextInt();
        if (numberOfUsers < 1) {
            System.out.println("Error: There are no users in the system.");
            System.out.println("-------------------------------------------------");
            System.exit(1);
        }
        for (int i = 0; i < numberOfUsers || numberOfUsers > users.size(); i++) {
            System.out.println("\nIntroduce data for a new user.");
            requestUserData();
        }
        System.out.println("-------------------------------------------------");
    }

    public String generateUserID() {
        String uid = "";
        Random random = new Random();
        final int len = 6;
        boolean nonUnique = false;

        do {
            for (int c = 0; c < len; c++) {
                uid += ((Integer)random.nextInt(10));
            }
            // Make sure the ID is unique
            for (User u: this.users) {
                if (uid.compareTo(u.getUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while(nonUnique);

        return uid;
    }

    public void requestUserData() throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type the First Name: ");
        String firstName = scanner.next();

        while (!firstName.matches("^[A-Za-z]+")) {
            System.out.println("The first name may be typed wrong. Try again!");
            firstName = scanner.next();
        }

        System.out.print("Type the Last Name: ");
        String lastName = scanner.next();

        while (!firstName.matches("^[A-Za-z]+")) {
            System.out.println("The last name may be typed wrong. Try again!");
            firstName = scanner.next();
        }

        System.out.print("Type the PIN code: ");
        createUser(firstName, lastName, scanner.next());
    }

    public void createUser (String firstName, String lastName, String pin) throws NoSuchAlgorithmException {
        User newUser = new User(firstName, lastName, pin, this);
        if (newUser.firstName != null) {
            this.users.add(newUser);
        }
    }

    public User userLogin(String uid, String pin, int attempts) throws NoSuchAlgorithmException {
        for (User user: this.users) {
            // Check if the user exists in the system
            if (user.getUID().compareTo(uid) == 0 && user.validatePin(pin)) {
                return user;
            }
        }
        if (attempts != 0){
            System.out.println("The introduced data is wrong. Please, try again!\n"
                               + attempts + " tries left!");
        } else {
            System.out.println("You have no more attemps left. Goodbye!");
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
