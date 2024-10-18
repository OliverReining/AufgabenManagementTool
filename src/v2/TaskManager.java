package v2;

import java.sql.*;
import java.time.*;
import java.util.*;

import v2.Task.Priority;

//Manager-Klasse für Aufgaben:
//Verwaltet Aufgaben über eine ArrayList<Task> und ermöglicht Interaktionen mit der Datenbank
//Diese Klasse ist verantwortlich für das Hinzufügen, Entfernen, Aktualisieren und Abrufen von Aufgaben
//sowie die Interaktion mit anderen Managern (z.B. ProjectManager) für projektbezogene Aufgabenabfragen

public class TaskManager {

	// Lokale Variablen
	private ArrayList<Task> tasks; // Liste aller Aufgaben, die in der Anwendung verwendet werden
	private DatabaseConnection dbConnect; // Verbindung zur Datenbank
	private LogManager log; // LogManager zum Protokollieren von Informationen und Fehlern
	private ProjectManager pMan;
	private UserManager uMan;

	// Konstruktoren
	public TaskManager() {
		tasks = new ArrayList<>(); // Initialisiert die Task-Liste ohne Datenbankverbindung
	}

	public TaskManager(DatabaseConnection dbConnect) {
		this.dbConnect = dbConnect; // Setzt die Datenbankverbindung
		getAllTasks(); // Lädt Aufgaben aus der Datenbank
	}

	public TaskManager(DatabaseConnection dbConnect, LogManager log, ProjectManager pMan, UserManager uMan) {
		this.log = log; // Setzt LogManager
		this.dbConnect = dbConnect; // Setzt die Datenbankverbindung
		this.pMan = pMan;
		this.uMan = uMan;
		getAllTasks(); // Lädt Aufgaben aus der Datenbank
	}

