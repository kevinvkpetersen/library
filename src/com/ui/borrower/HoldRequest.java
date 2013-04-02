/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.ui.borrower;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import com.borrower.Borrower;
import com.borrower.BorrowerType;
import com.date.DateParser;

/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Alec O'Connor
 */
public class HoldRequest {
	
	private Connection con;
	private JFrame frame = new JFrame("New Borrower");
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private static final int FIELD_WIDTH = 30;
	private final int LABEL_ALIGNMENT = GridBagConstraints.LINE_START;
	
	private JTextField titleField = new JTextField(FIELD_WIDTH);
	private JTextField callNumberField = new JPasswordField(FIELD_WIDTH);
	
	private ActionListener submitAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

				
				String title = titleField.getText(); 
				int callNumber = Integer.parseInt(callNumberField.getText());
				
				Statement stmt = null;
				try {
					stmt = con.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					stmt.executeQuery("UPDATE BookCopy SET status= onHold WHERE callNumber= " + callNumber + "");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				
				System.out.print("Hold Requested");
				titleField.setText("");
				callNumberField.setText("");
	}
	};
	
	private ActionListener cancelAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			frame.dispose();	
		}
	};
	private final JButton btnSubmitHold = new JButton("Submit Hold");
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	public HoldRequest() {
		initializePane();
		addBid();
		addPassword();
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
		JLabel lblBookTitle = new JLabel("Book Title:");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(lblBookTitle, c);
		GridBagConstraints gbc_lblBookTitle = new GridBagConstraints();
		gbc_lblBookTitle.insets = new Insets(0, 0, 0, 5);
		gbc_lblBookTitle.gridx = 0;
		gbc_lblBookTitle.gridy = 0;
		contentPane.add(lblBookTitle, gbc_lblBookTitle);

		// Place the text field for the bid
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(titleField, c);
		GridBagConstraints gbc_titleField = new GridBagConstraints();
		gbc_titleField.insets = new Insets(0, 0, 0, 5);
		gbc_titleField.gridx = 1;
		gbc_titleField.gridy = 0;
		contentPane.add(titleField, gbc_titleField);
	}
	
	/**
	 * Builds the password field and label and adds them to the window 
	 */
	private void addPassword() {
		// Place password label
		JLabel lblBookCallNumber = new JLabel("Book Call Number: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(lblBookCallNumber, c);
		GridBagConstraints gbc_lblBookCallNumber = new GridBagConstraints();
		gbc_lblBookCallNumber.insets = new Insets(0, 0, 0, 5);
		gbc_lblBookCallNumber.gridx = 2;
		gbc_lblBookCallNumber.gridy = 0;
		contentPane.add(lblBookCallNumber, gbc_lblBookCallNumber);

		// Place the password field
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(callNumberField, c);
		GridBagConstraints gbc_callNumberField = new GridBagConstraints();
		gbc_callNumberField.insets = new Insets(0, 0, 0, 5);
		gbc_callNumberField.gridx = 3;
		gbc_callNumberField.gridy = 0;
		contentPane.add(callNumberField, gbc_callNumberField);
		
		GridBagConstraints gbc_btnSubmitHold = new GridBagConstraints();
		gbc_btnSubmitHold.gridx = 4;
		gbc_btnSubmitHold.gridy = 0;
		btnSubmitHold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String title = titleField.getText(); 
				int callNumber = Integer.parseInt(callNumberField.getText());
				
				Statement stmt = null;
				try {
					stmt = con.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					stmt.executeQuery("UPDATE BookCopy SET status= onHold WHERE callNumber= " + callNumber + "");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				
				System.out.print("Hold Requested");
				titleField.setText("");
				callNumberField.setText("");
	};
		});
		contentPane.add(btnSubmitHold, gbc_btnSubmitHold);
	}

	
	/**
	 * Builds the submit button and adds it to the window 
	 */
	private void addSubmitButton() {
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.LINE_START;
	}
	
	/**
	 * Builds the cancel button and adds it to the window 
	 */
	private void addCancelButton() {
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 5, 10, 10);
		c.anchor = GridBagConstraints.LINE_END;
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
	}
	}