/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class BorrowerTab extends JPanel {
	private static final long serialVersionUID = -2359671998669762596L;
	
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private JTextField usernameField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(10);
	

	/**
	 * Builds the components and does any initialization for the window
	 */
	BorrowerTab() {
		super(false);
		initializePane();
		addUserName();
		addPassword();
		addButton();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		this.setLayout(gb);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
	
	/**
	 * Builds the username field and label and adds them to the window 
	 */
	private void addUserName() {
		// Place the username label
		JLabel usernameLabel = new JLabel("Enter Borrower name: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		this.add(usernameLabel);

		// Place the text field for the username
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(usernameField, c);
		this.add(usernameField);
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
		this.add(passwordLabel);

		// Place the password field
		passwordField.setEchoChar('*');
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		this.add(passwordField);
	}

	/**
	 * Builds the login button and adds it to the window 
	 */
	private void addButton() {
		// Place the login button
		JButton loginButton = new JButton("Log In");
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(loginButton, c);
		this.add(loginButton);
	}
}
