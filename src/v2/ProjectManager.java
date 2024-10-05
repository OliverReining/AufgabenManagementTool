package v2;

import java.sql.*;
import java.util.*;

//Manager-Klasse für Projekte:
//Verwaltet Projekte über eine ArrayList<Project> und ermöglicht Interaktionen mit der Datenbank
//Diese Klasse ist verantwortlich für das Hinzufügen, Entfernen, Aktualisieren und Abrufen von Projektdaten
//sowie die Interaktion mit anderen Managern (z.B. TaskManager), um komplexe Abfragen zu bearbeiten

public class ProjectManager {

	// Lokale Variablen
	private ArrayList<Project> projects; // Liste aller Projekte, die in der Anwendung verwendet werden
	private DatabaseConnection dbConnect; // Verbindung zur Datenbank
	private LogManager log; // LogManager zum Protokollieren von Informationen und Fehlern

	// Konstruktoren
	public ProjectManager() {
		projects = new ArrayList<>(); // Initialisiert die Project-Liste ohne Datenbankverbindung
	}

	public ProjectManager(DatabaseConnection dbConnect) {
		this.dbConnect = dbConnect; // Setzt die Datenbankverbindung
		getProjectsFromDB(); // Lädt Projekte aus der Datenbank
	}

	public ProjectManager(DatabaseConnection dbConnect, LogManager log) {
		this.log = log; // Setzt LogManager
		this.dbConnect = dbConnect; // Setzt die Datenbankverbindung
		getProjectsFromDB(); // Lädt Projekte aus der Datenbank
	}

	// Methode zum Laden der Projekte aus der Datenbank
	// Liest alle Projekte aus der Tabelle "project" in der Datenbank und fügt sie
	// der lokalen Project-Liste hinzu
	private void getProjectsFromDB() {
		projects = new ArrayList<>();
		String table = "project";
		String sql = "SELECT * FROM " + table;
		log.log("Versuche '" + sql + "' auszuführen...", Log.LogType.INFO);

		try (ResultSet rs = dbConnect.getConnection().prepareStatement(sql).executeQuery()) {
			while (rs.next()) {
				// Erstellen eines neuen Project-Objekts basierend auf den DB-Daten
				Project project = new Project(rs.getInt("projectid"), rs.getString("title"),
						rs.getString("description"), rs.getString("projectlead"), rs.getBoolean("isCompleted"),
						rs.getDate("startDate").toLocalDate(), rs.getDate("dueDate").toLocalDate(),
						rs.getInt("taskCount"), rs.getInt("memberCount"));
				projects.add(project); // Projekt zur lokalen Liste hinzufügen
			}
			setProjects(projects); // Liste lokal speichern
		} catch (SQLException e) {
			log.sqlExceptionLog(e, sql); // Fehler protokollieren
		}
	}

	// Methode um ein Projekt anhand seiner ID zu finden
	public Project getProjectById(int projectId) {
		for (Project project : projects) {
			if (project.getProjectId() == projectId) {
				return project; // Projekt gefunden und zurückgegeben
			} else {
				log.log("Project-ID nicht gefunden", Log.LogType.ERROR);
			}
		}
		return null; // Kein Projekt gefunden
	}

	// Methode zum Hinzufügen eines neuen Projekts zur Liste
	public void addProject(Project project) {
		projects.add(project); // Projekt zur lokalen Liste hinzufügen
		// TODO: SQL-INSERT-Anweisung hier implementieren, um das Projekt in die
		// Datenbank einzufügen
	}

	// Methode zum Entfernen eines Projekts aus der Liste
	public void removeProject(Project project) {
		updateProject(project); // Sicherstellen, dass das Projekt vorher aktualisiert wird
		projects.remove(project); // Projekt aus der lokalen Liste entfernen
		// TODO: SQL-DELETE-Anweisung hier implementieren, um das Projekt aus der
		// Datenbank zu löschen
	}

	// Methode zum Entfernen eines Projekts anhand seiner ID
	public void removeProjectById(int projectId) {
		removeProject(getProjectById(projectId)); // Sucht das Projekt und entfernt es
	}

	// Methode zum Erstellen eines Kontakt-Strings für den Projektleiter
	public String createProjectLeadContact(User user) {
		return user.toString(); // Gibt die String-Repräsentation des Benutzers (Projektleiter) zurück
	}

	// TODO Methode um Projektdaten zu aktualisieren und in die Datenbank
	// hochzuladen
	public void updateProject(Project selectedProject) {
		for (Project project : projects) {
			if (project.equals(selectedProject)) {
				// TODO: SQL-UPDATE-Anweisung hier implementieren, um die Projektdaten in der
				// Datenbank zu aktualisieren
				return;
			}
		}
	}

	// TODO Methode um alle überfälligen Projekte (nicht abgeschlossen und
	// Fälligkeitsdatum überschritten) abzurufen
	public ArrayList<Project> getOverdueProjects() {
		ArrayList<Project> overdueProjects = new ArrayList<>();
		// TODO: SQL-SELECT-Anweisung hier implementieren, um überfällige Projekte zu
		// finden
		return overdueProjects;
	}

