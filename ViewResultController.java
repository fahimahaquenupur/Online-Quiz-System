package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.Result;
import bd.edu.seu.onlinequiz.model.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewResultController implements Initializable {

    private static String currentRole; // admin / teacher / student
    private static String currentUsername;

    public static void setCurrentRole(String role) {
        currentRole = role;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    private final ObservableList<Result> resultSourceList = FXCollections.observableArrayList();
    private final FilteredList<Result> filteredResults = new FilteredList<>(resultSourceList, p -> true);

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Result> resultTable;

    @FXML
    private TableColumn<Result, String> titleCol;

    @FXML
    private TableColumn<Result, String> nameCol;

    @FXML
    private TableColumn<Result, String> scoreCol;

    @FXML
    private TableColumn<Result, String> dateCol;

    @FXML
    private Label highestLabel;

    @FXML
    private Label lowestLabel;

    @FXML
    private Label averageLabel;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (currentUsername == null) {
            currentUsername = UserSession.getLoggedInUsername();
            currentRole = UserSession.getLoggedInRole();
        }
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQuiz_title()));
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudent_name()));
        scoreCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getObtained_marks() + " / " + data.getValue().getTotal_marks()));
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubmission_time()));

        resultTable.setItems(filteredResults);
        loadResultsFromDB();
        setupSearchFilter();
        updateScoreStats();
    }

    private void loadResultsFromDB() {
        resultSourceList.clear();



        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("ERROR: Database connection is null!");
                return;
            }

            String sql;
            PreparedStatement ps;

            if ("Teacher".equals(currentRole)) {
                sql = "SELECT r.*, q.title as quiz_title FROM results r " +
                        "JOIN quizes q ON r.quiz_id = q.id WHERE q.created_by = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, currentUsername);
                System.out.println("Teacher query executed");
            }
            else if ("Student".equals(currentRole)) {
                sql = "SELECT * FROM results WHERE student_name = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, currentUsername);
                System.out.println("Student query executed");
            }
            else {
                sql = "SELECT * FROM results";
                ps = conn.prepareStatement(sql);

            }


            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                Result result = new Result(
                        rs.getInt("id"),
                        rs.getInt("quiz_id"),
                        rs.getString("quiz_title"),
                        rs.getString("student_name"),
                        rs.getInt("obtained_marks"),
                        rs.getInt("total_marks"),
                        rs.getString("submission_time")
                );
                resultSourceList.add(result);
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredResults.setPredicate(result -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String search = newVal.toLowerCase();
                return result.getQuiz_title().toLowerCase().contains(search) ||
                        result.getStudent_name().toLowerCase().contains(search);
            });
            updateScoreStats();
        });
    }

    private void updateScoreStats() {
        var visible = filteredResults.filtered(r -> true);

        if (visible.isEmpty()) {
            highestLabel.setText("Highest: N/A");
            lowestLabel.setText("Lowest: N/A");
            averageLabel.setText("Average: N/A");
            return;
        }

        int highest = visible.stream().mapToInt(Result::getObtained_marks).max().orElse(0);
        int lowest = visible.stream().mapToInt(Result::getObtained_marks).min().orElse(0);
        double average = visible.stream().mapToInt(Result::getObtained_marks).average().orElse(0);

        highestLabel.setText("Highest: " + highest);
        lowestLabel.setText("Lowest: " + lowest);
        averageLabel.setText("Average: " + String.format("%.2f", average));
    }

    @FXML
    void backEvent() {
        if ("Admin".equals(currentRole)) {
            HelloApplication.changeScene("admin-dashboard");
        }
        else if ("Teacher".equals(currentRole)) {
            HelloApplication.changeScene("teacher-dashboard");
        }
        else if ("Student".equals(currentRole)) {
            HelloApplication.changeScene("student-dashboard");
        }
        else {
            HelloApplication.changeScene("welcome");
        }
    }
}
