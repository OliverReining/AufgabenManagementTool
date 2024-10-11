package v2;

import java.util.*;

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel {

	private final String[] columnNames = { "User-ID", "Name", "Vorname", "E-Mail","Tel.Nr", "Passwort", "Rolle", "Projektleiter", "Stundenlohn" };
	private final ArrayList<User> users;

	public UserTableModel(ArrayList<User> users) {
		this.users = users;
	}

	@Override
	public int getRowCount() {
		return users.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User user = users.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return user.getUserId().strip();
		case 1:
			return user.getName().strip();
		case 2:
			return user.getVorname().strip();
		case 3:
			return user.getEmail().strip();
		case 4:
			return user.getTel().strip();
		case 5:
			return user.getPass().strip();
		case 6:
			return user.toRoleString(user.getRole());
		case 7:
			return user.isProjectLead() ? "Yes" : "No";
		case 8:
			return user.getHourlyRate();
		default:
			return null;
		}
	}

}
