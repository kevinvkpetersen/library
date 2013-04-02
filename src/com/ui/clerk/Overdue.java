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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.book.records.Borrowing;

/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Kevin Petersen
 */
public class Overdue {
	private JFrame frame = new JFrame("Overdue Books");
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private ActionListener closeAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			frame.dispose();	
		}
	};
	
	/**
	 * Builds the components and does any initialization for the window
	 * @throws SQLException 
	 */
	public Overdue() throws SQLException {
		initializePane();
		addLabels();
		addList();
		addCloseButton();
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
		c.fill = GridBagConstraints.HORIZONTAL;
	}

	/**
	 * Builds the column labels and adds them to the window 
	 */
	private void addLabels() {
		// Place call number label
		JLabel callNumber = new JLabel("Call Number");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(callNumber, c);
		contentPane.add(callNumber);
	/*	
		// Place copy number label
		JLabel copyNo = new JLabel("Copy Number");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 5, 5, 5);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(copyNo, c);
		contentPane.add(copyNo);
	*/	
		// Place borrower id label
		JLabel bid = new JLabel("Borrower ID");
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 5, 5, 10);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(bid, c);
		contentPane.add(bid);
	}
	
	/**
	 * Gets the list data and iteratively adds rows of data to the window
	 * @throws SQLException 
	 */
	private void addList() throws SQLException  {
		List<Borrowing> records = Borrowing.getOverdue();
		
		for(Borrowing borid : records) {
			addRow(borid);
		}
	}

	/**
	 * Builds the builds a row of data and adds it to the window
	 * 
	 * @param borid
	 *            The source of the data
	 */
	private void addRow(Borrowing borid) {
		int callNumber = borid.getCallNumber().getCallNumber().getCallNumber();
		int copyNo = borid.getCallNumber().getCopyNo();
		int bid = borid.getBid().getBid();
		
		// Place call number label
		JLabel call = new JLabel("" + callNumber);
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 5);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(call, c);
		contentPane.add(call);
/*
		// Place copy number label
		JLabel copy = new JLabel("" + copyNo);
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 5, 5, 5);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(copy, c);
		contentPane.add(copy);
*/
		// Place borrower id label
		JLabel borrower = new JLabel("" + bid);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 5, 5, 10);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(borrower, c);
		contentPane.add(borrower);
	}
	
	/**
	 * Builds the cancel button and adds it to the window 
	 */
	private void addCloseButton() {
		// Place the cancel button
		JButton button = new JButton("Close");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.LINE_END;
		gb.setConstraints(button, c);
		contentPane.add(button);
		button.addActionListener(this.closeAction);
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