	// TODO Methode um den Fortschritt eines Projekts in Prozenten zurückzugeben
	public double getTaskCompletionPercentageForProject(int projectId) {
		// TODO: Aufgaben für das Projekt über den TaskManager abrufen
		// TODO: Berechnung des Prozentsatzes abgeschlossener Aufgaben (abgeschlossene /
		// Gesamtaufgaben * 100)
		return 0.0; // Platzhalter
	}

	// TODO Methode um die Gesamtkosten eines Projekts basierend auf dem Stundenlohn
	// zu berechnen
	public double calculateTotalProjectCost(Project project) {
		// TODO: Arbeitszeiten des Projektteams abrufen und mit deren Stundenlohn
		// multiplizieren
		return 0.0; // Platzhalter
	}

	// TODO addProject(Project project)
	// - Methode um ein neues Projekt in die Datenbank einzufügen
	// - SQL-INSERT-Befehl vorbereiten, um die Projektdaten in die Datenbank zu
	// schreiben
	// - Projekt in die lokale ArrayList hinzufügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO removeProject(Project project)
	// - Methode um ein Projekt aus der Datenbank zu löschen
	// - SQL-DELETE-Befehl vorbereiten, um das Projekt anhand seiner ID zu entfernen
	// - Projekt aus der lokalen ArrayList entfernen
	// - Exception Handling hinzufügen, falls das Löschen fehlschlägt

	// TODO updateProject(Project project)
	// - Methode um Projektdaten in der Datenbank zu aktualisieren
	// - SQL-UPDATE-Befehl vorbereiten, um die geänderten Projektdaten in der
	// Datenbank zu speichern
	// - Lokale Projektdaten in der ArrayList aktualisieren
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getProjectById(int projectId)
	// - Methode um ein Projekt anhand seiner ID aus der Datenbank abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um die Projektdaten zu laden
	// - Projekt in die lokale ArrayList einfügen, falls nicht bereits vorhanden
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getAllProjects()
	// - Methode um alle Projekte aus der Datenbank abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um eine Liste aller Projekte zu laden
	// - Projekte in die lokale ArrayList einfügen
	// - Exception Handling hinzufügen

	// TODO searchProjects(String attribute, String value)
	// - Methode um Projekte anhand eines bestimmten Attributs zu suchen (z.B.
	// Projektnamen oder Status)
	// - SQL-SELECT-Befehl mit WHERE-Klausel vorbereiten, um nach dem Attribut zu
	// suchen
	// - Projekte entsprechend der Suchkriterien aus der Datenbank laden und in die
	// ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO validateProjectData(Project project)
	// - Methode zur Validierung der Projektdaten vor dem Einfügen oder
	// Aktualisieren in der Datenbank
	// - Überprüfen, ob alle Pflichtfelder ausgefüllt sind (z.B. Projektname und
	// Startdatum)
	// - Falls Validierung fehlschlägt, eine Exception werfen oder eine
	// Fehlermeldung zurückgeben

	// TODO manageDatabaseTransaction()
	// - Methode für grundlegendes Transaktionsmanagement
	// - Sicherstellen, dass alle Änderungen in der Datenbank atomar ablaufen
	// (Transaktionen starten, committen, und ggf. zurückrollen)
	// - Transaktionen für die Methoden addProject, removeProject und updateProject
	// implementieren

	// TODO handleDatabaseExceptions()
	// - Methode für Exception Handling bei Datenbankoperationen
	// - SQLExceptions und andere mögliche Fehler abfangen
	// - Benutzer oder Entwickler über Fehler informieren (Logging oder
	// GUI-Benachrichtigung)

	// TODO getProjectsByUser(User user)
	// - Methode um alle Projekte zu finden, an denen ein bestimmter Benutzer
	// arbeitet
	// - SQL-SELECT-Befehl vorbereiten, um Projekte basierend auf der Benutzer-ID zu
	// laden
	// - Projekte, die mit dem Benutzer in Verbindung stehen, in die lokale
	// ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getCompletedProjects()
	// - Methode um alle abgeschlossenen Projekte abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um Projekte mit dem Status "abgeschlossen"
	// zu laden
	// - Abgeschlossene Projekte in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO getActiveProjects()
	// - Methode um alle aktiven Projekte abzurufen
	// - SQL-SELECT-Befehl vorbereiten, um Projekte mit dem Status "aktiv" zu laden
	// - Aktive Projekte in die lokale ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler hinzufügen

	// TODO Name, Vorname und Email des Projektleiters als String aus UserTabelle
	// holen

	// TODO getTaskCompletionPercentageForProject(int projectId)
	// Methode die den Fortschritt eines Projekts in Prozenten zurückgibt,
	// basierend auf abgeschlossenen Aufgaben.
	// Dies benötigt:
	// Alle Aufgaben für das Projekt über den TaskManager.
	// Prüfe, wie viele Aufgaben abgeschlossen sind.
	// Berechne den Prozentsatz (abgeschlossene Aufgaben / Gesamtaufgaben * 100).

	// TODO Gesamtkosten eines Projektes mit der hourlyRate

	// Getter und Setter für die ProjectList
	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	private void setProjectParameters(PreparedStatement stmt, List<String> columnNames, User user) {
		// TODO Auto-generated method stub

	}

}
