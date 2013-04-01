/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ui.librarian.*;

class LibrarianTab extends JPanel {
	private static final long serialVersionUID = -1645948987147921182L;
	
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private ActionListener newBookAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new NewBook().displayWindow();
		}
	};
	private ActionListener checkedOutAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new CheckedOut().displayWindow();
		}
	};
	private ActionListener popularAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new Popular().displayWindow();
		}
	};
	
	private static final int BUTTON_HEIGHT = MainMenu.BUTTON_HEIGHT;
	private static final int BUTTON_WIDTH = MainMenu.BUTTON_WIDTH;
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	LibrarianTab() {
		super(false);
		initializePane();
		addNewBookButton();
		addCheckedOutButton();
		addPopularButton();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		this.setLayout(gb);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Builds the new book button and adds it to the window 
	 */
	private void addNewBookButton() {
		// Place the new book button
		JButton button = new JButton("Add a Book");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 5);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.newBookAction);
	}

	/**
	 * Builds the checked out button and adds it to the window 
	 */
	private void addCheckedOutButton() {
		// Place the checked out button
		JButton button = new JButton("Checked Out Books");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 5, 5, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.checkedOutAction);
	}

	/**
	 * Builds the popular button and adds it to the window 
	 */
	private void addPopularButton() {
		// Place the popular button
		JButton button = new JButton("Popular Books");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.popularAction);
	}
}
