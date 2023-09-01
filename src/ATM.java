import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ATM {
    public ATM() throws NoSuchAlgorithmException {
        System.out.println("ATM is turning on...");
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
                    System.exit(0);
                }
            }
        }
    }

    private User login() throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        User currentUser;
        int attempts = 3;

        do {
            System.out.print("Enter your UID: ");
            String uuid = scanner.nextLine();

            System.out.print("Enter PIN-code, then press ENTER: ");
            currentUser = Main.bank.userLogin(uuid, scanner.nextLine(), --attempts);
        } while (currentUser == null && attempts > 0);

        return currentUser;
    }
}
