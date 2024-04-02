package coe528.project;
//Krunal Patel 501175325
//Represents a Gold level state of a bank account. Handles transactions with a specific service charge.
public class Gold implements AccountState{
    private static final double SERVICE_CHARGE = 10; //Fixed service charge for gold accounts

    @Override
    public void executeOnlinePurchase(ModernBankAccount account, double purchaseAmount){
        double totalCost = purchaseAmount + SERVICE_CHARGE;
        if (account.checkBalance() >= totalCost){
            account.updateBalance(account.checkBalance() - totalCost); //Perform the withdrawal logic here instead of calling withdraw
            NotificationWindow.show("Purchase Successful", "Gold Member Purchase: $" + purchaseAmount + " with $" + SERVICE_CHARGE + " fee. Total: $" + totalCost);
        } else {
            NotificationWindow.show("Purchase Error", "Insufficient funds for this purchase.");
        }
    }

    @Override
    public String describeAccountType(){
        return "Gold Member Account";
    }

    @Override
    public double checkBalance(ModernBankAccount account){
        return account.checkBalance();
    }

    @Override
    public void deposit(ModernBankAccount account, double amount){
    }

    @Override
    public void withdraw(ModernBankAccount account, double amount){
    }

    @Override
    public void updateBalance(ModernBankAccount account, double newBalance){
    }

}