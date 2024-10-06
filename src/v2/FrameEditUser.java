package v2;

import java.awt.GridBagLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public class FrameEditUser extends JFrame{
	
	private User user;
	private UserManager uMan;
	private LogManager log;
	
	public FrameEditUser(User currentUser, UserManager uMan, LogManager log) {
		user = currentUser;
		this.uMan = uMan;
		this.log = log;
		setTitle("");
		setSize(500,300);
		setLayout(new GridBagLayout());
		
	}
	
}
