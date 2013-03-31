package com.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.Main;
import com.user.BorrowerType;

/**
 * Implements a text based menu used to connect to the database.
 * 
 * @author Kevin Petersen
 */
public class MainMenu {
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private Connection con = Main.con;
	
	/**
	 * Temporary text based menu
	 */
	public void showMenu() {
		int choice;
		boolean quit = false;

		try {
			// Disable auto commit mode
			con.setAutoCommit(false);

			while (!quit) {
				System.out.print("\n\nPlease choose one of the following: \n");
				System.out.print("1.  Insert a Type\n");
				System.out.print("2.  Delete a Type\n");
				System.out.print("3.  Update type\n");
				System.out.print("4.  Update bookTimeLimit\n");
				System.out.print("5.  Show a Type\n");
				System.out.print("6.  Show all Types\n");
				System.out.print("7.  Quit\n>> ");

				choice = Integer.parseInt(in.readLine());

				System.out.println(" ");

				String type;
				int limit;
				try {
					switch (choice) {
					case 1:
						System.out.print("\nType: ");
						type = in.readLine();
						
						System.out.print("\nbookTimeLimit: ");
						limit = Integer.parseInt(in.readLine());
						
						BorrowerType.addBorrowerType(type, limit);
						break;
					case 2:
						System.out.print("\nType: ");
						type = in.readLine();
						
						BorrowerType.getBorrowerType(type).delete();
						break;
					case 3:
						System.out.print("\nOld Type: ");
						type = in.readLine();
						
						System.out.print("\nNew Type: ");
						String newType = in.readLine();
						
						BorrowerType.getBorrowerType(type).setType(newType);
						break;
					case 4:
						System.out.print("\nType: ");
						type = in.readLine();
						
						System.out.print("\nNew bookTimeLimit: ");
						limit = Integer.parseInt(in.readLine());
						
						BorrowerType.getBorrowerType(type).setBookTimeLimit(limit);
						break;
					case 5:
						System.out.print("\nType: ");
						type = in.readLine();
						
						BorrowerType t = BorrowerType.getBorrowerType(type);
						System.out.print("\n" + t.getType());
						System.out.print("\t" + t.getBookTimeLimit());
						break;
					case 6:
						List<BorrowerType> l = BorrowerType.getAllBorrowerTypes();
						
						System.out.print("\nType\tbookTimeLimit");
						for(BorrowerType bt : l) {
							System.out.print("\n" + bt.getType());
							System.out.print("\t" + bt.getBookTimeLimit());
						}
						
						break;
					case 7:
						quit = true;
					}
				} catch(SQLException sql) {
				}
			}

			con.close();
			in.close();
			System.out.println("\nGood Bye!\n\n");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("IOException!");

			try {
				con.close();
				System.exit(-1);
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}
}