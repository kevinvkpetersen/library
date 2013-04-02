/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.ui.clerk;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.book.Book;
import com.book.BookCopy;

/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Kevin Petersen
 */
public class Return {
	private JFrame frame = new JFrame("Return");
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private static final int FIELD_WIDTH = 30;
	private final int LABEL_ALIGNMENT = GridBagConstraints.LINE_START;
	
	private JTextField callNumberField = new JTextField(FIELD_WIDTH);
	private JTextField copyNoField = new JTextField(FIELD_WIDTH);
	
	private ActionListener returnAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int callNumber = Integer.parseInt(callNumberField.getText());
			int copyNo = Integer.parseInt(copyNoField.getText());
			
			try {
				BookCopy.get(Book.get(callNumber), copyNo).doReturn();
				callNumberField.setText("");
				copyNoField.setText("");
			} catch (SQLException sql) {
				System.out.println("Error returning book.");
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
	public Return() {
		initializePane();
		addCallNumber();
		addCopyNo();
		addReturnButton();
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
	 * Builds the call number field and label and adds them to the window 
	 */
	private void addCallNumber() {
		// Place the call number label
		JLabel label = new JLabel("Enter Call Number*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the call number
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(this.callNumberField, c);
		contentPane.add(this.callNumberField);
	}
	
	/**
	 * Builds the copy number field and label and adds them to the window 
	 */
	private void addCopyNo() {
		// Place the copy number label
		JLabel label = new JLabel("Enter Copy Number*: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the copy number
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(this.copyNoField, c);
		contentPane.add(this.copyNoField);
	}
	
	/**
	 * Builds the return button and adds it to the window 
	 */
	private void addReturnButton() {
		// Place the return button
		JButton button = new JButton("Return");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.LINE_START;
		gb.setConstraints(button, c);
		contentPane.add(button);
		button.addActionListener(this.returnAction);
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
		this.callNumberField.requestFocus();
	}
}