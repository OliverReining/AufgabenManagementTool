package v1;

import java.awt.GridLayout;
import javax.swing.*;

@SuppressWarnings("serial")
public class AdminModeFrame extends JFrame {
	
	private int userId;

	public AdminModeFrame(int userId) {
		this.setUserId(userId);
        setTitle("Admin Options");
        setSize(300, 200);
		setLocationRelativeTo(null);
		setResizable(false);

        
        // Panel für die Buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        
        // Button für den Admin Mode
        JButton adminModeButton = new JButton("Admin Login");
        adminModeButton.addActionListener(e -> {
        	dispose();
        	new AdminGUI().setVisible(true);
        });
        panel.add(adminModeButton);
        
        // Button für den User Mode
        JButton userModeButton = new JButton("User Login");
        userModeButton.addActionListener(e -> {
        	dispose();
        	new UserGUI(userId).setVisible(true);
        });
        	
        panel.add(userModeButton);
        
        add(panel);
		
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
