/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.book;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Main;
import com.book.records.Borrowing;
import com.book.records.Fine;
import com.borrower.Borrower;
import com.date.DateParser;

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
	 * Add a copy to the BookCopy table.
	 * 
	 * @param callNumber
	 *            Book object this is a copy of
	 * @param status
	 *            This copy's status
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static BookCopy add(Book callNumber, String status)
			throws SQLException {
		int copyNo = generateKey(callNumber);
		
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
	 * Generates a new copyNo for an entry in the BookCopy table
	 * 
	 * @return An unused unique copyNo for this Book
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	private static int generateKey(Book callNumber) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT MAX(copyNo) as maxCopyNo FROM BookCopy WHERE callNumber=?");
			ps.setInt(1, callNumber.getCallNumber());
			ResultSet r = ps.executeQuery();
			
			return (r.next() ? r.getInt("maxCopyNo") + 1 : 1);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
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
	 * Looks up all the entries in the BookCopy table that are checked out and
	 * returns the corresponding objects in a list.
	 * 
	 * @return A List of BookCopy objects representing the entries of the
	 *         BookCopy table that are checked out
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<BookCopy> getCheckedOut() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM BookCopy WHERE status='out' ORDER BY callNumber, copyNo");
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
	 * Looks up all the entries in the BookCopy table that are checked out and
	 * returns the corresponding objects in a list.
	 * 
	 * @return A List of BookCopy objects representing the entries of the
	 *         BookCopy table that are checked out
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<BookCopy> getOverdue() throws SQLException {
		List<BookCopy> out = getCheckedOut();
		List<BookCopy> overdue = new ArrayList<BookCopy>();
		
		for(BookCopy b : out) {
			Borrowing borid = Borrowing.getLast(b);
			if(DateParser.today().after(borid.getInDate())) {
				overdue.add(b);
			}
		}
		
		return overdue;
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
	
	public Borrowing checkout(Borrower bid) throws SQLException {
		Date outDate = DateParser.today();
		Date dueDate = DateParser.todayPlusDays(bid.getType().getBookTimeLimit());
		Borrowing record = Borrowing.add(bid, this, outDate, dueDate);
		this.setStatus("out");
		System.out.println("Copy #" + this.copyNo + " of Book with Call Number "
				+ this.callNumber.getCallNumber() + " checked out.");
		return record;
	}
	
	public void doReturn() throws SQLException {
		Borrowing record = Borrowing.getLast(this);
		
		int daysLate = DateParser.daysBetween(DateParser.today(), record.getInDate());
		if(daysLate > 0) {
			Fine.add(Fine.FEE_PER_DAY*daysLate, DateParser.today(), null, record);
		}
		
		// Check for hold
		if(this.callNumber.hasHold()) {
			this.setStatus("on-hold");
			int bid = this.callNumber.getHold().getBid().getBid();
			System.out.println("Notify borrower #" + bid);
		} else {
			this.setStatus("in");
		}
		
		System.out.print("Book returned.");
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
	public String getStatus() {
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
	public void setStatus(String status) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE BookCopy SET status=? WHERE callNumber=? AND copyNo=?");
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