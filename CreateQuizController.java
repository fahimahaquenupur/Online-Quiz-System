package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.Question;
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

public class CreateQuizController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        typeComboBox.setItems(FXCollections.observableArrayList("Single Answer", "Multiple Answer", "True/False"));
        typeComboBox.setOnAction(e -> updateQuestionUI());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 180, 30);
        durationField.setValueFactory(valueFactory);

        qusCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getText()));
        ansCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCorrectOption()));
        marksCol.setCellValueFactory(c-> new SimpleStringProperty(String.valueOf(c.getValue().getMarks())));

        questionTable.setItems(questionList);

        hideAllAnswerControls();

    }
     ObservableList<Question> questionList = FXCollections.observableArrayList();

    @FXML
    private RadioButton radioA;

    @FXML
    private RadioButton radioB;

    @FXML
    private RadioButton radioC;

    @FXML
    private RadioButton radioD;

    @FXML
    private RadioButton radioFalse;

    @FXML
    private RadioButton radioTrue;

    @FXML
    private CheckBox correctA;

    @FXML
    private CheckBox correctB;

    @FXML
    private CheckBox correctC;

    @FXML
    private CheckBox correctD;

    @FXML
    private TextArea detailsField;

    @FXML
    private Spinner<Integer> durationField;

    @FXML
    private TextField createdByField;

    @FXML
    private TextField marksField;

    @FXML
    private TextField optionA;

    @FXML
    private TextField optionB;

    @FXML
    private TextField optionC;

    @FXML
    private TextField optionD;

    @FXML
    private ToggleGroup optionGroup;

    @FXML
    private TableColumn<Question, String> ansCol;

    @FXML
    private TableColumn<Question, String> marksCol;

    @FXML
    private TableView<Question> questionTable;

    @FXML
    private TableColumn<Question, String> qusCol;
    @FXML
    private TextArea qusField;

    @FXML
    private TextField qusMarksField;

    @FXML
    private Label statusLabel;

    @FXML
    private ToggleGroup tfGroup;

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<String> typeComboBox;

    private void hideAllAnswerControls() {
        radioA.setVisible(false);
        radioB.setVisible(false);
        radioC.setVisible(false);
        radioD.setVisible(false);
        correctA.setVisible(false);
        correctB.setVisible(false);
        correctC.setVisible(false);
        correctD.setVisible(false);
        radioTrue.setVisible(false);
        radioFalse.setVisible(false);
    }

    private void updateQuestionUI() {
        String type = typeComboBox.getValue();

        if (type == null) return;// যদি কিছু select না করা হয়
        hideAllAnswerControls();

        switch (type) {
            case "Single Answer":
                // Single Answer এর জন্য radio buttons show করুন
                radioA.setVisible(true);
                radioB.setVisible(true);
                radioC.setVisible(true);
                radioD.setVisible(true);

                // Multiple Answer এর checkboxes hide করুন
                correctA.setVisible(false);
                correctB.setVisible(false);
                correctC.setVisible(false);
                correctD.setVisible(false);

                // True/False radio buttons hide করুন
                radioTrue.setVisible(false);
                radioFalse.setVisible(false);

                // Options fields show করুন
                optionA.setVisible(true);
                optionB.setVisible(true);
                optionC.setVisible(true);
                optionD.setVisible(true);
                break;

            case "Multiple Answer":
                // Multiple Answer এর জন্য checkboxes show করুন
                correctA.setVisible(true);
                correctB.setVisible(true);
                correctC.setVisible(true);
                correctD.setVisible(true);

                // Single Answer এর radio buttons hide করুন
                radioA.setVisible(false);
                radioB.setVisible(false);
                radioC.setVisible(false);
                radioD.setVisible(false);

                // True/False radio buttons hide করুন
                radioTrue.setVisible(false);
                radioFalse.setVisible(false);

                // Options fields show করুন
                optionA.setVisible(true);
                optionB.setVisible(true);
                optionC.setVisible(true);
                optionD.setVisible(true);
                break;

            case "True/False":
                // True/False radio buttons show করুন
                radioTrue.setVisible(true);
                radioFalse.setVisible(true);

                // Single Answer radio buttons hide করুন
                radioA.setVisible(false);
                radioB.setVisible(false);
                radioC.setVisible(false);
                radioD.setVisible(false);

                // Multiple Answer checkboxes hide করুন
                correctA.setVisible(false);
                correctB.setVisible(false);
                correctC.setVisible(false);
                correctD.setVisible(false);

                // Options fields hide করুন (True/False এ A,B,C,D options লাগে না)
                optionA.setVisible(false);
                optionB.setVisible(false);
                optionC.setVisible(false);
                optionD.setVisible(false);
                break;
        }
    }

    @FXML
    void addQusEvent(ActionEvent event) {
        String question = qusField.getText();
        String type = typeComboBox.getValue();
        String marksStr = qusMarksField.getText();
        if (question.isEmpty() || marksStr.isEmpty() || type == null) {
            statusLabel.setText("Fill all fields!");
            return;
        }

        int marks;
        try {
            marks = Integer.parseInt(marksStr);
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid marks");
            return;
        }

        String ans = "";
        switch (type) {
            case "Single Answer" -> {
                RadioButton selected = (RadioButton) optionGroup.getSelectedToggle();
                if (selected != null) ans = selected.getText();
            }
            case "Multiple Answer" -> {
                if (correctA.isSelected()) ans += "A,";
                if (correctB.isSelected()) ans += "B,";
                if (correctC.isSelected()) ans += "C,";
                if (correctD.isSelected()) ans += "D,";
                if (!ans.isEmpty()) ans = ans.substring(0, ans.length() - 1);
            }
            case "True/False" -> {
                RadioButton selected = (RadioButton) tfGroup.getSelectedToggle();
                if (selected != null) ans = selected.getText();
            }
        }

        Question q = new Question(

                question,
                optionA.getText(),
                optionB.getText(),
                optionC.getText(),
                optionD.getText(),
                ans,
                marks,
                type
        );

        questionList.add(q);
        statusLabel.setText("Question added successfully!");
        clearQuestionFields();
    }

    @FXML
    void deleteQusEvent(ActionEvent event) {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            questionList.remove(selected);
            statusLabel.setText("Question deleted successfully!");
        } else {
            statusLabel.setText("Please select a question to delete!");
        }
    }

    @FXML
    void saveQuizEvent(ActionEvent event) {
        String title = titleField.getText();
        String details = detailsField.getText();
        int duration = durationField.getValue();
        String totalMarks = marksField.getText();
        String createdBy = createdByField.getText();
        typeComboBox.setValue(title);

        if (title.isEmpty() || details.isEmpty() || totalMarks.isEmpty()) {
            statusLabel.setText("Fill quiz information");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String quizSql = "INSERT INTO quizes (title, description, duration, total_marks, created_by) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement quizStmt = conn.prepareStatement(quizSql, Statement.RETURN_GENERATED_KEYS);
            quizStmt.setString(1, title);
            quizStmt.setString(2, details);
            quizStmt.setInt(3, duration);
            quizStmt.setString(4, totalMarks);
            quizStmt.setString(5, createdBy);
            quizStmt.executeUpdate();

            ResultSet rs = quizStmt.getGeneratedKeys();
            int quizId = 0;
            if (rs.next()) quizId = rs.getInt(1);

            String questionSql = "INSERT INTO questions (quiz_id, question, option_a, option_b, option_c, option_d, correct_option, marks, question_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement qStmt = conn.prepareStatement(questionSql);

            for (Question q : questionList) {

                qStmt.setInt(1, quizId);
                qStmt.setString(2, q.getText());
                qStmt.setString(3, q.getOptionA());
                qStmt.setString(4, q.getOptionB());
                qStmt.setString(5, q.getOptionC());
                qStmt.setString(6, q.getOptionD());
                qStmt.setString(7, q.getCorrectOption());
                qStmt.setInt(8, q.getMarks());
                qStmt.setString(9,q.getQuestionType());
                qStmt.executeUpdate();
            }


            statusLabel.setText("Quiz saved!");

            clearAllFields();

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error saving quiz");
        }
        AdminSystemController.insertLog("Teacher created a quiz.");
    }

    @FXML
    void backEvent(ActionEvent event) {
        HelloApplication.changeScene("teacher-dashboard");
    }

    private void clearAllFields() {
        titleField.clear();
        detailsField.clear();
        marksField.clear();
        durationField.getValueFactory().setValue(30);
        typeComboBox.getSelectionModel().clearSelection();
        questionList.clear();
        clearQuestionFields();
        hideAllAnswerControls();
    }
    private void clearQuestionFields() {
        qusField.clear();
        qusMarksField.clear();
        optionA.clear();
        optionB.clear();
        optionC.clear();
        optionD.clear();

        // Clear selections
        if (optionGroup.getSelectedToggle() != null) {
            optionGroup.getSelectedToggle().setSelected(false);
        }
        if (tfGroup.getSelectedToggle() != null) {
            tfGroup.getSelectedToggle().setSelected(false);
        }

        correctA.setSelected(false);
        correctB.setSelected(false);
        correctC.setSelected(false);
        correctD.setSelected(false);
    }

}
