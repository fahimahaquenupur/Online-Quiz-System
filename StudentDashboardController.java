package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StudentDashboardController {

    @FXML
    void logoutEvent(ActionEvent event) {
        HelloApplication.changeScene("welcome");
    }

    @FXML
    void viewQuizEvent(ActionEvent event) {
    HelloApplication.changeScene("viewquiz");
    }

    @FXML
    void viewResultEvent(ActionEvent event) {
        ViewResultController.setCurrentRole("Student");
        HelloApplication.changeScene("view-result");
    }
    @FXML
    void submitFeedback(ActionEvent event) {
        HelloApplication.changeScene("studentfeedback");
    }

}
