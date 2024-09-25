package v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Analytics {
	

	// Benutzerdaten anzeigen
	public static Object[] showUserData(int userId) {
		Object[] userData = new Object[6];
		String sql = "SELECT * FROM benutzer WHERE userid = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId); // Setze die UserID als Parameter
			try (ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {
					// Die abgerufenen Werte in das Array einfügen
					userData[0] = rs.getInt("userid");
					userData[1] = rs.getString("name");
					userData[2] = rs.getString("vorname");
					userData[3] = rs.getString("email");
					userData[4] = rs.getString("pass");
					userData[5] = rs.getString("role");
				} else {
					// Falls der Benutzer nicht existiert
					System.out.println("Benutzer nicht gefunden.");
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userData;
	}

	public static void showTaskData(int userId) {
		Object[] taskData = new Object[5];
		
		//TODO SQL Statement
		String sql = "WHERE userid = ?"; 
		
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId); // Setze die UserID als Parameter
			try (ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {
					
				} else {
					// Falls der Benutzer nicht existiert
					System.out.println("Benutzer nicht gefunden.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
