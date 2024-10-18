package v2;

import java.time.*;
import java.util.*;

public class Project {

	private int projectid;
	private String title;
	private String description;
	private User projectLead; // Projektleiters als User
	private boolean isCompleted; // Projekt fertig? 
	private LocalDate startDate; // Startdatum
	private LocalDate dueDate; // Abgabetermin
	private ArrayList<User> teamMembers; // Liste aller Nutzer die an einer Aufgabe des Projektes arbeiten
	private ArrayList<Task> tasks; // Liste aller Aufgaben
	private int taskCount; // Anzahl der Aufgaben, jede neue Aufgabe -> taskCount++
	private int memberCount; // sobald ein Nutzer eine Aufgabe des Projektes bearbeitet geht memberCount++

	public Project() {
		// Leerer Konstruktor
	}

	// Konstruktor mit allen Parametern
	public Project(int projectId, String title, String description, User projectlead, boolean isCompleted,
			LocalDate startDate, LocalDate dueDate) {
		setProjectId(projectId);
		setTitle(title);
		setDescription(description);
		setProjectLead(projectlead);
		setCompleted(isCompleted);
		setStartDate(startDate);
		setDueDate(dueDate);
	}

	public int getProjectId() {
		return projectid;
	}

	public void setProjectId(int projectId) {
		this.projectid = projectId;
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

	public User getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(User projectLead) {
		this.projectLead = projectLead;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public ArrayList<User> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(ArrayList<User> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
}
