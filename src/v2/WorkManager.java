package v2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

//Manager-Klasse für spezielle Abfragen, die viele Parameter benötigen
//Diese Klasse vereint die Funktionalitäten von UserManager, ProjectManager und TaskManager
//Sie verwaltet zusätzlich die task_user-Kreuztabelle "Worktime" aus der Datenbank

public class WorkManager {

	// Lokale Variablen
	private ArrayList<Worktime> times; // Liste aller Worktime-Einträge (task_user)
	private DatabaseConnection dbConnect; // Verbindung zur Datenbank
	private LogManager log; // LogManager zum Protokollieren von Informationen und Fehlern
	private UserManager uMan; // Verwalter für Benutzer
	private ProjectManager pMan; // Verwalter für Projekte
	private TaskManager tMan; // Verwalter für Aufgaben

	// Konstruktoren
	public WorkManager() {
		dbConnect = new DatabaseConnection(); // Initialisiere Datenbankverbindung
		uMan = new UserManager(); // Initialisiere UserManager
		pMan = new ProjectManager(); // Initialisiere ProjectManager
		tMan = new TaskManager(); // Initialisiere TaskManager
		getTaskUserFromDB(); // Lade Worktime-Daten
	}

	public WorkManager(DatabaseConnection dbConnect) {
		this.dbConnect = dbConnect; // Setze Datenbankverbindung
		tMan = new TaskManager(dbConnect); // Initialisiere TaskManager mit Datenbankverbindung
		uMan = new UserManager(dbConnect); // Initialisiere UserManager mit Datenbankverbindung
		pMan = new ProjectManager(dbConnect); // Initialisiere ProjectManager mit Datenbankverbindung
		getTaskUserFromDB(); // Lade Worktime-Daten
	}

	public WorkManager(DatabaseConnection dbConnect, LogManager log, UserManager userManager,
			ProjectManager projectManager, TaskManager taskManager) {
		this.dbConnect = dbConnect; // Setze Datenbankverbindung
		this.log = log; // Setze LogManager
		this.uMan = userManager; // Setze UserManager
		this.pMan = projectManager; // Setze ProjectManager
		this.tMan = taskManager; // Setze TaskManager
		getTaskUserFromDB(); // Lade Worktime-Daten
	}

	// Methode zum Laden der Worktime-Daten (task_user) aus der Datenbank
	private void getTaskUserFromDB() {
		times = new ArrayList<>();
		String sql = "SELECT * FROM task_user;";

		log.log("Versuche '" + sql + "' auszuführen...", Log.LogType.INFO);

		try (ResultSet rs = dbConnect.getConnection().prepareStatement(sql).executeQuery()) {
			while (rs.next()) {
				// Erstellen eines neuen Worktime-Objekts basierend auf den DB-Daten
				int id = rs.getInt("id");
				Task task = tMan.getTaskById(rs.getInt("taskid"));
				User user = uMan.getUserById(rs.getString("userid"));
				LocalDateTime startTime = rs.getTimestamp("starttime").toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp("endtime").toLocalDateTime();
				int problems = rs.getInt("problems");
				String comment = rs.getString("commentar");
				boolean overtime = rs.getBoolean("overtime");

				Worktime worktime = new Worktime(id, task, user, startTime, endTime, problems, comment, overtime);
				times.add(worktime); // Worktime zur Liste hinzufügen
			}
			setTimes(times); // Liste lokal speichern
		} catch (SQLException e) {
			log.sqlExceptionLog(e, sql); // Fehler protokollieren
		}
	}

	// Methode um die Gesamtarbeitszeit eines Benutzers an einem bestimmten Projekt
	// zu berechnen
	public void getTotalWorktimeForUserOnProject(User user, Project project) {
		// TODO: Implementierung der Berechnung der Gesamtarbeitszeit eines Benutzers an
		// einem Projekt
	}

	// Methode um alle Benutzer zurückzugeben, die an einem bestimmten Projekt
	// gearbeitet haben
	public void getAllUsersWithWorktimeOnProject(Project project) {
		// TODO: Implementierung um alle Benutzer zu finden, die an einem Projekt
		// gearbeitet haben
		// - Aufgaben des Projekts über TaskManager abrufen
		// - Arbeitszeiten für jede Aufgabe über WorktimeManager durchsuchen
		// - Benutzer, die in diesen Arbeitszeiten enthalten sind, sammeln
	}

