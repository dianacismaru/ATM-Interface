package transactions;

import users.User;

import java.util.Scanner;

public class Withdrawal extends Transaction {
	public Withdrawal(User user) {
		super(user);
		setTransactionType("withdrawal");
	}

	@Override
	public void performTransaction() {
		System.out.print("Enter the amount of money you would like to withdraw: ");

		Scanner scanner = new Scanner(System.in);
		setAmount(scanner.nextDouble());

		if (getAmount() <= 0) {
			System.out.println("The amount must be a positive number!\n");
			return;
		}

		if (getAmount() > getUser().getBalance()) {
			System.out.println("The withdrawal cannot be done. Your balance is too low.\n");
		} else {
			getUser().updateBalance(-getAmount());
			System.out.println("Your balance went down with " + getAmount() + "EUR.\n");
		}
	}
}
