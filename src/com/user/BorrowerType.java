/* CPSC 304 - Library Checkout System
 * � Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Main;

/**
 * Representation of a type of borrower as described by BorrowerType in
 * tables.sql.
 * 
 * @author Kevin Petersen
 */
public class BorrowerType {
	private static Connection con = Main.con;
	
	private String type;
	private int bookTimeLimit;

	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the BorrowerType table
	 * 
	 * @param type
	 *            Primary Key string describing type of borrower
	 * @param bookTimeLimit
	 *            Number of days a borrower can borrow a book for
	 */
	private BorrowerType(String type, int bookTimeLimit) {
		this.type = type;
		this.bookTimeLimit = bookTimeLimit;
	}

	/**
	 * Add a type of borrower to the BorrowerType table.
	 * 
	 * @param type
	 *            Primary Key string describing type of borrower
	 * @param bookTimeLimit
	 *            Number of days a borrower can borrow a book for
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static BorrowerType addBorrowerType(String key, int bookTimeLimit)
			throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO BorrowerType VALUES (?,?)");
			
			ps.setString(1, key);
			ps.setInt(2, bookTimeLimit);
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new BorrowerType(key, bookTimeLimit);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			try {
				// Undo
				con.rollback();
			} catch (SQLException sql2) {
				System.out.println("Message: " + sql2.getMessage());
				System.exit(-1);
			}
			throw sql;
		}
	}

	/**
	 * Looks up the entry for the given key in the BorrowerType table and
	 * returns the corresponding object.
	 * 
	 * @param key
	 *            The primary key used to look up the entry
	 * @return A BorrowerType object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static BorrowerType getBorrowerType(String key) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM BorrowerType WHERE type=?");
			ps.setString(1, key);
			
			ps.setMaxRows(1);
			ResultSet r = ps.executeQuery();
			r.next();
			
			return new BorrowerType(key, r.getInt("bookTimeLimit"));
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}

	/**
	 * Looks up all the entries in the BorrowerType table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of BorrowerType objects representing the entries of the BorrowerType table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<BorrowerType> getAllBorrowerTypes() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM BorrowerType");
			ResultSet r = ps.executeQuery();
			List<BorrowerType> allTypes = new ArrayList<BorrowerType>();
			
			while(r.next()) {
				allTypes.add(new BorrowerType(r.getString("type"), r.getInt("bookTimeLimit")));
			}
			
			return allTypes;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}

	/**
	 * Deletes a type of borrower from the BorrowerType table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM BorrowerType WHERE type=?");
			ps.setString(1, this.type);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			try {
				// Undo
				con.rollback();
			} catch (SQLException sql2) {
				System.out.println("Message: " + sql2.getMessage());
				System.exit(-1);
			}
			throw sql;
		}
	}

	/**
	 * @return Primary key string describing this BorrowerType
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Updates this object and the BorrowrType table
	 * 
	 * @param type
	 *            Primary key string describing this BorrowerType
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setType(String type) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE BorrowerType SET type=? WHERE type=?");
			ps.setString(2, this.type);
			
			ps.setString(1, type);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.type = type;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			try {
				// Undo
				con.rollback();
			} catch (SQLException sql2) {
				System.out.println("Message: " + sql2.getMessage());
				System.exit(-1);
			}
			throw sql;
		}
	}

	/**
	 * @return Number of days a borrower can borrow a book for
	 */
	public int getBookTimeLimit() {
		return this.bookTimeLimit;
	}

	/**
	 * Updates this object and the BorrowrType table
	 * 
	 * @param bookTimeLimit
	 *            Number of days a borrower can borrow a book for
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setBookTimeLimit(int bookTimeLimit) throws SQLException {
		try {
			PreparedStatement ps = 
					con.prepareStatement("UPDATE BorrowerType SET bookTimeLimit=? WHERE type=?");
			ps.setString(2, this.type);
			
			ps.setInt(1, bookTimeLimit);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.bookTimeLimit = bookTimeLimit;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			try {
				// Undo
				con.rollback();
			} catch (SQLException sql2) {
				System.out.println("Message: " + sql2.getMessage());
				System.exit(-1);
			}
			throw sql;
		}
	}
}