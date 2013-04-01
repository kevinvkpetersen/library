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
 * Representation of a book having a subject as described by HasSubject in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class HasSubject {
	private static Connection con = Main.con;  
	
	private Book callNumber;
	private String subject;
	 
	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the HasSubject table
	 * 
	 * @param callNumber
	 *            Book object that has this subject
	 * @param subject
	 *            The name of the subject
	 */
	private HasSubject(Book callNumber, String subject) {
		this.callNumber = callNumber;
		this.subject = subject;
	 }
	
	/**
	 * Add a subject of a book to the HasSubject table.
	 * 
	 * @param callNumber
	 *            Book object that has this subject
	 * @param subject
	 *            The name of the subject
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static HasSubject addHasSubject(Book callNumber, String subject) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO HasSubject VALUES (?,?)");
			
			ps.setInt(1, callNumber.getCallNumber());
			ps.setString(2, subject);
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new HasSubject(callNumber, subject);
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
	 * Looks up the entry for the given pair in the HasSubject table and returns
	 * the corresponding object.
	 * 
	 * @param callNumber
	 *            Book object that has this subject
	 * @param subject
	 *            The name of the subject
	 * @return A HasSubject object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static HasSubject getHasSubject(Book callNumber, String subject) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM HasSubject WHERE callNumber=? AND subject=?");
			ps.setInt(1, callNumber.getCallNumber());
			ps.setString(2, subject);
			
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
	 * Looks up all the entries in the HasSubject table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of HasSubject objects representing the entries of the HasSubject table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<HasSubject> getAllHasSubjects() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM HasSubject");
			ResultSet r = ps.executeQuery();
			List<HasSubject> allSubjects = new ArrayList<HasSubject>();
			
			while(r.next()) {
				allSubjects.add(parseLine(r));
			}
			
			return allSubjects;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding HasSubject object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The subject-book pair represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static HasSubject parseLine(ResultSet r) throws SQLException {
		Book callNumber = Book.getBook(r.getInt("callNumber"));
		String subject = r.getString("subject");
		
		return new HasSubject(callNumber, subject);
	}

	/**
	 * Deletes a book-subject pair from the HasSubject table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM HasSubject WHERE callNumber=? AND subject=?");
			ps.setInt(1, this.callNumber.getCallNumber());
			ps.setString(2, this.subject);
			
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
	 * @return Book object that has this subject
	 */
	public Book getCallNumber() {
		return this.callNumber;
	}

	/**
	 * Updates this object and the HasSubject table
	 * 
	 * @param callNumber
	 *            Book object that has this subject
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setCallNumber(Book callNumber) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HasSubject SET callNumber=? WHERE callNumber=? AND subject=?");
			ps.setInt(2, this.callNumber.getCallNumber());
			ps.setString(3, this.subject);
			
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
	 * @return The name of the subject
	 */
	public String getName() {
		return this.subject;
	}

	/**
	 * Updates this object and the HasSubject table
	 * 
	 * @param subject
	 *            The name of the subject
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setName(String subject) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HasSubject SET subject=? WHERE callNumber=? AND subject=?");
			ps.setInt(2, this.callNumber.getCallNumber());
			ps.setString(3, this.subject);
			
			ps.setString(1, subject);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.subject = subject;
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