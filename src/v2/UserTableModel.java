package v2;

import java.util.*;

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel {

	private final String[] columnNames = { "User-ID", "Name", "Vorname", "E-Mail","Tel.Nr", "Passwort", "Rolle", "Projektleiter" };
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
			return user.getUserId();
		case 1:
			return user.getName();
		case 2:
			return user.getVorname();
		case 3:
			return user.getEmail();
		case 4:
			return user.getTel();
		case 5:
			return user.getPass();
		case 6:
			return user.getRole();
		case 7:
			return user.isProjectLead() ? "Yes" : "No";
		case 8:
			return user.getHourlyRate();
		default:
			return null;
		}
	}

}
