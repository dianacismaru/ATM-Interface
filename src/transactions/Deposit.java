package transactions;

import users.User;

import java.util.Scanner;

public class Deposit extends Transaction {
	public Deposit(User user) {
		super(user);
		this.transactionType = "deposit";
		this.recipientId = user.getUid();
	}

	@Override
	public void performTransaction() {
		System.out.print("Enter the amount of money you would like to deposit: ");

		Scanner scanner = new Scanner(System.in);
		this.amount = scanner.nextDouble();
		if (amount <= 0) {
			System.out.println("The amount must be a positive number!\n");
			return;
		}

		this.user.updateBalance(this.amount);
		this.transactionType = "deposit";
		System.out.println("Your balance increased with " + amount + "EUR.\n");
	}
}
