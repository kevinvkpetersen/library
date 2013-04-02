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
 * Representation of a book as described by Book in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class BookCopy {
	private static Connection con = Main.con;  
	
	private Book callNumber;
	private int copyNo;
	private String status;
	 
	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the BookCopy table
	 * 
	 * @param callNumber
	 *            Book object this is a copy of
	 * @param copyNo
	 *            This book's copy number
	 * @param status
	 *            This copy's status
	 */
	private BookCopy(Book callNumber, int copyNo, String status) {
		this.callNumber = callNumber;
		this.copyNo = copyNo;
		this.status = status;
	 }
	
	/**
	 * Generates a new entry in the BookCopy table
	 * 
	 * @return A new BookCopy object with default values.
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static BookCopy generate(Book callNumber) throws SQLException {
		int copyNo;
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT MAX(copyNo) as maxCopyNo FROM BookCopy WHERE callNumber=?");
			ps.setInt(1, callNumber.getCallNumber());
			ResultSet r = ps.executeQuery();
			
			copyNo = (r.next() ? r.getInt("maxCopyNo") : 0);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
		
		return add(callNumber, copyNo + 1, "in");
	}
	
	/**
	 * Add a copy to the BookCopy table.
	 * 
	 * @param callNumber
	 *            Book object this is a copy of
	 * @param copyNo
	 *            This book's copy number
	 * @param status
	 *            This copy's status
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	private static BookCopy add(Book callNumber, int copyNo, String status) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO BookCopy VALUES (?,?,?)");
			
			ps.setInt(1, callNumber.getCallNumber());
			ps.setInt(2, copyNo);
			ps.setString(3, status);
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new BookCopy(callNumber, copyNo, status);
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
	 * Looks up the entry for the given key in the BookCopy table and returns
	 * the corresponding object.
	 * 
	 * @param callNumber
	 *            Book object this is a copy of
	 * @param copyNo
	 *            This book's copy number
	 * @return A BookCopy object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static BookCopy get(Book callNumber, int copyNo) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM BookCopy WHERE callNumber=? AND copyNo=?");
			ps.setInt(1, callNumber.getCallNumber());
			ps.setInt(2, copyNo);

			ps.setMaxRows(1);
			ResultSet r = ps.executeQuery();
			
			if(r.next()) {
				return parseLine(r);
			} else {
				throw new SQLException("No such Copy of this Book.");
			}
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}

	/**
	 * Looks up all the entries in the BookCopy table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of BookCopy objects representing the entries of the BookCopy table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<BookCopy> getAll() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM BookCopy");
			ResultSet r = ps.executeQuery();
			List<BookCopy> allBooks = new ArrayList<BookCopy>();
			
			while(r.next()) {
				allBooks.add(parseLine(r));
			}
			
			return allBooks;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding BookCopy object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The copy represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static BookCopy parseLine(ResultSet r) throws SQLException {
		Book callNumber = Book.get(r.getInt("callNumber"));
		int copyNo = r.getInt("copyNo");
		String status = r.getString("status");
		
		return new BookCopy(callNumber, copyNo, status);
	}

	/**
	 * Deletes a copy from the BookCopy table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM BookCopy WHERE callNumber=? AND copyNo=?");
			ps.setInt(1, this.callNumber.getCallNumber());
			ps.setInt(2, this.copyNo);
			
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
	 * @return Book object this is a copy of
	 */
	public Book getCallNumber() {
		return this.callNumber;
	}

	/**
	 * @return This book's copy number
	 */
	public int getCopyNo() {
		return this.copyNo;
	}

	/**
	 * @return This copy's status
	 */
	public String getstatus() {
		return this.status;
	}

	/**
	 * Updates this object and the Book table
	 * 
	 * @param status
	 *            This copy's status
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setstatus(String status) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Book SET status=? WHERE callNumber=? AND copyNo=?");
			ps.setInt(2, this.callNumber.getCallNumber());
			ps.setInt(3, this.copyNo);
			
			ps.setString(1, status);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.status = status;
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