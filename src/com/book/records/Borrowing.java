/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.book.records;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Main;
import com.book.Book;
import com.book.BookCopy;
import com.borrower.Borrower;

/**
 * Representation of a borrow as described by Borrowing in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class Borrowing {
	private static Connection con = Main.con;  
	
	private int borid;
	private Borrower bid;
	private BookCopy callNumber;
	private Date outDate;
	private Date inDate;
	 
	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the Borrowing table
	 * 
	 * @param borid
	 *            Primary key id number for this borrow
	 * @param bid
	 *            Borrower that borrowed the book
	 * @param callNumber
	 *            Book that was borrowed
	 * @param outDate
	 *            Date borrowed
	 * @param inDate
	 *            Date returned
	 */
	private Borrowing(int borid, Borrower bid, BookCopy callNumber, Date outDate, Date inDate) {
		this.borid = borid;
		this.bid = bid;
		this.callNumber = callNumber;
		this.outDate = outDate;
		this.inDate = inDate;
	}
	
	/**
	 * Add a borrow record to the Borrowing table.
	 * 
	 * @param borid
	 *            Primary key id number for this borrow
	 * @param bid
	 *            Borrower that borrowed the book
	 * @param callNumber
	 *            Book that was borrowed
	 * @param outDate
	 *            Date borrowed
	 * @param inDate
	 *            Date returned
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static Borrowing addBorrowing(int borid, Borrower bid,
			BookCopy callNumber, Date outDate, Date inDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO Borrowing VALUES (?,?,?,?,?,?)");
			
			ps.setInt(1, borid);
			ps.setInt(2, bid.getBid());
			ps.setInt(3, callNumber.getCallNumber().getCallNumber());
			ps.setInt(4, callNumber.getCopyNo());
			ps.setDate(5, outDate);
			ps.setDate(6, inDate);
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new Borrowing(borid, bid, callNumber, outDate, inDate);
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
	 * Looks up the entry for the given key in the Borrowing table and returns
	 * the corresponding object.
	 * 
	 * @param key
	 *            The primary key used to look up the entry
	 * @return A Borrowing object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static Borrowing getBorrowing(int key) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Borrowing WHERE borid=?");
			ps.setInt(1, key);
			
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
	 * Looks up all the entries in the Borrowing table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of Borrowing objects representing the entries of the Borrowing table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<Borrowing> getAllBorrowings() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Borrowing");
			ResultSet r = ps.executeQuery();
			List<Borrowing> allBorrowings = new ArrayList<Borrowing>();
			
			while(r.next()) {
				allBorrowings.add(parseLine(r));
			}
			
			return allBorrowings;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding Borrowing object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The borrow represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static Borrowing parseLine(ResultSet r) throws SQLException {
		int borid = r.getInt("borid");
		Borrower bid = Borrower.getBorrower(r.getInt("bid"));
		
		Book b = Book.getBook(r.getInt("callNumber"));
		BookCopy callNumber = BookCopy.getBookCopy(b, r.getInt("copyNo"));
		
		Date outDate = r.getDate("outDate");
		Date inDate = r.getDate("inDate");
		
		return new Borrowing(borid, bid, callNumber, outDate, inDate);
	}

	/**
	 * Deletes a borrow from the Borrowing table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Borrowing WHERE borid=?");
			ps.setInt(1, this.borid);
			
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
	 * @return Primary key id number for this borrow
	 */
	public int getBorid() {
		return this.borid;
	}

	/**
	 * Updates this object and the Borrowing table
	 * 
	 * @param borid
	 *            Primary key id number for this borrow
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setBorid(int borid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrowing SET borid=? WHERE borid=?");
			ps.setInt(2, this.borid);
			
			ps.setInt(1, borid);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.borid = borid;
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
	 * @return Borrower that borrowed the book
	 */
	public Borrower getBid() {
		return this.bid;
	}

	/**
	 * Updates this object and the Borrowing table
	 * 
	 * @param bid
	 *            Borrower that borrowed the book
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setBid(Borrower bid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrowing SET bid=? WHERE borid=?");
			ps.setInt(2, this.borid);
			
			ps.setInt(1, bid.getBid());
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.bid = bid;
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
	 * @return Book that was borrowed
	 */
	public BookCopy getCallNumber() {
		return this.callNumber;
	}

	/**
	 * Updates this object and the Borrowing table
	 * 
	 * @param callNumber
	 *            Book that was borrowed
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setCallNumber(BookCopy callNumber) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrowing SET callNumber=?, copyNo=? WHERE borid=?");
			ps.setInt(3, this.borid);
			
			ps.setInt(1, callNumber.getCallNumber().getCallNumber());
			ps.setInt(2, callNumber.getCopyNo());
			
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
	 * @return Date borrowed
	 */
	public Date getOutDate() {
		return this.outDate;
	}

	/**
	 * Updates this object and the Borrowing table
	 * 
	 * @param outDate
	 *            Date borrowed
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setOutDate(Date outDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrowing SET outDate=? WHERE borid=?");
			ps.setInt(2, this.borid);
			
			ps.setDate(1, outDate);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.outDate = outDate;
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
	 * @return Date returned
	 */
	public Date getInDate() {
		return this.inDate;
	}

	/**
	 * Updates this object and the Borrowing table
	 * 
	 * @param inDate
	 *            Date returned
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setInDate(Date inDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrowing SET inDate=? WHERE borid=?");
			ps.setInt(2, this.borid);
			
			ps.setDate(1, inDate);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.inDate = inDate;
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