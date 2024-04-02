package coe528.project;

import javafx.scene.control.Alert;
//Krunal Patel 501175325
//Notification Window pops up when used.
public class NotificationWindow {

    //Displays an alert box with the given title and message
    public static void show(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
