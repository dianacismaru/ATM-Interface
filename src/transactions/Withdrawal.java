package transactions;

import users.User;

import java.util.Scanner;

public class Withdrawal extends Transaction {
	public Withdrawal(User user) {
		super(user);
		this.transactionType = "withdrawal";
		this.recipientId = user.getUid();
	}

	@Override
	public void performTransaction() {
		System.out.print("Enter the amount of money you would like to withdraw: ");

		Scanner scanner = new Scanner(System.in);
		this.amount = scanner.nextDouble();

		if (amount <= 0) {
			System.out.println("The amount must be a positive number!\n");
			return;
		}

		if (amount > user.getBalance()) {
			System.out.println("The withdrawal cannot be done. Your balance is too low.\n");
		} else {
			this.user.updateBalance(-amount);
			this.transactionType = "withdrawal";
			System.out.println("Your balance went down with " + amount + "EUR.\n");
		}
	}
}