	// Methode zur Berechnung der Effizienz eines Benutzers auf einem Projekt
	public void getUserEfficiencyOnProject(User user, Project project) {
		// TODO: Effizienz eines Benutzers berechnen
		// - Verhältnis zwischen geplanter und tatsächlicher Arbeitszeit berechnen
		// - Geplante Arbeitszeiten aus Aufgaben abrufen (new variable plannedTime)
		// - Tatsächliche Arbeitszeiten aus Worktime abrufen
	}

	// Arbeitszeiten anhand der ID holen
	private Worktime getWorktimeById(int timeId) {
		for (Worktime time : times) {
			if (timeId == time.getId()) {
				return time;
			}
		}
		return null;
	}

	// Methode, um eine Arbeitszeit zu löschen
	public void removeWorktime(Worktime time) {
		// TODO Arbeitszeit DB löschen
		times.remove(time);
	}

	// Methode, um eine Arbeitszeit anhand der ID zu löschen
	public void removeWorktimeById(int timeId) {
		removeWorktime(getWorktimeById(timeId));
	}

	// Arbeitszeit-Einträge aktualisieren und in DB laden
	public void updateWorktime(Worktime selectedTime) {
		for (Worktime time : times) {
			if (time.equals(selectedTime)) {
				// TODO SQL-Query
				return;
			}
		}
	}

	// Startzeit der Bearbeitung nehmen, dafür neuen Eintrag erstellen mit User und
	// Task
	// macht direkt einen eintrag in DB
	public void logStartTime(User user, Task task) {
		// new Worktime macht Automatisch
		Worktime time = new Worktime(user, task);
		times.add(time);
		// TODO Startzeit eines Benutzers der Bearbeitung einer Aufgabe stoppen
	}

	// Arbeitszeit für Aufgabe und Benutzer aufzuzeichnen und in DB einfügen
	public void logWorktime() {
		// TODO logStartTime()
		// while(running)? wenn nochmal Event ausgelöst wird endTime gestoppt
		// TODO logEndTime()
	}

	// alle Arbeitszeiten einer Aufgabe
	public void getWorktimeByTask(Task task) {
		// TODO alle Arbeitszeiten einer Aufgabe
	}

	// alle Arbeitszeiten der Nutzer einer Aufgabe
	public void getUsersWorktimeByTask(Task task, ArrayList<User> users) {
		// TODO alle Arbeitszeiten einer Aufgabe
	}

	public void getWorktimeByUser(User user) {
		// TODO alle Arbeitszeiten eines Benutzers
	}

	public void getWorktimeByTaskAndUser(Task task, User user) {
		// TODO alle Arbeitszeiten für eine bestimmte Aufgabe und einen Benutzer
	}

	public void getWorktimeByTaskAndUsers(Task task, ArrayList<User> users) {
		// TODO alle Arbeitszeiten für eine bestimmte Aufgabe und alle Benutzer
	}

