  package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.Question;
import bd.edu.seu.onlinequiz.model.Quiz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class TakeQuizController implements Initializable {
    private static Quiz selectedQuiz;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Timeline timer;
    private int timeRemaining; // in seconds
    private ToggleGroup singleChoiceGroup;
    private ToggleGroup trueFalseGroup;
    private Map<Integer, String> userAnswers;
    private Map<Integer, Set<String>> multipleAnswers;
    private int quizId;

    public static void setSelectedQuiz(Quiz quiz) {
        selectedQuiz = quiz;
    }

    @FXML private RadioButton falseRadio;
    @FXML private Button nextButton;
    @FXML private CheckBox optionACheck;
    @FXML private RadioButton optionARadio;
    @FXML private CheckBox optionBCheck;
    @FXML private RadioButton optionBRadio;
    @FXML private CheckBox optionCCheck;
    @FXML private RadioButton optionCRadio;
    @FXML private CheckBox optionDCheck;
    @FXML private RadioButton optionDRadio;
    @FXML private Button previousButton;
    @FXML private Label questionNumberLabel;
    @FXML private Label questionTextLabel;
    @FXML private Label quizTitleLabel;
    @FXML private Button submitButton;
    @FXML private TextField studentIdField;
    @FXML private TextField studentNameField;
    @FXML private Label statusLabel;
    @FXML private Label timerLabel;
    @FXML private RadioButton trueRadio;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (selectedQuiz == null) {
            showAlert("Error", "No quiz selected!");
            HelloApplication.changeScene("viewquiz");
            return;
        }

        singleChoiceGroup = new ToggleGroup();
        optionARadio.setToggleGroup(singleChoiceGroup);
        optionBRadio.setToggleGroup(singleChoiceGroup);
        optionCRadio.setToggleGroup(singleChoiceGroup);
        optionDRadio.setToggleGroup(singleChoiceGroup);

        trueFalseGroup = new ToggleGroup();
        trueRadio.setToggleGroup(trueFalseGroup);
        falseRadio.setToggleGroup(trueFalseGroup);

        questions = new ArrayList<>();
        userAnswers = new HashMap<>();
        multipleAnswers = new HashMap<>();

        setupQuiz();
        loadQuizData();
        displayCurrentQuestion();
        startTimer();

    }

    private void setupQuiz() {
        quizTitleLabel.setText(selectedQuiz.getTitle());
        timeRemaining = selectedQuiz.getDuration() * 60;
        updateTimerDisplay();
        quizId = selectedQuiz.getId();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            updateTimerDisplay();
            if (timeRemaining <= 0) submitQuiz();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void loadQuizData() {
        try (Connection conn = DBConnection.getConnection()) {
            String questionSql = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement qStmt = conn.prepareStatement(questionSql);
            qStmt.setInt(1, selectedQuiz.getId());
            ResultSet qrs = qStmt.executeQuery();

            while (qrs.next()) {
                Question q = new Question(
                        qrs.getString("question"),
                        qrs.getString("option_a"),
                        qrs.getString("option_b"),
                        qrs.getString("option_c"),
                        qrs.getString("option_d"),
                        qrs.getString("correct_option"),
                        qrs.getInt("marks"),
                        qrs.getString("question_type")
                );
                q.setId(qrs.getInt("id"));
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void displayCurrentQuestion() {
        if (questions.isEmpty()) return;

        Question currentQ = questions.get(currentQuestionIndex);
        questionTextLabel.setText(currentQ.getText());
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        hideAllOptions();

        switch (currentQ.getQuestionType()) {
            case "Single Answer" -> showSingleAnswerControls(currentQ);
            case "Multiple Answer" -> showMultipleAnswerControls(currentQ);
            case "True/False" -> showTrueFalseControls();
        }

        loadPreviousAnswer();
        previousButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex == questions.size() - 1);
    }

    private void showSingleAnswerControls(Question q) {
        optionARadio.setVisible(true);
        optionBRadio.setVisible(true);
        optionCRadio.setVisible(true);
        optionDRadio.setVisible(true);

        optionARadio.setText("A. " + q.getOptionA());
        optionBRadio.setText("B. " + q.getOptionB());
        optionCRadio.setText("C. " + q.getOptionC());
        optionDRadio.setText("D. " + (q.getOptionD() != null ? q.getOptionD() : ""));

    }

    private void showMultipleAnswerControls(Question q) {
        optionACheck.setVisible(true);
        optionBCheck.setVisible(true);
        optionCCheck.setVisible(true);
        optionDCheck.setVisible(true);

        optionACheck.setText("A. " + q.getOptionA());
        optionBCheck.setText("B. " + q.getOptionB());
        optionCCheck.setText("C. " + q.getOptionC());
        optionDCheck.setText("D. " + (q.getOptionD() != null ? q.getOptionD() : ""));

    }

    private void showTrueFalseControls() {
        trueRadio.setVisible(true);
        falseRadio.setVisible(true);
        trueRadio.setText("True");
        falseRadio.setText("False");
    }

    private void loadPreviousAnswer() {
        String previousAnswer = userAnswers.get(currentQuestionIndex);
        if (previousAnswer == null) return;

        Question currentQ = questions.get(currentQuestionIndex);
        switch (currentQ.getQuestionType()) {
            case "Single Answer" -> {
                switch (previousAnswer) {
                    case "A" -> optionARadio.setSelected(true);
                    case "B" -> optionBRadio.setSelected(true);
                    case "C" -> optionCRadio.setSelected(true);
                    case "D" -> optionDRadio.setSelected(true);
                }
            }
            case "Multiple Answer" -> {
                String[] answers = previousAnswer.split(",");
                for (String ans : answers) {
                    switch (ans.trim()) {
                        case "A" -> optionACheck.setSelected(true);
                        case "B" -> optionBCheck.setSelected(true);
                        case "C" -> optionCCheck.setSelected(true);
                        case "D" -> optionDCheck.setSelected(true);
                    }
                }
            }
            case "True/False" -> {
                if ("True".equals(previousAnswer)) trueRadio.setSelected(true);
                else if ("False".equals(previousAnswer)) falseRadio.setSelected(true);
            }
        }
    }

    private void saveCurrentAnswer() {
        Question currentQ = questions.get(currentQuestionIndex);
        String questionType = currentQ.getQuestionType();
        String answer = "";

        switch (questionType) {
            case "Single Answer" -> {
                RadioButton selected = (RadioButton) singleChoiceGroup.getSelectedToggle();
                if (selected != null) answer = selected.getText().substring(0, 1);
            }
            case "Multiple Answer" -> {
                List<String> answers = new ArrayList<>();
                if (optionACheck.isSelected()) answers.add("A");
                if (optionBCheck.isSelected()) answers.add("B");
                if (optionCCheck.isSelected()) answers.add("C");
                if (optionDCheck.isSelected()) answers.add("D");
                answer = String.join(",", answers);
            }
            case "True/False" -> {
                RadioButton tfSelected = (RadioButton) trueFalseGroup.getSelectedToggle();
                if (tfSelected != null) answer = tfSelected.getText();
            }
        }

        if (!answer.isEmpty()) userAnswers.put(currentQuestionIndex, answer);
    }

    @FXML void nextQuestion(ActionEvent event) {
        saveCurrentAnswer();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayCurrentQuestion();
        }
    }

    @FXML void previousQuestion(ActionEvent event) {
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayCurrentQuestion();
        }
    }

    @FXML void submitQuiz(ActionEvent event) {
        AdminSystemController.insertLog("Student has taken a quiz");
        submitQuiz();
    }

    private void submitQuiz() {

        saveCurrentAnswer();
        String studentId = studentIdField.getText();
        String studentName = studentNameField.getText();

        if (studentId.isEmpty() || studentName.isEmpty()) {
            statusLabel.setText("Please enter student ID and name");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int totalMarks = 0, obtainedMarks = 0;
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                totalMarks += q.getMarks();
                String userAnswer = userAnswers.get(i);
                if (userAnswer != null && userAnswer.equals(q.getCorrectOption())) {
                    obtainedMarks += q.getMarks();
                }
            }

            String resultSql = "INSERT INTO results (quiz_id, quiz_title, student_name, total_marks, obtained_marks, submission_time) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(resultSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, quizId);
            ps.setString(2, selectedQuiz.getTitle());
            ps.setString(3, studentName);
            ps.setInt(4, totalMarks);
            ps.setInt(5, obtainedMarks);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int resultId = 0;
            if (rs.next()) resultId = rs.getInt(1);

            String answerSql = "INSERT INTO student_answers (result_id, question_id, user_answer, is_correct) VALUES (?, ?, ?, ?)";
            PreparedStatement answerStmt = conn.prepareStatement(answerSql);

            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                String userAnswer = userAnswers.get(i);
                boolean isCorrect = userAnswer != null && userAnswer.equals(q.getCorrectOption());
                answerStmt.setInt(1, resultId);
                answerStmt.setInt(2, q.getId());
                answerStmt.setString(3, userAnswer != null ? userAnswer : "");
                answerStmt.setBoolean(4, isCorrect);
                answerStmt.addBatch();
            }

            answerStmt.executeBatch();
            if (timer == null) timer.stop();
            statusLabel.setText("Quiz submitted successfully! Score: " + obtainedMarks + "/" + totalMarks);
            //HelloApplication.changeScene("student-dashboard");

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error submitting quiz");
        }
    }


    @FXML void backToQuizList(ActionEvent event) {
        if (timer != null) timer.stop();
        HelloApplication.changeScene("student-dashboard");
    }

    private void hideAllOptions() {
        optionARadio.setVisible(false);
        optionBRadio.setVisible(false);
        optionCRadio.setVisible(false);
        optionDRadio.setVisible(false);
        optionACheck.setVisible(false);
        optionBCheck.setVisible(false);
        optionCCheck.setVisible(false);
        optionDCheck.setVisible(false);
        trueRadio.setVisible(false);
        falseRadio.setVisible(false);

        // Also clear selected states
        singleChoiceGroup.selectToggle(null);
        trueFalseGroup.selectToggle(null);
        optionACheck.setSelected(false);
        optionBCheck.setSelected(false);
        optionCCheck.setSelected(false);
        optionDCheck.setSelected(false);
    }
}
