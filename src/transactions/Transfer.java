package transactions;

import main.Main;
import users.User;

import java.util.Scanner;

public class Transfer extends Transaction {
	public Transfer(User user) {
		super(user);
		this.transactionType = "transfer";
	}

	@Override
	public void performTransaction() {
		System.out.print("Enter the ID of the user you would like to send money to: ");

		Scanner scanner = new Scanner(System.in);
		recipientId = scanner.next();
		User recipient = Main.bank.findUser(recipientId);

		if (recipient == null)
			return;

		if (recipient.equals(user)) {
			System.out.println("You can't transfer money to yourself.\n");
			return;
		}

		System.out.print("Enter the amount of money you would like to transfer to "
				+ recipient.getFirstName() + " " + recipient.getLastName() + ": ");
		this.amount = scanner.nextDouble();

		if (this.amount <= 0) {
			System.out.println("The amount must be a positive number!\n");
			return;
		}

		if (this.amount > user.getBalance()) {
			System.out.println("You can't transfer more money than you have.\n");
			return;
		}

		System.out.println("Are you sure you want to transfer " + amount + "EUR to "
				+ recipient.getFirstName() + " " + recipient.getLastName() + "?");
		System.out.print("Type YES or NO: ");
		String answer = scanner.next();

		if (answer.equals("YES")) {
			user.updateBalance(-amount);
			recipient.updateBalance(amount);
			System.out.println("The transaction is complete!\n");
		} else {
			System.out.println("The transaction has been canceled!\n");
		}
	}
}
