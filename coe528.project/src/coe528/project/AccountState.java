package coe528.project;
/*Krunal Patel 501175325
* An interface defining operations for account states in a banking system, ensuring consistent behavior
* across different states (e.g., Silver, Gold, Platinum) for transactions and other functions. 
*/
public interface AccountState {

    void executeOnlinePurchase(ModernBankAccount account, double purchaseAmount);

    String describeAccountType();

    double checkBalance(ModernBankAccount account);

    void deposit(ModernBankAccount account, double amount);

    void withdraw(ModernBankAccount account, double amount);
    
    void updateBalance(ModernBankAccount account, double newBalance);
}

