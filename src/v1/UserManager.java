package v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class UserManager {

	// Benutzer erstellen
	public void createUser(String name, String vorname, String email) {
		String sql = "INSERT INTO benutzer (name, vorname, email) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnectionOld.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, vorname);
			stmt.setString(3, email);
			stmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Benutzer erstellt!");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Benutzer konnte nicht erstellt werden.. " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Benutzer anzeigen
	public String showUsers() {
		StringBuilder result = new StringBuilder();
		String sql = "SELECT * FROM benutzer";
		try (Connection conn = DatabaseConnectionOld.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				result.append("ID: ").append(rs.getInt("userid")).append(", Name: ").append(rs.getString("name"))
						.append(", Vorname: ").append(rs.getString("vorname")).append(", Email: ")
						.append(rs.getString("email")).append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	// Benutzer aktualisieren
	public void updateUser(int userId, String newName, String newVorname, String newEmail, String password, String role) {

		// Update-Query
		String sql = "UPDATE benutzer SET name = ?, vorname = ?, email = ?, pass = ?, role = ? WHERE userid = ?";

		try (Connection conn = DatabaseConnectionOld.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, newName);
			stmt.setString(2, newVorname);
			stmt.setString(3, newEmail);
			stmt.setString(4, password);
			stmt.setString(5, role);
			stmt.setInt(6, userId);

			int rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated > 0) {
				JOptionPane.showMessageDialog(null, "Benutzer erfolgreich aktualisiert!", "Erfolg",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fehler beim Aktualisieren des Benutzers.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Benutzer löschen
	public void deleteUser(int userId) {
		String sql = "DELETE FROM benutzer WHERE userid = ?";
		try (Connection conn = DatabaseConnectionOld.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Benutzer gelöscht!");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Benutzer konnte nicht gelöscht werden.. " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

