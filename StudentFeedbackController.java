package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentFeedbackController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingCombo.getItems().addAll(1, 2, 3, 4, 5);
        ratingCombo.setValue(5);
    }


    @FXML
    private TextArea feedbackArea;

    @FXML
    private Label statusLabel;

    @FXML
    private ComboBox<Integer> ratingCombo;

    private static String currentStudentName;

    public static void setCurrentStudentName(String name) {
        currentStudentName = name;
    }
    @FXML
    void backEvent(ActionEvent event) {
        HelloApplication.changeScene("student-dashboard");
    }

    @FXML
    void handleSubmit() {
        String message = feedbackArea.getText().trim();
        Integer rating = ratingCombo.getValue();

        if (message.isEmpty()) {
            statusLabel.setText("Feedback cannot be empty.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO feedback (student_name, message, rating) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentStudentName);
            stmt.setString(2, message);
            stmt.setInt(3, rating);
            stmt.executeUpdate();

           statusLabel.setText("Feedback submitted successfully!");
            feedbackArea.clear();
            ratingCombo.setValue(5);
        } catch (SQLException e) {
            e.printStackTrace();
           statusLabel.setText("Error submitting feedback.");
        }
    }
}
