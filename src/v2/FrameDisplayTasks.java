package v2;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class FrameDisplayTasks extends JFrame {

	private TaskManager manager = new TaskManager();

	public FrameDisplayTasks() {
		ArrayList<Task> tasks = manager.getTasks();
		setTitle("Aufgabenübersicht");
		setSize(650, 400);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		TaskTableModel model = new TaskTableModel(tasks);
		JTable table = new JTable(model);
		JScrollPane scrolltable = new JScrollPane(table);

		panel.add(scrolltable, BorderLayout.CENTER);
		
		add(panel);

	}

}
