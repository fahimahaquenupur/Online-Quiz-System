package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.Quiz;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ViewQuizController implements Initializable {
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

        quizTable.setOnMouseClicked(this::handleQuizTableClick);
    }

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

    ObservableList<Quiz> quizList = FXCollections.observableArrayList();



    @FXML
    void backEvent(ActionEvent event) {

        HelloApplication.changeScene("student-dashboard");

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded quizzes: " + quizList.size());
    }

    private void filterQuizList(String filter) {
        ObservableList<Quiz> filteredList = FXCollections.observableArrayList();
        for(Quiz q : quizList) {
            if (q.getId() == Integer.parseInt(filter) || q.getTitle().toLowerCase().contains(filter.toLowerCase()) || q.getDescription().toLowerCase().contains(filter.toLowerCase()) || q.getDuration() == Integer.parseInt(filter) || q.getTotalMarks() == Integer.parseInt(filter) || q.getTotalQuestions() == Integer.parseInt(filter) || q.getCreatedBy().toLowerCase().contains(filter.toLowerCase())) {
                filteredList.add(q);
            }
        }
        quizTable.setItems(filteredList);
    }

    @FXML
    void handleQuizTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Double click
            Quiz selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
            if (selectedQuiz != null) {
                TakeQuizController.setSelectedQuiz(selectedQuiz);
                HelloApplication.changeScene("takequiz");
            }
        }
    }
}

