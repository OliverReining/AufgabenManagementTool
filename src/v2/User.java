package v2;

import java.util.*;

public class User {

	private String userId;
	private String name;
	private String vorname;
	private String email;
	private String tel;
//	private String hashedPass; // TODO: Hash-Funktion, um die Passwörter nicht in klarschrift in die DB zu geben
	// TODO Methode um die gehashten PW auszulesen und zu vergleichen
	private String pass;
	private String role; // TODO: weitere Rollen hinzufügen, basierend darauf zeigt GUI anderes
	private boolean projectLead; // wenn ProjectLead eines aktuellen Projekt -> true 
	private double hourlyRate; // StundenSatz des Nutzers

	// Ab hier errechnete Daten die nicht aus DB kommen
	private ArrayList<Task> userTasks;
	private ArrayList<Project> userProjects;
	private ArrayList<Worktime> userTimes;
	private int userTaskCount;
	private int userProjectCount;
	private double userTimeCount;

	// Aufgaben gesamt -> ArrayList<Task> userTasks -> über Worktime (task_user in
	// DB)
	// aktuelle Aufgaben -> Abfrage: userTasks != isCompleted
	// Aufgaben überfällig -> Abfrage: dueDate > Abgabetermin && task != isCompleted
	// erledigte Aufgaben -> Abfrage: aus userTask
	// Projekte gesamt -> ArrayList<Project> userProjects
	// aktuelle Projekte
	// Projekte überfällig
	// erledigte Projekte

	public User() {
		// leerer Konstruktor
	}

	// Konstruktor mit allen Werten um
	public User(String userId, String name, String vorname, String email, String tel, String pass, String role,
			boolean projectLead, double hourlyRate) {
		setUserId(userId);
		setName(name);
		setVorname(vorname);
		setEmail(email);
		setTel(tel);
		setPass(pass);
		setRole(role);
		setProjectLead(projectLead);
		setHourlyRate(hourlyRate);
	}

	@Override
	public String toString() {
		return getName() + ", " + getVorname() + "\n" + getEmail() + "\n" + getTel();
	}

	// Getter und Setter
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isProjectLead() {
		return projectLead;
	}

	public void setProjectLead(boolean projectLead) {
		this.projectLead = projectLead;
	}

	public ArrayList<Task> getUserTasks() {
		return userTasks;
	}

	public void setUserTasks(ArrayList<Task> userTasks) {
		this.userTasks = userTasks;
	}

	public ArrayList<Project> getUserProjects() {
		return userProjects;
	}

	public void setUserProjects(ArrayList<Project> userProjects) {
		this.userProjects = userProjects;
	}

	public ArrayList<Worktime> getUserTimes() {
		return userTimes;
	}

	public void setUserTimes(ArrayList<Worktime> userTimes) {
		this.userTimes = userTimes;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public int getUserTaskCount() {
		return userTaskCount;
	}

	public void setUserTaskCount(int userTaskCount) {
		this.userTaskCount = userTaskCount;
	}

	public int getUserProjectCount() {
		return userProjectCount;
	}

	public void setUserProjectCount(int userProjectCount) {
		this.userProjectCount = userProjectCount;
	}

	public double getUserTimeCount() {
		return userTimeCount;
	}

	public void setUserTimeCount(double userTimeCount) {
		this.userTimeCount = userTimeCount;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
