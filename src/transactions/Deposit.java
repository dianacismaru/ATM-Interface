package transactions;

import users.User;

import java.util.Scanner;

public class Deposit extends Transaction {
	public Deposit(User user) {
		super(user);
		setTransactionType("deposit");
	}

	@Override
	public void performTransaction() {
		System.out.print("Enter the amount of money you would like to deposit: ");

		Scanner scanner = new Scanner(System.in);
		setAmount(scanner.nextDouble());
		if (getAmount() <= 0) {
			System.out.println("The amount must be a positive number!\n");
			return;
		}

		getUser().updateBalance(getAmount());
		System.out.println("Your balance increased with " + getAmount() + "EUR.\n");
	}
}
