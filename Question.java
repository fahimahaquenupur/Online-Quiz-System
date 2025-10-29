package bd.edu.seu.onlinequiz.model;

public class Question {
    private int id;
    private String text;
    private String optionA, optionB, optionC, optionD;
    private String correctOption;
    private int marks;
    private String questionType;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public Question(String text, String optionA, String optionB, String optionC, String optionD, String correctOption, int marks, String questionType) {

        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.marks = marks;
        this.questionType = questionType;
    }

    public Question(int id, String text, String optionA, String optionB, String optionC,
                    String optionD, String correctOption, int marks, String questionType) {
        this.id = id;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.marks = marks;
        this.questionType = questionType;
    }
    // create table questions ( id  INT AUTO_INCREMENT PRIMARY KEY,  quiz_id INT, question TEXT, option_a VARCHAR(255), option_b VARCHAR(255),  option_c VARCHAR(255),  option_d VARCHAR(255), correct_option VARCHAR(255),  marks INT);
    // create table results( id INT AUTO_INCREMENT PRIMARY KEY, quiz_id INT, quiz_title VARCHAR(255), student_name VARCHAR(100), obtained_marks INT,  total_marks INT, submission_time DATETIME);
   // create table student_answers(  id INT AUTO_INCREMENT PRIMARY KEY, result_id INT,  question_id INT, user_answer VARCHAR(255), is_correct BOOLEAN,  marks_awarded INT, answered_at DATETIME);
    //  create table quizes (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(100),  description TEXT,   duration INT,   total_marks INT, total_questions INT, created_by VARCHAR(100));
    // create table feedback (  id INT AUTO_INCREMENT PRIMARY KEY, student_name VARCHAR(100), message TEXT,rating INT DEFAULT 5, submission_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
// create table system_logs ( id INT AUTO_INCREMENT PRIMARY KEY, message TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);
    //  create table users(id INT AUTO_INCREMENT PRIMARY KEY,  username VARCHAR(50) UNIQUE,   password VARCHAR(10), role ENUM('Admin', 'Teacher', 'Student'));

}