	// TODO addWorktime(Worktime worktime)
	// - Methode um eine neue Worktime-Instanz in die Datenbank einzufügen
	// - SQL-INSERT-Befehl vorbereiten, um die Worktime-Daten (Task, User,
	// Start-/Endzeit, Kommentar) in die Datenbank zu schreiben
	// - Worktime in die lokale ArrayList hinzufügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO removeWorktime(Worktime worktime)
	// - Methode um eine Worktime-Instanz aus der Datenbank zu löschen
	// - SQL-DELETE-Befehl vorbereiten, um Worktime anhand der ID zu löschen
	// - Worktime aus der lokalen ArrayList entfernen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO updateWorktime(Worktime worktime)
	// - Methode um Worktime-Daten in der Datenbank zu aktualisieren
	// - SQL-UPDATE-Befehl vorbereiten, um die geänderten Worktime-Daten in der
	// Datenbank zu speichern
	// - Lokale Worktime-Daten in der ArrayList aktualisieren
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getWorktimeById(int worktimeId)
	// - Methode um eine Worktime-Instanz anhand ihrer ID aus der Datenbank
	// abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um die Worktime-Daten zu laden
	// - Worktime in die lokale ArrayList einfügen, falls nicht bereits vorhanden
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getWorktimesByTask(Task task)
	// - Methode um alle Worktime-Einträge für eine bestimmte Aufgabe zu finden
	// - SQL-SELECT-Befehl vorbereiten, um Worktime-Daten basierend auf der Task-ID
	// zu laden
	// - Worktime-Instanzen in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getWorktimesByUser(User user)
	// - Methode um alle Worktime-Einträge für einen bestimmten Benutzer zu finden
	// - SQL-SELECT-Befehl vorbereiten, um Worktime-Daten basierend auf der User-ID
	// zu laden
	// - Worktime-Instanzen in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getAllWorktimes()
	// - Methode um alle Worktime-Einträge aus der Datenbank abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um eine Liste aller Worktime-Einträge zu
	// laden
	// - Worktime-Instanzen in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getUserEfficiencyOnProject(User user, Project project)
	// - Methode um die Effizienz eines Benutzers auf einem Projekt zu bewerten
	// - Verhältnis zwischen der geplanten und tatsächlichen Arbeitszeit berechnen
	// - Geplante Arbeitszeiten aus den Aufgaben des Projekts abrufen (neue Variable
	// `plannedTime`)
	// - Tatsächliche Arbeitszeiten aus den Worktime-Einträgen des Benutzers abrufen
	// - Effizienz berechnen und zurückgeben (tatsächliche Zeit / geplante Zeit)
	// - Exception Handling hinzufügen

	// TODO validateWorktimeData(Worktime worktime)
	// - Methode zur Validierung der Worktime-Daten vor dem Einfügen oder
	// Aktualisieren in der Datenbank
	// - Überprüfen, ob alle Pflichtfelder ausgefüllt sind (z.B. Startzeit, Task und
	// User)
	// - Falls Validierung fehlschlägt, eine Exception werfen oder eine
	// Fehlermeldung zurückgeben

	// TODO manageDatabaseTransaction()
	// - Methode für grundlegendes Transaktionsmanagement
	// - Sicherstellen, dass alle Änderungen in der Datenbank atomar ablaufen
	// (Transaktionen starten, committen, und ggf. zurückrollen)
	// - Transaktionen für die Methoden addWorktime, removeWorktime und
	// updateWorktime implementieren

	// TODO handleDatabaseExceptions()
	// - Methode für Exception Handling bei Datenbankoperationen
	// - SQLExceptions und andere mögliche Fehler abfangen
	// - Benutzer oder Entwickler über Fehler informieren (Logging oder
	// GUI-Benachrichtigung)

	// TODO calculateTotalWorktimeOnProject(Project project)
	// - Methode um die gesamte Arbeitszeit aller Benutzer an einem bestimmten
	// Projekt zu berechnen
	// - Worktime-Einträge basierend auf der Projekt-ID abrufen
	// - Arbeitszeiten summieren und die Gesamtdauer zurückgeben
	// - Exception Handling hinzufügen

// TODO getAllUsersWithWorktimeOnProject(Project project)
// - Methode, die alle Benutzer zurückgibt, die an einem bestimmten Projekt
// gearbeitet haben
// Dies beinhaltet:
// Alle Aufgaben für das Projekt aus dem TaskManager abrufen
// Die Arbeitszeiten für jede Aufgabe im WorktimeManager durchlaufen
// Eine Liste aller Benutzer erstellen, die zu diesen Arbeitszeiten gehören

// TODO getUserEfficiencyOnProject(User user, Project project)
// - Methode um die Effizienz eines Benutzers auf einem Projekt zu bewertet
// - Verhältnis zwischen der geplanten und tatsächlichen Arbeitszeit
// - Geplante Arbeitszeiten aus der Tasks abrufen (neue Variable plannedTime)
// - Tatsächliche Arbeitszeiten aus der WorktimeManager-Klasse abrufen

	// Getter und Setter
	public ArrayList<Worktime> getTimes() {
		return times; // Gibt Liste aller Arbeitszeiten, Anzahl Probleme und Comment
	}

	public void setTimes(ArrayList<Worktime> times) {
		this.times = times; // überschreibt aktuelle Liste
	}

	private void setWorktimeParameters(PreparedStatement stmt, List<String> columnNames, User user) {
		// TODO Auto-generated method stub

	}

}