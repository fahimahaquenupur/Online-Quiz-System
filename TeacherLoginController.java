package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherLoginController {

    @FXML
    private PasswordField teacherPasswordField;

    @FXML
    private TextField teacherNameField;

    @FXML
    private Label nameError;

    @FXML
    private Label passError;


    @FXML
    void backEvent(ActionEvent event) {
        HelloApplication.changeScene("welcome");
    }

    @FXML
    void goToTeacherDashboard(ActionEvent event) {
        boolean valid = true;

        String username = this.teacherNameField.getText();
        String password = teacherPasswordField.getText();


        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("id");

                HelloApplication.changeScene("teacher-dashboard");
            } else if (username.isEmpty() || password.isEmpty()) {
                passError.setText("Fill all the fields");
            } else {
                passError.setText("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        valid = false;
    }

}
