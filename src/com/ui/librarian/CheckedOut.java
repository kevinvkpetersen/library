/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.ui.librarian;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.date.DateParser;
import com.user.Borrower;
import com.user.BorrowerType;

/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Kevin Petersen
 */
public class CheckedOut {
	private JFrame frame = new JFrame("New Borrower");
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private static final int FIELD_WIDTH = 30;
	private final int LABEL_ALIGNMENT = GridBagConstraints.LINE_START;
	
	private JTextField bidField = new JTextField(FIELD_WIDTH);
	private JPasswordField passwordField = new JPasswordField(FIELD_WIDTH);
	private JTextField nameField = new JTextField(FIELD_WIDTH);
	private JTextField addressField = new JTextField(FIELD_WIDTH);
	private JTextField phoneField = new JTextField(FIELD_WIDTH);
	private JTextField emailField = new JTextField(FIELD_WIDTH);
	private JTextField sinField = new JTextField(FIELD_WIDTH);
	private JTextField expiryField = new JTextField(FIELD_WIDTH);
	private JTextField typeField = new JTextField(FIELD_WIDTH);
	
	private ActionListener submitAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				int bid = Integer.parseInt(bidField.getText());
				String password = String.valueOf(passwordField.getPassword()); 
				String name = nameField.getText();
				
				String address = addressField.getText();
				address = (address.isEmpty() ? null : address);
				
				String phoneString = phoneField.getText();
				int phone = (phoneString.isEmpty() ? 0 : Integer.parseInt(phoneString));
				
				String emailAddress = emailField.getText();
				emailAddress = (emailAddress.isEmpty() ? null : emailAddress);
				
				int sinOrStNo = Integer.parseInt(sinField.getText());
				
				String dateString = expiryField.getText();
				Date expiryDate = (dateString.isEmpty() ? null : DateParser.parseString(dateString)); 
				
				BorrowerType type = BorrowerType.getBorrowerType(typeField.getText());
				
				Borrower.addBorrower(bid, password, name, address, phone,
						emailAddress, sinOrStNo, expiryDate, type);
				
				System.out.print("Borrower added!");
				bidField.setText("");
				passwordField.setText("");
				nameField.setText("");
				addressField.setText("");
				phoneField.setText("");
				emailField.setText("");
				sinField.setText("");
				expiryField.setText("");
				typeField.setText("");
			} catch (SQLException sql) {
				System.out.print("Could not add Borrower.");
			}
		}
	};
	private ActionListener cancelAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			frame.dispose();	
		}
	};
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	public CheckedOut() {
		initializePane();
		addBid();
		addPassword();
		addName();
		addAddress();
		addPhone();
		addEmail();
		addSin();
		addExpiry();
		addType();
		addSubmitButton();
		addCancelButton();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		frame.setContentPane(contentPane);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		
		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Builds the bid field and label and adds them to the window 
	 */
	private void addBid() {
		// Place the bid label
		JLabel label = new JLabel("Enter Borrower ID*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the bid
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(bidField, c);
		contentPane.add(bidField);
	}
	
	/**
	 * Builds the password field and label and adds them to the window 
	 */
	private void addPassword() {
		// Place password label
		JLabel label = new JLabel("Enter Password*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the password field
		passwordField.setEchoChar('*');
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(passwordField, c);
		contentPane.add(passwordField);
	}

	/**
	 * Builds the name field and label and adds them to the window 
	 */
	private void addName() {
		// Place the name label
		JLabel label = new JLabel("Enter Full Name*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the name
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(nameField, c);
		contentPane.add(nameField);
	}
	
	/**
	 * Builds the address field and label and adds them to the window 
	 */
	private void addAddress() {
		// Place the address label
		JLabel label = new JLabel("Enter Full Address: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the address
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(addressField, c);
		contentPane.add(addressField);
	}
	
	/**
	 * Builds the phone field and label and adds them to the window 
	 */
	private void addPhone() {
		// Place the phone label
		JLabel label = new JLabel("Enter Phone Number: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the phone
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(phoneField, c);
		contentPane.add(phoneField);
	}
	
	/**
	 * Builds the email field and label and adds them to the window 
	 */
	private void addEmail() {
		// Place the email label
		JLabel label = new JLabel("Enter Email Address: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the email
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(emailField, c);
		contentPane.add(emailField);
	}
	
	/**
	 * Builds the sin field and label and adds them to the window 
	 */
	private void addSin() {
		// Place the sin label
		JLabel label = new JLabel("Enter Student Number or SIN*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the sin
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(sinField, c);
		contentPane.add(sinField);
	}
	
	/**
	 * Builds the expiry field and label and adds them to the window 
	 */
	private void addExpiry() {
		// Place the expiry label
		JLabel label = new JLabel("Enter Expiry Date (YYYY-MM-DD): ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the expiry
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(expiryField, c);
		contentPane.add(expiryField);
	}
	
	/**
	 * Builds the type field and label and adds them to the window 
	 */
	private void addType() {
		// Place the type label
		JLabel label = new JLabel("Enter Borrower Type*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the type
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(typeField, c);
		contentPane.add(typeField);
	}
	
	/**
	 * Builds the submit button and adds it to the window 
	 */
	private void addSubmitButton() {
		// Place the submit button
		JButton button = new JButton("Submit");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.LINE_START;
		gb.setConstraints(button, c);
		contentPane.add(button);
		button.addActionListener(this.submitAction);
	}
	
	/**
	 * Builds the cancel button and adds it to the window 
	 */
	private void addCancelButton() {
		// Place the cancel button
		JButton button = new JButton("Cancel");
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 5, 10, 10);
		c.anchor = GridBagConstraints.LINE_END;
		gb.setConstraints(button, c);
		contentPane.add(button);
		button.addActionListener(this.cancelAction);
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
		bidField.requestFocus();
	}
}