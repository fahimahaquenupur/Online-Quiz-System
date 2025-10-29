package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TeacherDashboardController {

    @FXML
    void createQuizEvent(ActionEvent event) {
        HelloApplication.changeScene("createquiz");
    }



    @FXML
    void viewQuizEvent(ActionEvent event) {
        HelloApplication.changeScene("view-quiz2");
    }

    @FXML
    void viewResultEvent(ActionEvent event) {
        ViewResultController.setCurrentRole("Teacher");
        HelloApplication.changeScene("view-result");
    }

    @FXML
    void logoutEvent(ActionEvent event) {
            HelloApplication.changeScene("welcome");
    }
}
