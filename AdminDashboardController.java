package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    void logoutEvent(ActionEvent event) {
        HelloApplication.changeScene("welcome");
    }

    @FXML
    void manageUserEvent(ActionEvent event) {
        HelloApplication.changeScene("manage-users");
    }

    @FXML
    void systemLogsEvent(ActionEvent event) {
        HelloApplication.changeScene("adminsystem");
    }

    @FXML
    void viewFeedbackEvent(ActionEvent event) {
        HelloApplication.changeScene("adminfeedback");
    }

    @FXML
    void viewResultsEvent(ActionEvent event) {
        ViewResultController.setCurrentRole("Admin");
        HelloApplication.changeScene("view-result");
    }

}
