package bd.edu.seu.onlinequiz.model;

public class Result {
    private int id;
    private int quiz_id;
    private String quiz_title;
    private String student_name;
    private int obtained_marks;
    private int total_marks;
    private String submission_time;


    public String getQuiz_title() {
        return quiz_title;
    }

    public void setQuiz_title(String quiz_title) {
        this.quiz_title = quiz_title;
    }

    public Result(int id, int quiz_id,String quiz_title, String student_name, int obtained_marks, int total_marks, String submission_time) {
        this.id = id;
        this.quiz_id = quiz_id;
        this.quiz_title = quiz_title;
        this.student_name = student_name;
        this.obtained_marks = obtained_marks;
        this.total_marks = total_marks;
        this.submission_time = submission_time;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public int getObtained_marks() {
        return obtained_marks;
    }

    public void setObtained_marks(int obtained_marks) {
        this.obtained_marks = obtained_marks;
    }

    public int getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(int total_marks) {
        this.total_marks = total_marks;
    }

    public String getSubmission_time() {
        return submission_time;
    }

    public void setSubmission_time(String submission_time) {
        this.submission_time = submission_time;
    }
}
