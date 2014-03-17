import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class BankSimulation {
	public static void main(String[] args) {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		int count = 4;
        
        // New bank
        Bank bank = new Bank();
        
        // Some "jobs" (make money) and "family members" (spend money)
        BankCustomer[] jobs1 = new BankCustomer[count]; //count jobs to transfer money to home account
        BankCustomer[] jobs2 = new BankCustomer[count]; //count jobs to transfer money to home account
        BankCustomer[] familyMembers1 = new BankCustomer[count];//count family members to spend money
        BankCustomer[] familyMembers2 = new BankCustomer[count];//count family members to spend money
        
        // Accounts with initial balance:
        int homeAccount1 = bank.CreateAccount(100);//create account
        int homeAccount2 = bank.CreateAccount(100);//create account
        
        // Create people earning and spending money:
        for (int i = 0; i < count; i++) //create count of each
        {
            int shopAccount1 = bank.CreateAccount(100);
            jobs1[i] = new BankCustomer(bank, shopAccount1, homeAccount1, 50); //count jobs
            familyMembers1[i] = new BankCustomer(bank, homeAccount1, shopAccount1, 50); //count family members
            int shopAccount2 = bank.CreateAccount(100);
            jobs2[i] = new BankCustomer(bank, shopAccount2, homeAccount2, 50); //count jobs
            familyMembers2[i] = new BankCustomer(bank, homeAccount2, shopAccount2, 50); //count family members
        }
        
        System.out.println("Total money in bank initially: " + bank.GetTotalMoney());
        
        try {
			console.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) //start all threads
        {
            jobs1[i].start();
            familyMembers1[i].start();
            jobs2[i].start();
            familyMembers2[i].start();
        }
        
        try {
			console.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		time = System.currentTimeMillis() - time;
        System.out.println("Total time: " + time + " ms");
        bank.PrintAccountBalances();
        
        try {
			console.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
