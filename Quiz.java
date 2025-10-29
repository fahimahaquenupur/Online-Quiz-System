package bd.edu.seu.onlinequiz.model;

public class Quiz {
    private int id;
    private String title;
    private String description;
    private int duration; // in minutes
    private int totalMarks;
    private int totalQuestions;
    private String createdBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Quiz(int id, String title, String description, int duration, int totalMarks, int totalQuestions, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.totalMarks = totalMarks;
        this.totalQuestions = totalQuestions;
        this.createdBy = createdBy;
    }

}
