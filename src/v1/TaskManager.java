package v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskManager {

	// Aufgabe erstellen
	public void createTask(String title, String description, int projectId) {
		String sql = "INSERT INTO task (title, description, projectid) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setInt(3, projectId); // projectid ist foreign key
			stmt.executeUpdate();
			System.out.println("Aufgabe erstellt!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Aufgaben anzeigen
	public String showTasks() {
		StringBuilder result = new StringBuilder();
		String sql = "SELECT * FROM task";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				result.append("ID: ").append(rs.getInt("taskid")).append(", Titel: ").append(rs.getString("title"))
						.append(", Beschreibung: ").append(rs.getString("description")).append(", Projekt-ID: ")
						.append(rs.getInt("projectid")).append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	// Benutzer zur Aufgabe hinzufügen (Mehrere Benutzer pro Aufgabe)
	public void addUserToTask(int taskId, int userId) {
		String sql = "INSERT INTO task_user (taskid, userid) VALUES (?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, taskId);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
			System.out.println("Benutzer zur Aufgabe hinzugefügt!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Benutzer von der Aufgabe entfernen
	public void removeUserFromTask(int taskId, int userId) {
		String sql = "DELETE FROM task_user WHERE taskid = ? AND userid = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, taskId);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
			System.out.println("Benutzer von der Aufgabe entfernt!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Aufgabe löschen
	public void deleteTask(int taskId) {
		String sql = "DELETE FROM task WHERE taskid = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, taskId);
			stmt.executeUpdate();
			System.out.println("Aufgabe gelöscht!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Aufgabe aktualisieren
	public void updateTask(int taskId, String newTitle, String newDescription, int newProjectId) {
	    String sql = "UPDATE task SET title = ?, description = ?, projectid = ? WHERE taskid = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, newTitle);
	        stmt.setString(2, newDescription);
	        stmt.setInt(3, newProjectId);
	        stmt.setInt(4, taskId);
	        stmt.executeUpdate();
	        System.out.println("Aufgabe aktualisiert!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


}
