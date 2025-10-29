package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.Quiz;
import bd.edu.seu.onlinequiz.model.User;
import bd.edu.seu.onlinequiz.model.UserSession;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewQuiz2Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getTitle()));
        descriptionCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getDescription()));
        durationCol.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getDuration()));
        totalMarksCol.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getTotalMarks()));

        loadQuizes();

        quizTable.setItems(quizList);

        searchField.textProperty().addListener((obs, wasSelected, isSelected) -> {
            filterQuizList(isSelected);
        });


    }

    @FXML
    private Label deleteLabel;

    @FXML
    private TableColumn<Quiz, String> descriptionCol;

    @FXML
    private TableColumn<Quiz, Number> durationCol;

    @FXML
    private TableView<Quiz> quizTable;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<Quiz, String> titleCol;

    @FXML
    private TableColumn<Quiz, Number> totalMarksCol;

    private User currentUser;

    ObservableList<Quiz> quizList = FXCollections.observableArrayList();

    @FXML
    void backEvent(ActionEvent event) {
        HelloApplication.changeScene("teacher-dashboard");
    }

    @FXML
    void deleteEvent(ActionEvent event) {
        Quiz selected = quizTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            deleteLabel.setText("Select a user to delete!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Quiz");
        confirmAlert.setHeaderText("Are you sure you want to delete this quiz?");
        confirmAlert.setContentText("Quiz: " + selected.getTitle() + "\nThis action cannot be undone.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try (Connection conn = DBConnection.getConnection()) {
                // Delete associated questions first
                String deleteQuestionsSQL = "DELETE FROM questions WHERE quiz_id = ?";
                PreparedStatement deleteQuestionsStmt = conn.prepareStatement(deleteQuestionsSQL);
                deleteQuestionsStmt.setInt(1, selected.getId());
                deleteQuestionsStmt.executeUpdate();

                // Delete the quiz
                String deleteQuizSQL = "DELETE FROM quizes WHERE id = ?";
                PreparedStatement deleteQuizStmt = conn.prepareStatement(deleteQuizSQL);
                deleteQuizStmt.setInt(1, selected.getId());
                deleteQuizStmt.executeUpdate();

                deleteLabel.setText("Quiz deleted successfully!");
                deleteLabel.setStyle("-fx-text-fill: green;");
                loadQuizes();

            } catch (SQLException e) {
                e.printStackTrace();
                deleteLabel.setText("Failed to delete quiz!");
                deleteLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }


    private void loadQuizes() {
        quizList.clear();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM quizes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int count = 0;
            while (rs.next()) {
                Quiz quiz = new Quiz(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration"),
                        rs.getInt("total_marks"),
                        rs.getInt("total_questions"),
                        rs.getString("created_by")
                );
                quizList.add(quiz);
                count++;
            }
            System.out.println("Loaded " + quizList.size() + " quizzes for teacher: " + currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load quizzes: " + e.getMessage());
        }

    }

    private void filterQuizList(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            quizTable.setItems(quizList);
            return;
        }

        ObservableList<Quiz> filteredList = FXCollections.observableArrayList();
        String lowerFilter = filter.toLowerCase();

        for(Quiz q : quizList) {
            boolean matches = false;

            // Check title
            if (q.getTitle() != null && q.getTitle().toLowerCase().contains(lowerFilter)) {
                matches = true;
            }

            // Check description
            if (q.getDescription() != null && q.getDescription().toLowerCase().contains(lowerFilter)) {
                matches = true;
            }

            // Check created by
            if (q.getCreatedBy() != null && q.getCreatedBy().toLowerCase().contains(lowerFilter)) {
                matches = true;
            }

            // Check numeric fields
            try {
                int numericFilter = Integer.parseInt(filter);
                if (q.getId() == numericFilter || q.getDuration() == numericFilter ||
                        q.getTotalMarks() == numericFilter || q.getTotalQuestions() == numericFilter) {
                    matches = true;
                }
            } catch (NumberFormatException e) {
                // Not a number, ignore
            }

            if (matches) {
                filteredList.add(q);
            }
        }

        quizTable.setItems(filteredList);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
