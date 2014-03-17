
public class BankCustomer extends Thread {
    int sourceAccount;//the account this customer takes the money from  
    int destinationAccount;//the account this customer sends the money to
    int amount;//the amount to transfer each time
    Bank bank;
    
    public BankCustomer(Bank bank, int sourceAccount, int destinationAccount, int amount)
    {
        this.bank = bank;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }
    
    public void run()
    {
        for (int i = 0; i < 50; i++) //do 50 transfers
        {
            try {
                bank.TransferMoney(sourceAccount, destinationAccount, amount);
                System.out.println(Thread.currentThread().getId() + ": Transfer of " + amount + " money from " + sourceAccount + " to " + destinationAccount);
                Thread.sleep((int)(Math.random() * 10));
            }
            catch (Exception ex) {
            	System.out.println(ex.getMessage());
            } //print out exception if anything goes wrong
        }
        System.out.println("Thread " + Thread.currentThread().getId() + " finished");
    }
}
