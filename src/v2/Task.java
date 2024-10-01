package v2;

public class Task {

    private int taskId;
    private String title;
    private String description;
    private int projectId;
    private boolean isCompleted;

    public Task() {
        // leerer Konstruktor
    }

    public Task(int taskId, String title, String description, int projectId, boolean isCompleted) {
        setTaskId(taskId);
        setTitle(title);
        setDescription(description);
        setProjectId(projectId);
        setCompleted(isCompleted);
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getProjectId() {
        return projectId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

}
