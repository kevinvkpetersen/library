/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Main;

/**
 * Representation of a book having an author as described by HasAuthor in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class HasAuthor {
	private static Connection con = Main.con;  
	
	private Book callNumber;
	private String name;
	 
	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the HasAuthor table
	 * 
	 * @param callNumber
	 *            Book object that has this author
	 * @param name
	 *            The name of the author
	 */
	private HasAuthor(Book callNumber, String name) {
		this.callNumber = callNumber;
		this.name = name;
	 }
	
	/**
	 * Add an author of a book to the HasAuthor table.
	 * 
	 * @param callNumber
	 *            Book object that has this author
	 * @param name
	 *            The name of the author
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static HasAuthor addHasAuthor(Book callNumber, String name) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO HasAuthor VALUES (?,?)");
			
			ps.setInt(1, callNumber.getCallNumber());
			ps.setString(2, name);
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new HasAuthor(callNumber, name);
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
	 * Looks up the entry for the given pair in the HasAuthor table and returns
	 * the corresponding object.
	 * 
	 * @param callNumber
	 *            Book object that has this author
	 * @param name
	 *            The name of the author
	 * @return A HasAuthor object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static HasAuthor getHasAuthor(Book callNumber, String name) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM HasAuthor WHERE callNumber=? AND name=?");
			ps.setInt(1, callNumber.getCallNumber());
			ps.setString(2, name);
			
			ps.setMaxRows(1);
			ResultSet r = ps.executeQuery();
			r.next();
			
			return parseLine(r);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}

	/**
	 * Looks up all the entries in the HasAuthor table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of HasAuthor objects representing the entries of the HasAuthor table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<HasAuthor> getAllHasAuthors() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM HasAuthor");
			ResultSet r = ps.executeQuery();
			List<HasAuthor> allAuthors = new ArrayList<HasAuthor>();
			
			while(r.next()) {
				allAuthors.add(parseLine(r));
			}
			
			return allAuthors;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding HasAuthor object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The author-book pair represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static HasAuthor parseLine(ResultSet r) throws SQLException {
		Book callNumber = Book.getBook(r.getInt("callNumber"));
		String name = r.getString("name");
		
		return new HasAuthor(callNumber, name);
	}

	/**
	 * Deletes a book-author pair from the HasAuthor table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM HasAuthor WHERE callNumber=? AND name=?");
			ps.setInt(1, this.callNumber.getCallNumber());
			ps.setString(2, this.name);
			
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
	 * @return Book object that has this author
	 */
	public Book getCallNumber() {
		return this.callNumber;
	}

	/**
	 * Updates this object and the HasAuthor table
	 * 
	 * @param callNumber
	 *            Book object that has this author
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setCallNumber(Book callNumber) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HasAuthor SET callNumber=? WHERE callNumber=? AND name=?");
			ps.setInt(2, this.callNumber.getCallNumber());
			ps.setString(3, this.name);
			
			ps.setInt(1, callNumber.getCallNumber());
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.callNumber = callNumber;
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
	 * @return The name of the author
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Updates this object and the HasAuthor table
	 * 
	 * @param name
	 *            The name of the author
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setName(String name) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HasAuthor SET name=? WHERE callNumber=? AND name=?");
			ps.setInt(2, this.callNumber.getCallNumber());
			ps.setString(3, this.name);
			
			ps.setString(1, name);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.name = name;
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