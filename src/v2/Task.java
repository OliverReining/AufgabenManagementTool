package v2;

import java.time.*;
import java.util.ArrayList;

import v2.Task.Priority;

public class Task {

	private int taskId;
	private String title;
	private String description;
	private int projectId;
	private LocalDate dueDate;
	private Priority priority;
	private boolean isCompleted;
	private int plannedTime; // geschätzte Zeit in Stunden
	private boolean isCritical; // Aufgabe kritisch für Erfolg des Projektes?
	private ArrayList<User> teamMembers; // Liste aller mitarbeitenden Nutzer
	private int memberCount; // Anzahl der Mitarbeiter der Aufgabe
	private ArrayList<String> TODOs;

	// Wenn ich weiß wie ich das gescheit von der DB umgewandelt bekomme dann mach
	// ich das so
	public enum Priority {
		none, low, average, high, very_high, overdue
	}

	public Task() {
		// leerer Konstruktor
	}

	public Task(int taskId, String title, String description, int projectId, LocalDate dueDate, Priority priority,
			boolean isCompleted, int plannedTime, boolean isCritical) {
		setTaskId(taskId);
		setTitle(title);
		setDescription(description);
		setProjectId(projectId);
		setDueDate(dueDate);
		setPriority(priority);
		setCompleted(isCompleted);
		setPlannedTime(plannedTime);
		setCritical(isCritical);
	}

	// Getter und Setter
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

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public int getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(int plannedTime) {
		this.plannedTime = plannedTime;
	}

	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	public ArrayList<User> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(ArrayList<User> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public ArrayList<String> getTODOs() {
		return TODOs;
	}

	public void setTODOs(ArrayList<String> tODOs) {
		TODOs = tODOs;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	// Methode um die Priority in einen String zu wandeln (für DB-Query)
	public String priorityToString(Priority priority) {
		switch (priority) {
		case none:
			return "keine";
		case low:
			return "niedrig";
		case average:
			return "mittel";
		case high:
			return "hoch";
		case very_high:
			return "sehr hoch";
		case overdue:
			return "überfällig";
		default:
			return null;
		}
	}

	// Methode um den Priority-String aus der DB in eine Priority umzuwandeln
	public Priority toPriority(String priorityString) {
		/*
		 * none, low, average, high, very_high, overdue,
		 */
		switch (priorityString) {
		case "keine":
			return Task.Priority.none;
		case "niedrig":
			return Task.Priority.low;
		case "mittel":
			return Task.Priority.average;
		case "hoch":
			return Task.Priority.high;
		case "sehr hoch":
			return Task.Priority.very_high;
		case "überfällig":
			return Task.Priority.overdue;
		default:
			return null;
		}
	}

}