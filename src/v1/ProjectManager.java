package v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ProjectManager {

	// Projekt erstellen
	public void createProject(String title, String description, int projectLeadId) {
		String sql = "INSERT INTO project (title, description, projectlead) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setInt(3, projectLeadId); // projectlead ist der foreign key von userid
			stmt.executeUpdate();
	        JOptionPane.showMessageDialog(null, "Projekt erstellt!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Projekt konnte nicht erstellt werden.. " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Projekte anzeigen
	public String showProjects() {
		StringBuilder result = new StringBuilder();
		String sql = "SELECT * FROM project";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				result.append("ID: ").append(rs.getInt("projectid")).append(", Titel: ").append(rs.getString("title"))
						.append(", Beschreibung: ").append(rs.getString("description")).append(", Projektleiter-ID: ")
						.append(rs.getInt("projectlead")).append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	// Projekt aktualisieren
	public void updateProject(int projectId, String newTitle, String newDescription, int newProjectLeadId) {
	    String sql = "UPDATE project SET title = ?, description = ?, projectlead = ? WHERE projectid = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, newTitle);
	        stmt.setString(2, newDescription);
	        stmt.setInt(3, newProjectLeadId);  // Neuer Projektleiter (foreign key)
	        stmt.setInt(4, projectId);
	        stmt.executeUpdate();
	        JOptionPane.showMessageDialog(null, "Projekt aktualisiert!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
	    } catch (SQLException e) {
	        e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Projekt konnte nicht aktualisiert werden.. " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
	    }
	}


	// Projekt löschen
	public void deleteProject(int projectId) {
	    String sql = "DELETE FROM project WHERE projectid = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, projectId);
	        stmt.executeUpdate();
	        JOptionPane.showMessageDialog(null, "Projekt gelöscht!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
	    } catch (SQLException e) {
	        e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Projekt konnte nicht gelöscht werden.. " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
	    }
	}

}
