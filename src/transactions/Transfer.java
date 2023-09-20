package transactions;

import main.Main;
import users.User;

import java.util.Scanner;

public class Transfer extends Transaction {
	public Transfer(User user) {
		super(user);
		setTransactionType("transfer");
	}

	@Override
	public void performTransaction() {
		System.out.print("Enter the ID of the user you would like to send money to: ");

		Scanner scanner = new Scanner(System.in);
		setRecipientId(scanner.next());
		User recipient = Main.bank.findUser(getRecipientId());

		if (recipient == null) {
			System.out.println("The user with the specified ID could not be found in the system.\n");
			return;
		}

		if (recipient.equals(getUser())) {
			System.out.println("You can't transfer money to yourself.\n");
			return;
		}

		System.out.print("Enter the amount of money you would like to transfer to "
				+ recipient.getFirstName() + " " + recipient.getLastName() + ": ");
		setAmount(scanner.nextDouble());

		if (getAmount() <= 0) {
			System.out.println("The amount must be a positive number!\n");
			return;
		}

		if (getAmount() > getUser().getBalance()) {
			System.out.println("You can't transfer more money than you have.\n");
			return;
		}

		System.out.println("Are you sure you want to transfer " + getAmount() + "EUR to "
				+ recipient.getFirstName() + " " + recipient.getLastName() + "?");
		System.out.print("Type YES or NO: ");
		String answer = scanner.next();

		if (answer.equals("YES")) {
			getUser().updateBalance(-getAmount());
			recipient.updateBalance(getAmount());
			System.out.println("The transaction is complete!\n");
		} else {
			System.out.println("The transaction has been canceled!\n");
		}
	}
}
