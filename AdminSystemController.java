package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminSystemController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertLog("Admin opened system logs view.");
        logListView.setItems(logs);
        loadLogsFromDatabase();

    }

    @FXML
    private ListView<String> logListView;
    private final ObservableList<String> logs = FXCollections.observableArrayList();

    private void loadLogsFromDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM system_logs";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String log = rs.getString("timestamp") + " - " + rs.getString("message");
                logs.add(log);
            }
        } catch (SQLException e) {
            logs.add("Error loading logs: " + e.getMessage());
        }
    }
    @FXML
    void handleBack(ActionEvent event) {
        insertLog("Admin exited system logs view.");
        HelloApplication.changeScene("admin-dashboard");
    }

    @FXML
    void handleExport(ActionEvent event) {
        try (FileWriter writer = new FileWriter("system_logs_export.txt")) {
            for (String log : logs) {
                writer.write(log + "\n");
            }
            logs.add("\nLogs exported to system_logs_export.txt");
        } catch (IOException e) {
            logs.add("Failed to export logs: " + e.getMessage());
        }
    }

    // Optional: call this to auto-insert log message from other controllers
    public static void insertLog(String message) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO system_logs (message) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to insert log: " + e.getMessage());
        }
    }
}

