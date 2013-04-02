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
import com.borrower.Borrower;

/**
 * Representation of a hold request as described by HoldRequest in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class HoldRequest {
	private static Connection con = Main.con;  
	
	private int hid;
	private Borrower bid;
	private Book callNumber;
	private Date issuedDate;
	 
	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the HoldRequest table
	 * 
	 * @param hid
	 *            Primary key id number for this hold request
	 * @param bid
	 *            Borrower that requested the hold
	 * @param callNumber
	 *            Book to be held
	 * @param issuedDate
	 *            Date hold was issued
	 */
	private HoldRequest(int hid, Borrower bid, Book callNumber, Date issuedDate) {
		this.hid = hid;
		this.bid = bid;
		this.callNumber = callNumber;
		this.issuedDate = issuedDate;
	}
	
	/**
	 * Generates a new entry in the HoldRequest table
	 * @return A new HoldRequest object with default values.
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static HoldRequest generate() throws SQLException {
		int hid;
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT MAX(hid) as maxHid FROM HoldRequest");
			ResultSet r = ps.executeQuery();
			
			hid = (r.next() ? r.getInt("maxHid") : 0);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
		
		Borrower bid = Borrower.getAll().get(0);
		Book callNumber = Book.getAll().get(0);
		return add(hid + 1, bid, callNumber, new Date(0));
	}
	
	/**
	 * Add a hold to the HoldRequest table.
	 * 
	 * @param hid
	 *            Primary key id number for this hold request
	 * @param bid
	 *            Borrower that requested the hold
	 * @param callNumber
	 *            Book to be held
	 * @param issuedDate
	 *            Date hold was issued
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	private static HoldRequest add(int hid, Borrower bid,
			Book callNumber, Date issuedDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO HoldRequest VALUES (?,?,?,?)");
			
			ps.setInt(1, hid);
			ps.setInt(2, bid.getBid());
			ps.setInt(3, callNumber.getCallNumber());
			ps.setDate(4, issuedDate);
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new HoldRequest(hid, bid, callNumber, issuedDate);
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
	 * Looks up the entry for the given key in the HoldRequest table and returns
	 * the corresponding object.
	 * 
	 * @param hid
	 *            The primary key used to look up the entry
	 * @return A HoldRequest object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static HoldRequest get(int hid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM HoldRequest WHERE hid=?");
			ps.setInt(1, hid);
			ps.setMaxRows(1);
			ResultSet r = ps.executeQuery();
			
			if(r.next()) {
				return parseLine(r);
			} else {
				throw new SQLException("No such Hold Request.");
			}
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}

	/**
	 * Looks up all the entries in the HoldRequest table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of HoldRequest objects representing the entries of the HoldRequest table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<HoldRequest> getAll() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM HoldRequest");
			ResultSet r = ps.executeQuery();
			List<HoldRequest> allHoldRequests = new ArrayList<HoldRequest>();
			
			while(r.next()) {
				allHoldRequests.add(parseLine(r));
			}
			
			return allHoldRequests;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding HoldRequest object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The hold request represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static HoldRequest parseLine(ResultSet r) throws SQLException {
		int hid = r.getInt("hid");
		Borrower bid = Borrower.get(r.getInt("bid"));
		Book callNumber = Book.get(r.getInt("callNumber"));
		Date issuedDate = r.getDate("issuedDate");
		
		return new HoldRequest(hid, bid, callNumber, issuedDate);
	}

	/**
	 * Deletes a hold request from the HoldRequest table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM HoldRequest WHERE hid=?");
			ps.setInt(1, this.hid);
			
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
	 * @return Primary key id number for this hold request
	 */
	public int getHid() {
		return this.hid;
	}

	/**
	 * @return Borrower that requested the hold
	 */
	public Borrower getBid() {
		return this.bid;
	}

	/**
	 * Updates this object and the HoldRequest table
	 * 
	 * @param bid
	 *            Borrower that requested the hold
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setBid(Borrower bid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HoldRequest SET bid=? WHERE hid=?");
			ps.setInt(2, this.hid);
			
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
	 * @return Book to be held
	 */
	public Book getCallNumber() {
		return this.callNumber;
	}

	/**
	 * Updates this object and the HoldRequest table
	 * 
	 * @param callNumber
	 *            Book to be held
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setCallNumber(Book callNumber) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HoldRequest SET callNumber=? WHERE hid=?");
			ps.setInt(2, this.hid);
			
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
	 * @return Date hold was issued
	 */
	public Date getIssuedDate() {
		return this.issuedDate;
	}

	/**
	 * Updates this object and the HoldRequest table
	 * 
	 * @param issuedDate
	 *            Date hold was issued
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setIssuedDate(Date issuedDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE HoldRequest SET issuedDate=? WHERE hid=?");
			ps.setInt(2, this.hid);
			
			ps.setDate(1, issuedDate);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.issuedDate = issuedDate;
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