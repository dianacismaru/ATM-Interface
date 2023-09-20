package main;

import transactions.Transaction;
import users.User;

import java.util.Scanner;

public class ATM {
    // The user that is currently connected to the ATM
    private final User currentUser;

    /**
     * Initialize the ATM by logging with an existing user
     */
    public ATM() {
        System.out.println("ATM is turning on...");
        System.out.println("\nPlease, insert your card");
        currentUser = login();

        turnOn();
    }

    /**
     * This method represents the main functionality of the ATM after it is turned on
     * It displays a menu of options to the user, reads their choice, and performs
     * the corresponding actions such as deposit, withdrawal, transfer, bank
     * statement, balance inquiry, PIN code change, and exit.
     */
    private void turnOn() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(Main.SEPARATOR);
            System.out.println(
                    """
                            Choose what you want to do (press the number and ENTER):
                            1. DEPOSIT
                            2. WITHDRAW
                            3. TRANSFER
                            4. BANK STATEMENT
                            5. BALANCE
                            6. CHANGE PIN CODE
                            7. EXIT""");

            int option = scanner.nextInt();
            switch (option) {
                case 1, 2, 3 -> {
                    Transaction transaction = Transaction.createTransaction(option, currentUser);
                    transaction.performTransaction();
                    Main.database.addTransaction(transaction);
                }
                case 4 -> {
                    currentUser.getBankStatement();
                }
                case 5 -> {
                    currentUser.printBalance();
                    System.out.println();
                }
                case 6 -> {
                    currentUser.changePin();
                }
                case 7 -> {
                    System.out.println("Do not forget to take your card!");
                    System.out.println(Main.SEPARATOR);
                    scanner.close();
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Authenticates and logs in the user into the ATM system
     * This method allows the user to enter their unique identifier (UID) and PIN-code
     * to authenticate themselves. The user has a limited number of login attempts
     * before being locked out. Upon successful authentication, the user is logged in
     *
     * @return The logged-in users.User object if authentication is successful, null otherwise
     */
    private User login() {
        Scanner scanner = new Scanner(System.in);
        User currentUser;
        int remainingAttempts = 3;

        do {
            System.out.print("Enter your User ID (UID): ");
            String uid = scanner.nextLine();

            System.out.print("Enter your PIN-code: ");
            String pin = scanner.nextLine();

            currentUser = Main.bank.userLogin(uid, pin, --remainingAttempts);
        } while (currentUser == null && remainingAttempts > 0);

        return currentUser;
    }
}
