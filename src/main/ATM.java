package main;

import transactions.Transaction;
import users.User;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ATM {
    public ATM() throws NoSuchAlgorithmException {
        System.out.println("main.ATM is turning on...");
        System.out.println("\nPlease, insert your card");
        Scanner scanner = new Scanner(System.in);

        User currentUser = login();

        while (true) {
            System.out.println(Main.SEPARATOR);
            System.out.println(
                    """
                            Choose what you want to do (press the number and ENTER):
                            1. DEPOSIT
                            2. WITHDRAW
                            3. TRANSFER
                            4. TRANSACTIONS HISTORY
                            5. BALANCE
                            6. CHANGE PIN CODE
                            7. EXIT""");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> {
                    System.out.print("Enter the amount of money you would like to deposit: ");
                    Transaction newDeposit = new Transaction(scanner.nextDouble(), currentUser);
                    newDeposit.deposit();
                }
                case 2 -> {
                    System.out.print("Enter the amount of money you would like to withdraw: ");
                    Transaction newWithdrawal = new Transaction(scanner.nextDouble(), currentUser);
                    newWithdrawal.withdraw();
                }
                case 3 -> {
                    System.out.print("Enter the ID of the user you would like to send money to: ");
                    Transaction newTransfer = new Transaction(currentUser);
                    newTransfer.transfer(Main.bank.findUser(scanner.next()));
                }
                case 4 -> {
                    currentUser.printTransactionHistory();
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
     * Authenticates and logs in the user into the main.ATM system
     * This method allows the user to enter their unique identifier (UID) and PIN-code
     * to authenticate themselves. The user has a limited number of login attempts
     * before being locked out. Upon successful authentication, the user is logged in
     *
     * @return The logged-in users.User object if authentication is successful, null otherwise
     */
    private User login() throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        User currentUser;
        int remainingAttempts = 3;

        do {
            System.out.print("Enter your users.User ID (UID): ");
            String uid = scanner.nextLine();

            System.out.print("Enter your PIN-code: ");
            String pin = scanner.nextLine();

            currentUser = Main.bank.userLogin(uid, pin, --remainingAttempts);
        } while (currentUser == null && remainingAttempts > 0);

        return currentUser;
    }
}
