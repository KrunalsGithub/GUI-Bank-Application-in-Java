package coe528.project;
//Krunal Patel 501175325
//Manages account operations for silver-level customers, applying service charges for online purchases.
public class Silver implements AccountState {
    private static final double SERVICE_CHARGE = 20; //Fixed service charge for silver accounts

    @Override
    public void executeOnlinePurchase(ModernBankAccount account, double purchaseAmount) {
        double totalCost = purchaseAmount + SERVICE_CHARGE;
        if (account.checkBalance() >= totalCost) {
            account.updateBalance(account.checkBalance() - totalCost); //Perform the withdrawal logic here instead of calling withdraw
            NotificationWindow.show("Purchase Successful", "Silver Member Purchase: $" + purchaseAmount + " with $" + SERVICE_CHARGE + " fee. Total: $" + totalCost);
        } else {
            NotificationWindow.show("Purchase Error", "Insufficient funds for this purchase.");
        }
    }

    @Override
    public String describeAccountType() {
        return "Silver Member Account";
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
