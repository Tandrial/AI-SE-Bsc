import java.util.HashMap;

public class Bank {
	HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();

	public int CreateAccount(int initialBalance) {
		int number;// choose an nonexistent random account number
		do {
			number = (int) Math.floor(Math.random() * 10000);
		} while (accounts.containsKey(new Integer(number)));
		Account newAccount = new Account(number, initialBalance);
		accounts.put(new Integer(number), newAccount);
		return number; // return account number of created account
	}

	public void TransferMoney(int source, int destination, int amount) {
		Account s = accounts.get(new Integer(source));
		Account d = accounts.get(new Integer(destination));

		// Änderung für Aufgabenteil b)
		// s.doLock();
		// d.doLock();

		// Aufgabenteil c)
		//Code neu START
		if (source < destination) {
			s.doLock();
			d.doLock();
		} else {
			d.doLock();
			s.doLock();
		}
		// Code neu ENDE
		if (s.getBalance() >= amount) {
			try {
				int balance;
				// Clerk needs some time
				Thread.sleep((int) (Math.random() * 50));
				balance = s.getBalance();
				// Money moves slowly
				Thread.sleep((int) (Math.random() * 50));
				s.setBalance(balance - amount);
				balance = d.getBalance();
				Thread.sleep((int) (Math.random() * 50));
				d.setBalance(balance + amount);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // very slowly
		} else {
			System.out.println(Thread.currentThread().getId()
					+ ": Transfer of " + amount + " money from " + source
					+ " to " + destination + " failed, balance only: "
					+ s.getBalance());
		}
		if (s.getBalance() < 0) {
			System.out.println("Something must be wrong, balance of " + source
					+ " is negative");
		}

		s.doUnlock();
		d.doUnlock();
	}

	public int GetTotalMoney() {
		int total = 0;
		System.out.println("Total accounts: " + accounts.size());
		for (Integer key : accounts.keySet()) {
			total += accounts.get(key).getBalance();
		}
		return total;
	}

	public void PrintAccountBalances() {
		for (Integer key : accounts.keySet()) {
			Account a = accounts.get(key);
			System.out.println("Account " + a.getNumber()
					+ " has a balance of " + a.getBalance());
		}
		System.out.println("Total money on bank: " + GetTotalMoney());
	}
}
