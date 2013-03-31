/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Main;

/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Kevin Petersen
 */
public class LoginWindow implements ActionListener {
	private JFrame frame = new JFrame();
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private JTextField usernameField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(10);
	
	private int loginAttempts = 3;
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	public LoginWindow() {
		initializePane();
		addUserName();
		addPassword();
		addButtons();
		loadDriver();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		frame.setContentPane(contentPane);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Builds the username field and label and adds them to the window 
	 */
	private void addUserName() {
		// Place the username label
		JLabel usernameLabel = new JLabel("Enter username: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		contentPane.add(usernameLabel);

		// Place the text field for the username
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(usernameField, c);
		contentPane.add(usernameField);
	}
	
	/**
	 * Builds the password field and label and adds them to the window 
	 */
	private void addPassword() {
		// Place password label
		JLabel passwordLabel = new JLabel("Enter password: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(passwordLabel, c);
		contentPane.add(passwordLabel);

		// Place the password field
		passwordField.setEchoChar('*');
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		contentPane.add(passwordField);
		passwordField.addActionListener(this);
	}

	/**
	 * Builds the login button and adds it to the window 
	 */
	private void addButtons() {
		// Place the login button
		JButton loginButton = new JButton("Log In");
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(loginButton, c);
		contentPane.add(loginButton);
		loginButton.addActionListener(this);

	}
	
	/**
	 * Loads the JDBC driver from Oracle
	 */
	private void loadDriver() {
		try {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * Packages, places and makes the window visible
	 */
	public void displayWindow() {
		// Size the window to obtain a best fit for the components
		frame.pack();
		
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation(	(d.width - r.width) / 2,
								(d.height - r.height) / 2);

		// make the window visible
		frame.setVisible(true);

		// place the cursor in the text field for the username
		usernameField.requestFocus();			
	}
	
	/**
	 * Invoked when the Login button is clicked, or enter is pressed in the
	 * password field
	 */
	public void actionPerformed(ActionEvent e) {
		String username = usernameField.getText();
		String password = String.valueOf(passwordField.getPassword());
		
		if(connect(username, password)) {
			// If username/password are correct, remove this and open main menu
			frame.dispose();
			new MainMenu().showMenu();
		} else if (loginAttempts-- > 0) {
			// Clear the password field
			passwordField.setText("");
		} else {
			frame.dispose();
			System.exit(-1);
		}
	}
	
	/**
	 * Attempts to connect to the Oracle "ug" database
	 * 
	 * @param username
	 *            Username to sign in with. Ex. ora_x1y2
	 * @param password
	 *            Password for the username Ex. a12345678
	 * @return True id connection is successful
	 */
	private boolean connect(String username, String password) {
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
		
		try {
			Main.con = DriverManager.getConnection(connectURL, username, password);
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			return false;
		}
	}
}