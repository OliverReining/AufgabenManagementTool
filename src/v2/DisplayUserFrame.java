package v2;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class DisplayUserFrame extends JFrame {
	
	private UserManager manager = new UserManager();
	
	public DisplayUserFrame() {
		ArrayList<User> users = manager.getUsers();
		setTitle("Nutzerübersicht");
		setSize(650,400);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		UserTableModel model = new UserTableModel(users);
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		
		panel.add(scrollPane);
		add(panel);
	}

}
