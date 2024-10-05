package v2;

import java.time.LocalDateTime;

public class Worktime {

	private int id;
	private User user;
	private Task task;
	private LocalDateTime startTime; // Start Arbeitszeit
	private LocalDateTime endTime; // Ende Arbeitszeit
	private int problemCounter; // Anzahl Probleme -> kommt später noch was
	private String comment; // Kommentare der letzten Sitzung, zur Rückverfolgung der Arbeit
	private boolean overtime; // gibt an ob Arbeitszeit Überstunden enthält

	public Worktime() {
		// leerer Konstruktor
	}

	// Standart-Konstruktor
	public Worktime(User user, Task task) {
		setUser(user);
		setTask(task);
		setStartTime(LocalDateTime.now());
	}

	// Kontsruktor mit allen Parametern, für DB export
	public Worktime(int id, Task task, User user, LocalDateTime startTime, LocalDateTime endTime, int problemCounter,
			String comment, boolean overtime) {
		setId(id);
		setUser(user);
		setTask(task);
		setStartTime(startTime);
		setEndTime(endTime);
		setProblemCounter(problemCounter);
		setComment(comment);
		setOvertime(overtime);

	}

	// Arbeit an der Aufgabe beenden, Frage ob Aufgabe fertig ist 
	// wenn ja -> task.setCompleted(true)
	public void endTask() {
		this.endTime = LocalDateTime.now();
		// TODO Aufgabe beendet Abfrage und 
	}

	// Getter und Setter
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public int getProblemCounter() {
		return problemCounter;
	}

	public void setProblemCounter(int problems2) {
		this.problemCounter = problems2;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isOvertime() {
		return overtime;
	}

	public void setOvertime(boolean overtime) {
		this.overtime = overtime;
	}

}
