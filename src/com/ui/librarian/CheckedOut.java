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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.book.records.Borrowing;
import com.borrower.Borrower;
import com.borrower.BorrowerType;
import com.date.DateParser;

/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Kevin Petersen
 */
public class CheckedOut {
	private JFrame frame = new JFrame("Books Checked Out");
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private final int LABEL_ALIGNMENT = GridBagConstraints.LINE_START;
	
	
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
		addList();
		addCancelButton();
	}
	
	private void addList() {
		try {
			List<Borrowing> borrowedBooks = Borrowing.getAll();
			List<Borrowing> booksOut = new ArrayList<Borrowing>();
			List<Borrowing> booksOverdue = new ArrayList<Borrowing>();
			
			Date now = new Date(System.currentTimeMillis());
			
			for (Borrowing b : borrowedBooks) {
				if (b.getInDate().after(now)) {
					booksOverdue.add(b);
				} else {
					booksOut.add(b);
				}
			}
			for (Borrowing b : booksOverdue) {
				makeLabel("!!!!!!!!Overdue book is: " + b.getCallNumber() + " It was checked out on: " + b.getOutDate() + " It is due on: " + b.getInDate() + "!!!!!!!!");
				}
			for (Borrowing b : booksOut) {
			makeLabel("Book is: " + b.getCallNumber() + " It was checked out on: " + b.getOutDate() + " It is due on: " + b.getInDate());
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	private void makeLabel(String contents) {
		// Place the bid label
		JLabel label = new JLabel(contents);
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);
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

	}
}