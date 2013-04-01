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

/**
 * Representation of a fine as described by Fine in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class Fine {
	private static Connection con = Main.con;  
	
	private int fid;
	private float amount;
	private Date issuedDate;
	private Date paidDate;
	private Borrowing borid;
	
	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the Fine table
	 * 
	 * @param fid
	 *            Primary key id number for this fine
	 * @param amount
	 *            Amount owed
	 * @param issuedDate
	 *            Date charged
	 * @param paidDate
	 *            Date paid
	 * @param borid
	 *            Borrow that caused fine
	 */
	private Fine(int fid, float amount, Date issuedDate, Date paidDate, Borrowing borid) {
		this.fid = fid;
		this.amount = amount;
		this.issuedDate = issuedDate;
		this.paidDate = paidDate;
		this.borid = borid;
	}
	
	/**
	 * Add a fine to the Fine table.
	 * 
	 * @param fid
	 *            Primary key id number for this fine
	 * @param amount
	 *            Amount owed
	 * @param issuedDate
	 *            Date charged
	 * @param paidDate
	 *            Date paid
	 * @param borid
	 *            Borrow that caused fine
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static Fine addFine(int fid, float amount, Date issuedDate, Date paidDate, Borrowing borid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO Fine VALUES (?,?,?,?,?)");
			
			ps.setInt(1, fid);
			ps.setFloat(2, amount);
			ps.setDate(3, issuedDate);
			ps.setDate(4, paidDate);
			ps.setInt(5, borid.getBorid());
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new Fine(fid, amount, issuedDate, paidDate, borid);
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
	 * Looks up the entry for the given key in the Fine table and returns
	 * the corresponding object.
	 * 
	 * @param key
	 *            The primary key used to look up the entry
	 * @return A Fine object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static Fine getFine(int key) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Fine WHERE fid=?");
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
	 * Looks up all the entries in the Fine table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of Fine objects representing the entries of the Fine table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<Fine> getAllFines() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Fine");
			ResultSet r = ps.executeQuery();
			List<Fine> allFines = new ArrayList<Fine>();
			
			while(r.next()) {
				allFines.add(parseLine(r));
			}
			
			return allFines;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding Fine object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The fine represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static Fine parseLine(ResultSet r) throws SQLException {
		int fid = r.getInt("fid");
		float amount = r.getFloat("amount");
		Date issuedDate = r.getDate("issuedDate");
		Date paidDate = r.getDate("paidDate");
		Borrowing borid = Borrowing.getBorrowing(r.getInt("borid"));
		
		return new Fine(fid, amount, issuedDate, paidDate, borid);
	}

	/**
	 * Deletes a fine from the Fine table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Fine WHERE fid=?");
			ps.setInt(1, this.fid);
			
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
	 * @return Primary key id number for this fine
	 */
	public int getFid() {
		return this.fid;
	}

	/**
	 * Updates this object and the Fine table
	 * 
	 * @param fid
	 *            Primary key id number for this fine
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setFid(int fid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Fine SET fid=? WHERE fid=?");
			ps.setInt(2, this.fid);
			
			ps.setInt(1, fid);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.fid = fid;
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
	 * @return Amount owed
	 */
	public float getAmount() {
		return this.amount;
	}

	/**
	 * Updates this object and the Fine table
	 * 
	 * @param amount
	 *            Amount owed
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setAmount(float amount) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Fine SET amount=? WHERE fid=?");
			ps.setInt(2, this.fid);
			
			ps.setFloat(1, amount);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.amount = amount;
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
	 * @return Date charged
	 */
	public Date getIssuedDate() {
		return this.issuedDate;
	}

	/**
	 * Updates this object and the Fine table
	 * 
	 * @param issuedDate
	 *            Date charged
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setIssuedDate(Date issuedDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Fine SET issuedDate=? WHERE fid=?");
			ps.setInt(2, this.fid);
			
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

	/**
	 * @return Date paid
	 */
	public Date getPaidDate() {
		return this.paidDate;
	}

	/**
	 * Updates this object and the Fine table
	 * 
	 * @param paidDate
	 *            Date paid
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setPaidDate(Date paidDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Fine SET paidDate=? WHERE fid=?");
			ps.setInt(2, this.fid);
			
			ps.setDate(1, paidDate);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.paidDate = paidDate;
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
	 * @return Borrow that caused fine
	 */
	public Borrowing getBorid() {
		return this.borid;
	}

	/**
	 * Updates this object and the Fine table
	 * 
	 * @param borid
	 *            Borrow that caused fine
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setBorid(Borrowing borid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Fine SET borid=? WHERE fid=?");
			ps.setInt(2, this.fid);
			
			ps.setInt(1, borid.getBorid());
			
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
}