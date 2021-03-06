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
import com.date.DateParser;

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
	public static Borrowing add(Borrower bid, BookCopy callNumber,
			Date outDate, Date inDate) throws SQLException {
		int borid = generateKey();
		
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
	 * Generates a new key for an entry in the Borrower table
	 * 
	 * @return An unused unique key for this table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	private static int generateKey() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT MAX(borid) as maxBorid FROM Borrowing");
			ResultSet r = ps.executeQuery();
			
			return (r.next() ? r.getInt("maxBorid") + 1 : 1);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Looks up the entry for the given key in the Borrowing table and returns
	 * the corresponding object.
	 * 
	 * @param borid
	 *            The primary key used to look up the entry
	 * @return A Borrowing object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static Borrowing get(int borid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Borrowing WHERE borid=?");
			ps.setInt(1, borid);
			ps.setMaxRows(1);
			ResultSet r = ps.executeQuery();
			
			if(r.next()) {
				return parseLine(r);
			} else {
				throw new SQLException("No such Borrow record.");
			}
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}

	/**
	 * Looks up the entry for the given key in the Borrowing table and returns
	 * the corresponding object.
	 * 
	 * @param borid
	 *            The primary key used to look up the entry
	 * @return A Borrowing object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static Borrowing getLast(BookCopy callNumber) throws SQLException {
		Date recent;
		int borid;
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT MAX(outDate) as recent FROM Borrowing WHERE callNumber=? AND copyNo=?");
			ps.setInt(1, callNumber.getCallNumber().getCallNumber());
			ps.setInt(2, callNumber.getCopyNo());
			ResultSet r = ps.executeQuery();
			if(r.next()) {
				recent = r.getDate("recent");
			} else {
				throw new SQLException("No such Borrow record.");
			}
			
			ps = con.prepareStatement("SELECT borid FROM Borrowing WHERE callNumber=? AND copyNo=? AND outDate=?");
			ps.setInt(1, callNumber.getCallNumber().getCallNumber());
			ps.setInt(2, callNumber.getCopyNo());
			ps.setDate(3, recent);
			r = ps.executeQuery();
			if(r.next()) {
				borid = r.getInt("borid");
			} else {
				throw new SQLException("No such Borrow record.");
			}
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
		
		return Borrowing.get(borid);
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
	public static List<Borrowing> getCheckedOut() throws SQLException {
		List<BookCopy> outBooks = BookCopy.getCheckedOut();
		List<Borrowing> records = new ArrayList<Borrowing>();
		
		for(BookCopy b : outBooks) {
			records.add(getLast(b));
		}
		
		return records;
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
	public static List<Borrowing> getOverdue() throws SQLException {
		List<BookCopy> outBooks = BookCopy.getCheckedOut();
		List<Borrowing> overdue = new ArrayList<Borrowing>();
		
		for(BookCopy b : outBooks) {
			Borrowing borid = getLast(b);
			if(DateParser.today().after(borid.getInDate())) {
				overdue.add(borid);
			}
		}
		
		return overdue;
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
	public static List<Borrowing> getAll() throws SQLException {
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
		Borrower bid = Borrower.get(r.getInt("bid"));
		
		Book b = Book.get(r.getInt("callNumber"));
		BookCopy callNumber = BookCopy.get(b, r.getInt("copyNo"));
		
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