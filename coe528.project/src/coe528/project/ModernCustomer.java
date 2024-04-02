package coe528.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Krunal Patel 501175325
//Manages a customer's bank account and operations like deposits, withdrawals, and online purchases. Mutable.
public class ModernCustomer {
    private String username;
    private String password;
    private ModernBankAccount account;
    private String customerRole = "customer";

    public ModernCustomer(String username, String password, double initialBalance, String customerRole){
        this.username = username;
        this.password = password;
        this.account = new ModernBankAccount(determineAccountLevel(initialBalance)){};
        this.account.updateBalance(initialBalance);
        createOrUpdateCustomerFile(true);
    }
    
    //Determines the correct AccountState based on the balance
    private AccountState determineAccountLevel(double balance){
        if (balance < 10000){
            return new Silver();
        } else if (balance >= 10000 && balance < 20000){
            return new Gold();
        } else{
            return new Platinum();
        }
    }

    //Methods below to interact with the account through its state
    public void depositMoney(double amount) {
        this.account.deposit(amount);
        updateAccountLevel();
        createOrUpdateCustomerFile(false);
    }

    public void withdrawMoney(double amount){
        this.account.withdraw(amount);
        updateAccountLevel();
        createOrUpdateCustomerFile(false);
    }
    
    //Attempts an online purchase
    public void attemptOnlinePurchase(double amount){
        this.account.executeOnlinePurchase(amount);
        updateAccountLevel();
        createOrUpdateCustomerFile(false);
    }

    //Updates the account's state based on the current balance
    private void updateAccountLevel(){
        AccountState newState = determineAccountLevel(this.account.checkBalance());
        if (!newState.getClass().equals(this.account.getAccountState().getClass())){
            this.account.setAccountState(newState); 
        }
    }

    private void createOrUpdateCustomerFile(boolean isNewCustomer){
        try{
            File file = new File(username + ".txt");
            if (isNewCustomer) file.createNewFile();
            try (FileWriter writer = new FileWriter(file, false)){
                writer.write(username + "\n" + password + "\n" + customerRole + "\n" + account.checkBalance() + "\n" + account.describeAccountType());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating/updating the customer file: " + e.getMessage());
        } 
    }

    //Getters for username, password, and balance
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public double getAccountBalance(){return this.account.checkBalance();}
    public ModernBankAccount getAccount(){
        return account;
    }
}
