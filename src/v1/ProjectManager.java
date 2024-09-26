package v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
		String sql = "SELECT p.projectid, p.title, p.description, "
				+ "CONCAT(benutzer.vorname, ' ', benutzer.name, ' ID: ', benutzer.userid) AS Projektleiter "
				+ "FROM project p JOIN benutzer ON p.projectlead = benutzer.userid;";
		
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				result.append("ID: ").append(rs.getInt("projectid")).append(", Titel: ").append(rs.getString("title"))
						.append(", Beschreibung: ").append(rs.getString("description")).append(", Projektleiter: ")
						.append(rs.getString("Projektleiter")).append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	// Projekte als Tabelle mit Projektleiter ID und Name
	public TableModel showProjectsAsTable() {
		String[] columnNames = { "ID", "Titel", "Beschreibung", "Projektleiter" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);

		String sql = "SELECT p.projectid, p.title, p.description, "
				+ "CONCAT(benutzer.vorname, ' ', benutzer.name, ' ID: ', benutzer.userid) AS Projektleiter "
				+ "FROM project p JOIN benutzer ON p.projectlead = benutzer.userid;";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Object[] row = { rs.getInt("projectid"), rs.getString("title"), rs.getString("description"),
						rs.getString("Projektleiter") };
				model.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;
	}

	// Projekt aktualisieren
	public void updateProject(int projectId, String newTitle, String newDescription, int newProjectLeadId) {
		String sql = "UPDATE project SET title = ?, description = ?, projectlead = ? WHERE projectid = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, newTitle);
			stmt.setString(2, newDescription);
			stmt.setInt(3, newProjectLeadId); // Neuer Projektleiter (foreign key)
			stmt.setInt(4, projectId);
			stmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Projekt aktualisiert!", "Erfolgreich",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Projekt konnte nicht aktualisiert werden.. " + e.getMessage(),
					"Fehler", JOptionPane.ERROR_MESSAGE);
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
