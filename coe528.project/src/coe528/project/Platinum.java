package coe528.project;
//Krunal Patel 501175325
//Manages account operations for platinum-level customers, including transactions without service charges.
public class Platinum implements AccountState {

    @Override
    public void executeOnlinePurchase(ModernBankAccount account, double purchaseAmount) {
        if (account.checkBalance() >= purchaseAmount) {
            account.updateBalance(account.checkBalance() - purchaseAmount);
            NotificationWindow.show("Purchase Successful", "Purchase of $" + purchaseAmount + " has been processed with no additional fee.");
        } else {
            NotificationWindow.show("Purchase Error", "Insufficient funds for this purchase.");
        }
    }
 
    @Override 
    public String describeAccountType() {
        return "Platinum Member Account";
    }

    @Override
    public double checkBalance(ModernBankAccount account) {
        return account.checkBalance();
    }

    @Override
    public void deposit(ModernBankAccount account, double amount) {
    }

    @Override
    public void withdraw(ModernBankAccount account, double amount) {
    }

    @Override
    public void updateBalance(ModernBankAccount account, double newBalance) {
    }

}