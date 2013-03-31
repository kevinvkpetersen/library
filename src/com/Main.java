/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com;

import java.sql.Connection;

import com.ui.LoginWindow;

/**
 * Entry point for Library database system.
 * 
 * @author Kevin Petersen
 */
public class Main {
	public static Connection con;
	
	/**
	 * Entry point. Loads the UI.
	 * 
	 * @param args Arguments from the command line
	 */
	public static void main(String args[]) {
		new LoginWindow().displayWindow();
	}
	
	
}