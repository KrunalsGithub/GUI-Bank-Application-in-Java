//This class represents the main GUI of the app
//Krunal Patel 501175325
package coe528.project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.Scanner; 
import javafx.application.Platform;
import javafx.geometry.HPos; 
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

public class ModernBankApp extends Application {
    private Stage mainStage;
    private ArrayList<ModernCustomer> customers = new ArrayList<>();
    private ModernBankManager bankManager = new ModernBankManager();

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        primaryStage.setTitle("Toronto Metropolitan Bank");
        initializeCustomers();
        showLoginScene();
    }
    
private void initializeCustomers() {
    File dir = new File("."); 
    for (File file : dir.listFiles()) {
        if (file.getName().endsWith(".txt")) {
            try (Scanner scanner = new Scanner(file)) {
                String username = scanner.nextLine();
                String password = scanner.nextLine();
                String role = scanner.nextLine(); //Read the role line
                
                //Determine if it's a manager or customer based on the role
                if ("manager".equals(role)) {
                    //If it's a manager, no need to create a ModernCustomer instance
                    continue; //Skip the rest and move on
                }
                double balance = scanner.hasNextDouble() ? scanner.nextDouble() : 0.0;
                
                //Create a customer instance if it's not the manager
                ModernCustomer customer = new ModernCustomer(username, password, balance, "customer");
                customers.add(customer);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

private void showLoginScene() {
    //Initialize layout components
    GridPane gridpane = new GridPane();
    gridpane.setHgap(5);
    gridpane.setVgap(5);
    gridpane.setAlignment(Pos.CENTER); //Center align the gridpane itself

    //Username label and input field
    Label usernameLabel = new Label("Username:");
    TextField usernameInput = new TextField();
    usernameInput.setPromptText("Enter username");
    GridPane.setConstraints(usernameLabel, 0, 0);
    GridPane.setConstraints(usernameInput, 1, 0);
    GridPane.setHalignment(usernameLabel, HPos.CENTER);
    GridPane.setHalignment(usernameInput, HPos.CENTER);
    gridpane.getChildren().addAll(usernameLabel, usernameInput);

    //Password label and input field
    Label passwordLabel = new Label("Password:");
    PasswordField passwordInput = new PasswordField();
    passwordInput.setPromptText("Enter password");
    GridPane.setConstraints(passwordLabel, 0, 1);
    GridPane.setConstraints(passwordInput, 1, 1);
    GridPane.setHalignment(passwordLabel, HPos.CENTER);
    GridPane.setHalignment(passwordInput, HPos.CENTER);
    gridpane.getChildren().addAll(passwordLabel, passwordInput);

    //Login button with action
    Button loginButton = new Button("Login");
    loginButton.setOnAction(e -> handleLogin(usernameInput.getText(), passwordInput.getText()));
    GridPane.setConstraints(loginButton, 1, 2);
    GridPane.setHalignment(loginButton, HPos.CENTER);
    gridpane.getChildren().add(loginButton);

    //Setting up the scene and displaying it on the main stage
    VBox layout = new VBox(20); //Using VBox for vertical spacing
    layout.getChildren().add(gridpane); //Add gridpane to the layout
    layout.setAlignment(Pos.CENTER); //Center the layout

    Scene loginScene = new Scene(layout, 300, 200); //Create scene with specified layout and size
    mainStage.setScene(loginScene); //Set the scene to the main stage
    mainStage.show(); //Display the stage
}

private void handleLogin(String username, String password){
    if ("admin".equals(username) && "admin".equals(password)){
        bankManager.initializeManagerAccount(); 
        showManagerScene(); //Transition to the manager view
    } else {
        ModernCustomer loggingCustomer = findCustomerByUsernameAndPassword(username, password);
        if (loggingCustomer != null){
            showCustomerScene(loggingCustomer); //Transition to the customer view
        } else {
            NotificationWindow.show("Login Failed", "Invalid username or password."); //Show error on failed login
        }
    }
}

private ModernCustomer findCustomerByUsernameAndPassword(String username, String password) {
    for (ModernCustomer customer : customers) {
        if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) {
            return customer;
        }
    }
    return null;
}

//Manager View
private void showManagerScene() {
    Button addButton = new Button("Add Customer");
    Button deleteButton = new Button("Delete Customer");
    Button logoutButton = new Button("Log Out");
    VBox layout = new VBox(20, addButton, deleteButton, logoutButton);
    layout.setAlignment(Pos.CENTER);
    Scene managerScene = new Scene(layout, 400, 300);
    
    //When add customer button is pressed
    addButton.setOnAction(e -> {
        Stage addStage = new Stage();
        GridPane addLayout = new GridPane();
        addLayout.setVgap(10);
        addLayout.setHgap(10);
        addLayout.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Customer Username:");
        TextField customerUsernameField = new TextField();
        Label passwordLabel = new Label("Customer Password:");
        TextField customerPasswordField = new TextField();
        Label balanceLabel = new Label("Initial Balance ($):");
        TextField initialBalanceField = new TextField(); //For entering the initial balance
        Button confirmAddButton = new Button("Confirm Add");

        addLayout.add(usernameLabel, 0, 0);
        addLayout.add(customerUsernameField, 1, 0);
        addLayout.add(passwordLabel, 0, 1);
        addLayout.add(customerPasswordField, 1, 1);
        addLayout.add(balanceLabel, 0, 2);
        addLayout.add(initialBalanceField, 1, 2); //Add to layout
        addLayout.add(confirmAddButton, 1, 3);

        confirmAddButton.setOnAction(ev -> {
            String username = customerUsernameField.getText();
            String password = customerPasswordField.getText();
            double initialBalance;
            try {
                initialBalance = Double.parseDouble(initialBalanceField.getText());
            } catch (NumberFormatException ex) {
                NotificationWindow.show("Error", "Initial balance must be a number value.");
                return; 
            }
            ModernCustomer newCustomer = bankManager.addCustomer(username, password, initialBalance);
            if (newCustomer != null) {
                customers.add(newCustomer);
                NotificationWindow.show("Success", "Customer Added");
            }
        });

        Scene addScene = new Scene(addLayout, 300, 250);
        addStage.setScene(addScene);
        addStage.setTitle("Add New Customer");
        addStage.show();
    });
    
    //When delete customer button is pressed
    deleteButton.setOnAction(e -> {
        Stage deleteStage = new Stage();
        GridPane deleteLayout = new GridPane();
        deleteLayout.setVgap(10);
        deleteLayout.setHgap(10);
        deleteLayout.setAlignment(Pos.CENTER); //Align the GridPane to the center

        Label selectCustomerLabel = new Label("Enter Username of Customer to Delete:");
        GridPane.setHalignment(selectCustomerLabel, HPos.CENTER); //Center align the label
        TextField customerUsernameToDelete = new TextField();
        Button confirmDeleteButton = new Button("Confirm Delete");
        GridPane.setHalignment(confirmDeleteButton, HPos.CENTER); //Center align the button

        deleteLayout.add(selectCustomerLabel, 0, 0);
        deleteLayout.add(customerUsernameToDelete, 0, 1);
        deleteLayout.add(confirmDeleteButton, 0, 2);

        confirmDeleteButton.setOnAction(ev -> {
            String selectedUsername = customerUsernameToDelete.getText();
            customers = bankManager.deleteCustomer(selectedUsername, customers);
        });


        Scene deleteScene = new Scene(deleteLayout, 300, 200);
        deleteStage.setScene(deleteScene);
        deleteStage.setTitle("Delete Customer");
        deleteStage.show();
    });

    logoutButton.setOnAction(e -> showLoginScene()); //Log out action

    mainStage.setScene(managerScene);
    mainStage.setTitle("Toronto Metropolitan Bank");
    mainStage.show();
}

private void showCustomerScene(ModernCustomer customer){
    GridPane layout = new GridPane();
    layout.setAlignment(Pos.CENTER);
    layout.setVgap(10);
    layout.setHgap(10);

    Label balanceLabel = new Label("Balance: $" + customer.getAccountBalance());
    GridPane.setConstraints(balanceLabel, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);

    Label accountLevelLabel = new Label("Account Level: " + customer.getAccount().describeAccountType());
    GridPane.setConstraints(accountLevelLabel, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER);

    TextField depositAmountField = new TextField();
    depositAmountField.setPromptText("Amount to deposit");
    Button depositButton = new Button("Deposit");
    GridPane.setConstraints(depositAmountField, 0, 2);
    GridPane.setConstraints(depositButton, 1, 2);

    depositButton.setOnAction(e -> {
            double amount = Double.parseDouble(depositAmountField.getText());
            customer.depositMoney(amount);
            balanceLabel.setText("Balance: $" + customer.getAccountBalance());
            updateAccountLevel(customer, accountLevelLabel, balanceLabel); //Update account level and balance
            depositAmountField.clear(); 
    });

    TextField withdrawAmountField = new TextField();
    withdrawAmountField.setPromptText("Amount to withdraw");
    Button withdrawButton = new Button("Withdraw");
    GridPane.setConstraints(withdrawAmountField, 0, 3);
    GridPane.setConstraints(withdrawButton, 1, 3);

    withdrawButton.setOnAction(e -> {
            double amount = Double.parseDouble(withdrawAmountField.getText());
            customer.withdrawMoney(amount);
            balanceLabel.setText("Balance: $" + customer.getAccountBalance());
            updateAccountLevel(customer, accountLevelLabel, balanceLabel); //Update account level and balance
        withdrawAmountField.clear();
    });
 
    //Online Purchase Button
    Button onlinePurchaseButton = new Button("Online Purchase");
    GridPane.setConstraints(onlinePurchaseButton, 0, 4, 2, 1, HPos.CENTER, VPos.CENTER);
    onlinePurchaseButton.setOnAction(e -> showOnlinePurchaseScene(customer, balanceLabel, accountLevelLabel)); //Pass balanceLabel and accountLevelLabel to update
    
    Button checkBalanceButton = new Button("Check Balance");
    checkBalanceButton.setOnAction(e -> {
        NotificationWindow.show("Balance", "Your current balance is: $" + customer.getAccountBalance());
    });
    GridPane.setConstraints(checkBalanceButton, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
    
    Button logoutbutton = new Button("Log Out");
    logoutbutton.setOnAction(e -> showLoginScene()); // Log out action
    GridPane.setConstraints(logoutbutton, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);

    layout.getChildren().addAll(balanceLabel, accountLevelLabel, depositAmountField, depositButton, withdrawAmountField, withdrawButton, onlinePurchaseButton, checkBalanceButton, logoutbutton);

    Scene customerScene = new Scene(layout, 400, 300);
    mainStage.setScene(customerScene);
}

private void updateAccountLevel(ModernCustomer customer, Label accountLevelLabel, Label balanceLabel){
    //Update account level label
    String accountLevel = customer.getAccount().describeAccountType();
    Platform.runLater(() -> accountLevelLabel.setText("Account Level: " + accountLevel));

    //Update balance label
    double balance = customer.getAccountBalance();
    Platform.runLater(() -> balanceLabel.setText(String.format("Balance: $%.2f", balance)));
}

private void showOnlinePurchaseScene(ModernCustomer customer, Label balanceLabel, Label accountLevelLabel){
    Stage onlinePurchaseStage = new Stage();
    GridPane purchaseLayout = new GridPane();
    purchaseLayout.setAlignment(Pos.CENTER);
    purchaseLayout.setVgap(10);
    purchaseLayout.setHgap(10);

    TextField productNameField = new TextField();
    productNameField.setPromptText("Product Name");
    TextField productAmountField = new TextField();
    productAmountField.setPromptText("Product Price");
    Button purchaseButton = new Button("Purchase");

    purchaseButton.setOnAction(e -> {
            double amount = Double.parseDouble(productAmountField.getText());
            customer.attemptOnlinePurchase(amount);
            onlinePurchaseStage.close(); //Close the online purchase window after the operation
            //Update UI with new balance and account level
            updateAccountLevel(customer, accountLevelLabel, balanceLabel);

        } 
    );   
    purchaseLayout.add(new Label("Product Name:"), 0, 0);
    purchaseLayout.add(productNameField, 1, 0);
    purchaseLayout.add(new Label("Price:"), 0, 1);
    purchaseLayout.add(productAmountField, 1, 1);
    purchaseLayout.add(purchaseButton, 1, 2);

    Scene purchaseScene = new Scene(purchaseLayout, 300, 200);
    onlinePurchaseStage.setScene(purchaseScene);
    onlinePurchaseStage.setTitle("Online Purchase");
    onlinePurchaseStage.initModality(Modality.WINDOW_MODAL);
    onlinePurchaseStage.initOwner(mainStage);  
    onlinePurchaseStage.showAndWait();
}

public static void main(String[] args) {
    launch(args);
}
}