	// Methode zum Laden der Aufgaben aus der Datenbank
	// Liest alle Aufgaben aus der Tabelle "task" in der Datenbank und fügt sie der
	// lokalen Task-Liste hinzu
	public void getAllTasks() {
		tasks = new ArrayList<>();
		String sql = "SELECT * FROM task";
		log.log("Versuche '" + sql + "' auszuführen...", Log.LogType.INFO, Log.Manager.TASK_MANAGER);

		try (ResultSet rs = dbConnect.getConnection().prepareStatement(sql).executeQuery()) {
			while (rs.next()) {
				// Erstellen eines neuen Task-Objekts basierend auf den DB-Daten
				Task task = new Task();
				task.setTaskId(rs.getInt("taskid"));
				task.setTitle(rs.getString("title"));
				task.setDescription(rs.getString("description"));
				task.setProject(pMan.getProjectById(rs.getInt("projectid")));
				task.setDueDate(rs.getDate("dueDate").toLocalDate());
				task.setPriority(task.toPriority(rs.getString("priority")));
				task.setCompleted(rs.getBoolean("isCompleted"));
				task.setPlannedTime(rs.getInt("plannedTime"));
				task.setCritical(rs.getBoolean("isCritical"));
				tasks.add(task); // Aufgabe zur lokalen Liste hinzufügen
			}
			log.log(tasks.size() + " Aufgaben gefunden.", Log.LogType.SUCCESS, Log.Manager.TASK_MANAGER);
			setTasks(tasks); // Liste lokal speichern
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// Methode um eine Aufgabe anhand ihrer ID zu finden
	public Task getTaskById(int taskId) {
		// Schleife durch alle Task in der TaskList
		for (Task task : tasks) {
			// wenn TaskID übereinstimmt wird sie zurückgegeben
			if (taskId == task.getTaskId()) {
				return task; // Task gefunden und zurückgegeben
			}
		}
		log.log("TaskID konnte nicht gefunden werden", Log.LogType.ERROR, Log.Manager.TASK_MANAGER);
		return null; // Kein Task gefunden
	}

	// Methode zum Hinzufügen einer neuen Aufgabe zur Liste
	public void addTask(Task task) {
		tasks.add(task); // Aufgabe zur lokalen Liste hinzufügen
		// TODO: SQL-INSERT-Anweisung hier implementieren, um die Aufgabe in die
		// Datenbank einzufügen
	}

	// Methode zum Erstellen einer neuen Aufgabe und Hinzufügen zur Liste
	public void createTask(String title, String description, int projectId, LocalDate dueDate, Priority priority,
			boolean isCompleted) {
		Task task = new Task(); // Neuer Task initialisieren und Parameter setzen
		task.setTitle(title);
		task.setDescription(description);
		task.setProject(pMan.getProjectById(projectId));
		task.setDueDate(dueDate);
		task.setPriority(priority);
		task.setCompleted(isCompleted);
		addTask(task); // Aufgabe zur Liste hinzufügen
		// TODO: SQL-INSERT-Anweisung hier implementieren, um die Aufgabe in die
		// Datenbank einzufügen
	}

	// Methode zum Entfernen einer Aufgabe aus der Liste und der Datenbank
	public void removeTask(Task task) {
		tasks.remove(task); // Aufgabe aus der lokalen Liste entfernen
		// TODO: SQL-DELETE-Anweisung hier implementieren, um die Aufgabe aus der
		// Datenbank zu löschen
	}

	// Methode zum Entfernen einer Aufgabe anhand ihrer ID
	public void removeTaskById(int taskId) {
		removeTask(getTaskById(taskId)); // Sucht die Aufgabe und entfernt sie
	}

	// Methode zum Aktualisieren einer Aufgabe
	public void updateTask(Task selectedTask) {
		for (Task task : tasks) {
			if (task.equals(selectedTask)) {
				// TODO: SQL-UPDATE-Anweisung hier implementieren, um die Task-Daten in der
				// Datenbank zu aktualisieren
				return;
			}
		}
	}

	// Methode zum Abrufen aller Aufgaben eines bestimmten Projekts
	public ArrayList<Task> getTasksByProject(Project project) {
		ArrayList<Task> tasksByProject = new ArrayList<>();
		// TODO: SQL-SELECT-Anweisung hier implementieren, um die Aufgaben für das
		// angegebene Projekt zu laden
		return tasksByProject;
	}

	// TODO addTask(Task task)
	// - Methode um eine neue Aufgabe in die Datenbank einzufügen
	// - SQL-INSERT-Befehl vorbereiten, um die Taskdaten in die Datenbank zu
	// schreiben
	// - Aufgabe in die lokale ArrayList hinzufügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO removeTask(Task task)
	// - Methode um eine Aufgabe aus der Datenbank zu löschen
	// - SQL-DELETE-Befehl vorbereiten, um die Aufgabe anhand ihrer ID zu entfernen
	// - Aufgabe aus der lokalen ArrayList entfernen
	// - Exception Handling hinzufügen, falls das Löschen fehlschlägt

	// TODO updateTask(Task task)
	// - Methode um Aufgabendaten in der Datenbank zu aktualisieren
	// - SQL-UPDATE-Befehl vorbereiten, um die geänderten Taskdaten in der Datenbank
	// zu speichern
	// - Lokale Aufgabendaten in der ArrayList aktualisieren
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getTaskById(int taskId)
	// - Methode um eine Aufgabe anhand ihrer ID aus der Datenbank abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um die Taskdaten zu laden
	// - Aufgabe in die lokale ArrayList einfügen, falls nicht bereits vorhanden
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getAllTasks()
	// - Methode um alle Aufgaben aus der Datenbank abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um eine Liste aller Aufgaben zu laden
	// - Aufgaben in die lokale ArrayList einfügen
	// - Exception Handling hinzufügen

	// TODO getTasksByProject(Project project)
	// - Methode um alle Aufgaben zu finden, die mit einem bestimmten Projekt
	// verknüpft sind
	// - SQL-SELECT-Befehl vorbereiten, um die Aufgaben basierend auf der Projekt-ID
	// zu laden
	// - Aufgaben in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getTasksByUser(User user)
	// - Methode um alle Aufgaben zu finden, die einem bestimmten Benutzer
	// zugewiesen sind
	// - SQL-SELECT-Befehl vorbereiten, um die Aufgaben basierend auf der
	// Benutzer-ID zu laden
	// - Aufgaben in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getCompletedTasks()
	// - Methode um alle abgeschlossenen Aufgaben abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um Aufgaben mit dem Status "abgeschlossen"
	// zu laden
	// - Abgeschlossene Aufgaben in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getActiveTasks()
	// - Methode um alle aktiven Aufgaben abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um Aufgaben mit dem Status "aktiv" zu laden
	// - Aktive Aufgaben in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO searchTasks(String attribute, String value)
	// - Methode um Aufgaben anhand eines bestimmten Attributs zu suchen (z.B. Titel
	// oder Priorität)
	// - SQL-SELECT-Befehl mit WHERE-Klausel vorbereiten, um nach dem Attribut zu
	// suchen
	// - Aufgaben entsprechend der Suchkriterien aus der Datenbank laden und in die
	// ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO validateTaskData(Task task)
	// - Methode zur Validierung der Taskdaten vor dem Einfügen oder Aktualisieren
	// in der Datenbank
	// - Überprüfen, ob alle Pflichtfelder ausgefüllt sind (z.B. Titel,
	// Projektzuweisung, Fälligkeitsdatum)
	// - Falls Validierung fehlschlägt, eine Exception werfen oder eine
	// Fehlermeldung zurückgeben

	// TODO manageDatabaseTransaction()
	// - Methode für grundlegendes Transaktionsmanagement
	// - Sicherstellen, dass alle Änderungen in der Datenbank atomar ablaufen
	// (Transaktionen starten, committen, und ggf. zurückrollen)
	// - Transaktionen für die Methoden addTask, removeTask und updateTask
	// implementieren

	// TODO handleDatabaseExceptions()
	// - Methode für Exception Handling bei Datenbankoperationen
	// - SQLExceptions und andere mögliche Fehler abfangen
	// - Benutzer oder Entwickler über Fehler informieren (Logging oder
	// GUI-Benachrichtigung)

	// Getter und Setter für die TaskList
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	private void setTaskParameters(PreparedStatement stmt, List<String> columnNames, User user) {
		// TODO Auto-generated method stub

	}

}
