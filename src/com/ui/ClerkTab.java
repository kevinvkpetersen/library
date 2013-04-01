package com.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import java.awt.event.*;
import com.ui.clerk.*;

class ClerkTab extends JPanel {
	private static final long serialVersionUID = -2359671998669762596L;
	
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private ActionListener checkoutAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
		}
	};
	private ActionListener returnAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
		}
	};
	private ActionListener newBorrowerAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new NewBorrower().displayWindow();
		}
	};
	private ActionListener overdueAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
		}
	};
	
	private static final int BUTTON_HEIGHT = MainMenu.BUTTON_HEIGHT;
	private static final int BUTTON_WIDTH = MainMenu.BUTTON_WIDTH;
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	ClerkTab() {
		super(false);
		initializePane();
		addCheckoutButton();
		addReturnButton();
		addNewBorrowerButton();
		addOverdueButton();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		this.setLayout(gb);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Builds the checkout button and adds it to the window 
	 */
	private void addCheckoutButton() {
		// Place the checkout button
		JButton button = new JButton("Check out");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 5);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.checkoutAction);
	}

	/**
	 * Builds the return button and adds it to the window 
	 */
	private void addReturnButton() {
		// Place the return button
		JButton button = new JButton("Return");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 5, 5, 10);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.returnAction);
	}

	/**
	 * Builds the new borrower button and adds it to the window 
	 */
	private void addNewBorrowerButton() {
		// Place the new borrower button
		JButton button = new JButton("Add Borrower");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 5);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.newBorrowerAction);
	}

	/**
	 * Builds the overdue button and adds it to the window 
	 */
	private void addOverdueButton() {
		// Place the overdue button
		JButton button = new JButton("Overdue");
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 5, 10, 10);
		gb.setConstraints(button, c);
		this.add(button);
		button.addActionListener(this.overdueAction);
	}
}
