package com.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.Main;

/**
 * Implements a text based menu used to connect to the database.
 * 
 * @author Kevin Petersen
 */
public class MainMenu {
	private JFrame frame = new JFrame("Main Menu");
	private JPanel contentPane = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	private Connection con = Main.con;
	
	static final int BUTTON_HEIGHT = 30;
	static final int BUTTON_WIDTH = 200;
	
	/**
	 * Builds the components and does any initialization for the window
	 */
	MainMenu() {
		tabbedPane.addTab("Clerk", new ClerkTab());
		tabbedPane.addTab("Borrower", new BorrowerTab());
		tabbedPane.addTab("Librarian", new LibrarianTab());
		initializePane();
	}
	
	/**
	 * Builds the base frame and pane for the window
	 */
	private void initializePane() {
		contentPane.add(tabbedPane);
		frame.setContentPane(contentPane);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					con.close();
					System.exit(0);
				} catch (SQLException sql) {
					System.out.println("Message: " + sql.getMessage());
					System.exit(-1);
				}
			}
		});
	}
	
	/**
	 * Packs, places and makes the window visible
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