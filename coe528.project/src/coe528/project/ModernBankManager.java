package coe528.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//Krunal Patel 501175325
//Manages bank manager operations such as initializing manager accounts, adding customers, and deleting customers.
public class ModernBankManager {

    public String managerUsername = "admin";
    public String managerPassword = "admin";
    public String managerRole = "manager";

    //Adds a customer if the username does not already exist
    public void initializeManagerAccount() {
        try {
            File managerFile = new File(managerUsername + ".txt");
            if (!managerFile.exists()) {
                try (FileWriter writer = new FileWriter(managerFile)) {
                    writer.write(managerUsername + "\n" + managerPassword + "\n" + managerRole);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing manager account: " + e.getMessage());
        } 
    }
    
    public ModernCustomer addCustomer(String username, String password, double initialBalance){
        //Check for "admin" username or password first
        if (username.equals("admin")) {
            NotificationWindow.show("Error", "Username cannot be 'admin'"); 
            return null;
        } else if (password.equals("admin")) {
            NotificationWindow.show("Error", "Password cannot be 'admin'"); 
            return null;
        }
        //Then, check if the username already exists by looking for an existing file
        File customerFile = new File(username + ".txt"); 
        if (customerFile.exists()) {
            NotificationWindow.show("Error", "Customer with username \"" + username + "\" already exists.");
            return null;
        }
        //Lastly, check the initial balance condition
        if (initialBalance < 100) {
            NotificationWindow.show("Error", "Initial Balance cannot be less than $100"); 
            return null;
        }
        //If all checks pass, create and return a new customer
        return new ModernCustomer(username, password, initialBalance, "customer");
    }
    
    //Deletes a customer
    public ArrayList<ModernCustomer> deleteCustomer(String customerToDelete, ArrayList<ModernCustomer> customersList) {
        ModernCustomer selectedCustomer = customersList.stream()
            .filter(customer -> customer.getUsername().equals(customerToDelete))
            .findFirst()
            .orElse(null);
        if (selectedCustomer != null){
            File customerFile = new File(selectedCustomer.getUsername() + ".txt");
            if (customerFile.delete()) { //Attempt to delete the file
                NotificationWindow.show("Success", "Customer Deleted"); //Display a success notification if deletion is successful
                customersList.remove(selectedCustomer); 
                return customersList;
            }
        }
        NotificationWindow.show("Error", "Customer not found.");
        return customersList;
    }
    
}