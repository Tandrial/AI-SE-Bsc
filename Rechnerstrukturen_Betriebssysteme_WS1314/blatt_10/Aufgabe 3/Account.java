import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private int number;
	private int balance;
	private ReentrantLock lock = new ReentrantLock();
	
	public Account(int number, int balance) {
		this.number = number;
		this.balance = balance;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void doLock() {
		lock.lock();
	}
	
	public void doUnlock() {
		lock.unlock();
	}
}

