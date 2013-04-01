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

import com.ui.borrower.*;

class BorrowerTab extends JPanel {
	private static final long serialVersionUID = -6644386017207453675L;
	
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private ActionListener searchAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new Search().displayWindow();
		}
	};
	private ActionListener accountAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new Account().displayWindow();
		}
	};
	private ActionListener holdRequestAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new HoldRequest().displayWindow();
		}
	};
	private ActionListener payFineAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new PayFine().displayWindow();
		}
	};
	
	private static final int BUTTON_HEIGHT = MainMenu.BUTTON_HEIGHT;
	private static final int BUTTON_WIDTH = MainMenu.BUTTON_WIDTH;
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	BorrowerTab() {
		super(false);
		initializePane();
		addSearchButton();
		addAccountButton();
		addHoldRequestButton();
		addPayFineButton();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		this.setLayout(gb);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Builds the search button and adds it to the window 
	 */
	private void addSearchButton() {
		// Place the search button
		JButton button = new JButton("Search");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 5);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.searchAction);
	}

	/**
	 * Builds the account button and adds it to the window 
	 */
	private void addAccountButton() {
		// Place the account button
		JButton button = new JButton("Account Status");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 5, 5, 10);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.accountAction);
	}

	/**
	 * Builds the hold request button and adds it to the window 
	 */
	private void addHoldRequestButton() {
		// Place the hold request button
		JButton button = new JButton("Request Hold");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 5);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.holdRequestAction);
	}

	/**
	 * Builds the pay fine button and adds it to the window 
	 */
	private void addPayFineButton() {
		// Place the pay fine button
		JButton button = new JButton("Pay Fines");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 5, 10, 10);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.payFineAction);
	}
}
