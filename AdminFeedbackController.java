package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminFeedbackController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            loadFeedbackFromDatabase();
            feedbackListView.setItems(feedbacks);
    }

    @FXML
    private ListView<String> feedbackListView;

    private final ObservableList<String> feedbacks = FXCollections.observableArrayList();

    @FXML
    void backEvent(ActionEvent event) {
        AdminSystemController.insertLog("Admin viewed feedback and returned.");
        HelloApplication.changeScene("admin-dashboard");
    }
    private void loadFeedbackFromDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM feedback";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fb = rs.getString("submission_time") + " - " +
                        rs.getString("student_name") + " (Rating: " + rs.getInt("rating") + "/5): " +
                        rs.getString("message");
                feedbacks.add(fb);
            }
        } catch (SQLException e) {
            feedbacks.add("Error loading feedback: " + e.getMessage());
        }
    }
}
