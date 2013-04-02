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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.book.Book;
import com.book.records.Borrowing;
import com.borrower.Borrower;


/**
 * This class implements a graphical login window to connect to the Oracle
 * database.
 * 
 * @author Kevin Petersen
 */
public class Checkout {
	private JFrame frame = new JFrame("Check Out");
	private JPanel contentPane = new JPanel();
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private final int FIELD_WIDTH = 30;
	private final int LABEL_ALIGNMENT = GridBagConstraints.LINE_START;
	private final int NUM_BOOK_FIELDS = 5;
	
	private JTextField bidField = new JTextField(FIELD_WIDTH);
	private JTextField[] bookField = new JTextField[NUM_BOOK_FIELDS];
	
	private ActionListener checkoutAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				Borrower bid = Borrower.get(Integer.parseInt(bidField.getText()));
				if(bid.isValid()) {
					List<Borrowing> receipt = new ArrayList<Borrowing>();
					
					for(int i = 0; i < NUM_BOOK_FIELDS; i++) {
						String bookString = bookField[i].getText();
						if (bookString.isEmpty()) {
							continue;
						}
						
						try {
							Book book = Book.get(Integer.parseInt(bookString));
							receipt.add(book.findAvailableCopy().checkout(bid));
							bookField[i].setText("");
						} catch (SQLException sql) {
							System.out.println("Could not checkout book " + (i+1));
						}
					}
					
					if(!receipt.isEmpty()) {
						System.out.println("To be returned on or before: " +
								receipt.get(0).getInDate());
					}
				} else {
					System.out.println("Borrower is cannot borrow books.");
				}
			} catch (SQLException sql) {
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
	public Checkout() {
		initializePane();
		addBid();
		for(int i = 0; i < NUM_BOOK_FIELDS; i++) {
			addBook(i);
		}
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
		c.insets = new Insets(10, 10, 15, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the bid
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 15, 10);
		gb.setConstraints(bidField, c);
		contentPane.add(bidField);
	}
	
	/**
	 * Builds the book field and label and adds them to the window 
	 */
	private void addBook(int i) {
		// Place the book label
		JLabel label = new JLabel("Enter Call Number: ");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 5, 0);
		c.anchor = LABEL_ALIGNMENT;
		gb.setConstraints(label, c);
		contentPane.add(label);

		// Place the text field for the book
		this.bookField[i] = new JTextField(FIELD_WIDTH);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 5, 10);
		gb.setConstraints(this.bookField[i], c);
		contentPane.add(this.bookField[i]);
	}
	
	/**
	 * Builds the submit button and adds it to the window 
	 */
	private void addSubmitButton() {
		// Place the submit button
		JButton button = new JButton("Check out");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.LINE_START;
		gb.setConstraints(button, c);
		contentPane.add(button);
		button.addActionListener(this.checkoutAction);
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