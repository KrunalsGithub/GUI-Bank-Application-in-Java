package coe528.project;

/* Krunal Patel 501175325
 * Overview: This class represents a bank account with a dynamic state that is determined by the account's balance.
 *           The account's state can be Silver, Gold, or Platinum, each affecting transaction behaviour differently.
 *           Also, this class is mutable as the account's balance and state can change in response to transactions 
 *           such as deposits, withdrawals, and online purchases.
 *
 * Abstraction Function: a bank account where "accountBalance" represents the balance of the account, and accountState, represents the membership lelvel,
 * provided that the rep invariant holds.
 * 
 * Rep Invariant: The account balance must be non-negative and the account state must not be null.
 */

public abstract class ModernBankAccount {
    private double accountBalance;
    private AccountState accountState; 

    /*
    * Initializes a new bank account with a specified initial state and zero balance
    * Effects: Creates a bank account with the specified initial state and a balance of zero
    */
    public ModernBankAccount(AccountState initialState) {
        this.accountBalance = 0; // Initialized to 0.0 implicitly
        this.accountState = initialState;
    }
 
    /*
    * Checks the account balance
    * Requires: account is not null
    * Effects: Returns the current balance of the account
    */
    public double checkBalance() { 
        return accountBalance;
    }

    /*
    * Updates the account balance to a new value
    * Requires: account is not null, newBalance >= 0
    * Modifies: account's balance
    * Effects: Sets the account balance to the new value
    */
    public void updateBalance(double newBalance) { 
        if (newBalance >= 0) {
            this.accountBalance = newBalance;
        }
    }
    
    /*
    * Deposits an amount to the account
    * Requires: account is not null, amount > 0
    * Modifies: account's balance
    * Effects: Increases the account balance by the deposit amount
    */
    public void deposit(double amount) { 
        if (amount > 0) {
            this.accountBalance += amount;
            NotificationWindow.show("Deposit", "$" + amount + " has been deposited into your account.");
        } else {
            NotificationWindow.show("Error","Enter a valid amount");
        }
    }

    /*
    * Withdraws a specified amount from the account
    * Requires: account is not null, amount > 0 and that the amount is less than or equal to the account balance
    * Modifies: account's balance
    * Effects: Decreases the account balance by the withdrawal amount
    */
    public void withdraw(double amount) { 
        if (amount > 0 && this.accountBalance >= amount) {
            this.accountBalance -= amount;
            NotificationWindow.show("Withdrawal", "$" + amount + " has been withdrawn from your account.");
        } else if (amount <= 0){
            NotificationWindow.show("Error","Enter a valid amount");
        } else if (amount > this.accountBalance) {
            NotificationWindow.show("Error","Insufficient Funds for Withdrawal");
        }
    }

    /*
    * Executes an online purchase, applying a service charge based on account level in other classes
    * Requires: account is not null, purchaseAmount >= 50
    * Modifies: account's balance
    * Effects: Withdraws purchase amount plus service charge from account if balance is sufficient
    */
    public void executeOnlinePurchase(double purchaseAmount) { 
        if (purchaseAmount < 50) {
            NotificationWindow.show("Purchase Error", "Online purchase must be $50 or more.");
            return;
        } else if (purchaseAmount > accountState.checkBalance(this)){
            NotificationWindow.show("Purchase Error", "Insufficient Funds");
        } else {
            this.accountState.executeOnlinePurchase(this, purchaseAmount);
        }
    }

    /*
    * Changes the account's state to a new state
    * Requires: newState is not null
    * Modifies: account's state
    * Effects: Sets the account's state to the new state
    */
    public void setAccountState(AccountState newState) {
        this.accountState = newState;
    }

    /*
    * Retrieves a description of the account's current state type
    * Effects: Returns a string describing the current state of the account (silver, gold, platinum)
    */
    public String describeAccountType() {
        return this.accountState.describeAccountType();
    }
    
    /*
    * Retrieves the current state of the account
    * Effects: Returns the current state of the account
    */
    public AccountState getAccountState() {
        return this.accountState;
    }

    /* 
    * Validates the account's balance
    * Effects: Returns true if the account's balance is non-negative, false otherwise
    */
    public boolean validateBalance() {
        return this.accountBalance >= 0;
    }
    
    //toString method implementing the abstraction function
    @Override
    public String toString() {
        return String.format("BankAccount[balance=%.2f, state=%s]", accountBalance, accountState.describeAccountType());
    }
    
    //Checks if the representation invariant holds for this account
    private boolean repOk() {
        return accountBalance >= 0 && accountState != null;
    }
}
