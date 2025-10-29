package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WelcomeController {
    @FXML
    void goToAdmin(ActionEvent event) {
            HelloApplication.changeScene("adminlogin");
    }

    @FXML
    void goToStudent(ActionEvent event) {
        HelloApplication.changeScene("studentlogin");
    }

    @FXML
    void goToTeacher(ActionEvent event) {
        HelloApplication.changeScene("teacherlogin");
    }

    @FXML
    void registrationEvent(ActionEvent event) {
        HelloApplication.changeScene("registration");
    }

}
