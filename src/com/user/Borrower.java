/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Main;

/**
 * Representation of a borrower as described by Borrower in tables.sql.
 * 
 * @author Kevin Petersen
 */
public class Borrower {
	private static Connection con = Main.con;  
	
	private int bid;
	private String password;
	private String name;
	private String address;
	private int phone;
	private String emailAddress;
	private int sinOrStNo;
	private Date expiryDate;
	private BorrowerType type;

	/**
	 * Constructor used only by this class to enforce an instance being a valid
	 * entry in the Borrower table
	 * 
	 * @param bid
	 *            Primary key id number for this borrower
	 * @param password
	 *            This borrower's password
	 * @param name
	 *            Borrower's full name
	 * @param address
	 *            Borrower's full address
	 * @param phone
	 *            Borrower's phone number
	 * @param email
	 *            Borrower's email
	 * @param sinOrStNo
	 *            Borrower's student number if a student, SIN otherwise
	 * @param expiryDate
	 *            Library card expiry date
	 * @param type
	 *            Foreign Key string describing type of borrower
	 */
	private Borrower(int bid, String password,
			String name, String address, int phone, String email,
			int sinOrStNo, Date expiryDate, BorrowerType type) {
		this.bid = bid;
		this.password = password;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.emailAddress = email;
		this.sinOrStNo = sinOrStNo;
		this.expiryDate = expiryDate;
		this.type = type;
	}
	
	/**
	 * Add a borrower to the Borrower table.
	 * 
	 * @param bid
	 *            Primary key id number for this borrower
	 * @param password
	 *            This borrower's password
	 * @param name
	 *            Borrower's full name
	 * @param address
	 *            Borrower's full address
	 * @param phone
	 *            Borrower's phone number
	 * @param emailAddress
	 *            Borrower's email
	 * @param sinOrStNo
	 *            Borrower's student number if a student, SIN otherwise
	 * @param expiryDate
	 *            Library card expiry date
	 * @param type
	 *            Foreign Key string describing type of borrower
	 * @return Object representing the newly created entry
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public static Borrower addBorrower(int bid, String password, String name,
			String address, int phone, String emailAddress, int sinOrStNo,
			Date expiryDate, BorrowerType type) throws SQLException {
		
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO Borrower VALUES (?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, bid);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setInt(5, phone);
			ps.setString(6, emailAddress);
			ps.setInt(7, sinOrStNo);
			ps.setDate(8, expiryDate);
			ps.setString(9, type.getType());
			
			// All inputs are OK
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			return new Borrower(bid, password, name, address, phone,
					emailAddress, sinOrStNo, expiryDate, type);
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			try {
				// undo the insert
				con.rollback();
			} catch (SQLException sql2) {
				System.out.println("Message: " + sql2.getMessage());
				System.exit(-1);
			}
			throw sql;
		}
	}
	
	/**
	 * Looks up the entry for the given key in the Borrower table and returns
	 * the corresponding object.
	 * 
	 * @param key
	 *            The primary key used to look up the entry
	 * @return A Borrower object representing the entry with the given key
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static Borrower getBorrower(int key) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Borrower WHERE bid=?");
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
	 * Looks up all the entries in the Borrower table and returns the corresponding
	 * objects in a list.
	 * 
	 * @return A List of Borrower objects representing the entries of the Borrower table
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return
	 *             a ResultSet object
	 */
	public static List<Borrower> getAllBorrowers() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Borrower");
			ResultSet r = ps.executeQuery();
			List<Borrower> allBorrowers = new ArrayList<Borrower>();
			
			while(r.next()) {
				allBorrowers.add(parseLine(r));
			}
			
			return allBorrowers;
		} catch (SQLException sql) {
			System.out.println("Message: " + sql.getMessage());
			throw sql;
		}
	}
	
	/**
	 * Reads the data from the current row in the result set and generates the
	 * corresponding Borrower object
	 * 
	 * @param r
	 *            Result Set of a query
	 * @return The borrower represented by the current row of data
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private static Borrower parseLine(ResultSet r) throws SQLException {
		int bid = r.getInt("bid");
		String password = r.getString("password");
		String name = r.getString("name");
		
		String address = r.getString("address");
		address = (r.wasNull() ? null : address);
		
		int phone = r.getInt("phone");
		phone = (r.wasNull() ? null : phone);
		
		String emailAddress = r.getString("emailAddress");
		emailAddress = (r.wasNull() ? null : emailAddress);
		
		int sinOrStNo = r.getInt("sinOrStNo");
		
		Date expiryDate = r.getDate("expiryDate");
		expiryDate = (r.wasNull() ? null : expiryDate);
		
		BorrowerType type = BorrowerType.getBorrowerType(r.getString("type"));
		
		return new Borrower(bid, password, name, address, phone,
				emailAddress, sinOrStNo, expiryDate, type);
	}

	/**
	 * Deletes a borrower from the Borrower table.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void delete() throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Borrower WHERE bid=?");
			ps.setInt(1, this.bid);
			
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
	 * @return Primary key id number for this borrower
	 */
	public int getBid() {
		return this.bid;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param bid
	 *            Primary key id number for this borrower
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setBid(int bid) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET bid=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setInt(1, bid);
			
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
	 * @return This borrower's password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param password
	 *            This borrower's password
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setPassword(String password) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET password=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setString(1, password);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.password = password;
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
	 * @return Borrower's full name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param name
	 *            Borrower's full name
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setName(String name) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET name=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
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

	/**
	 * @return Borrower's full address
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param address
	 *            Borrower's full address
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setAddress(String address) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET address=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setString(1, address);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.address = address;
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
	 * @return Borrower's phone number
	 */
	public int getPhone() {
		return this.phone;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param phone
	 *            Borrower's phone number
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setPhone(int phone) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET phone=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setInt(1, phone);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.phone = phone;
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
	 * @return Borrower's email
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param emailAddress
	 *            Borrower's email
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setEmailAddress(String emailAddress) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET emailAddress=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setString(1, emailAddress);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.emailAddress = emailAddress;
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
	 * @return Borrower's student number if a student, SIN otherwise
	 */
	public int getSinOrStNo() {
		return this.sinOrStNo;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param sinOrStNo
	 *            Borrower's student number if a student, SIN otherwise
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setSinOrStNo(int sinOrStNo) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET sinOrStNo=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setInt(1, sinOrStNo);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.sinOrStNo = sinOrStNo;
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
	 * @return Library card expiry date
	 */
	public Date getExpiryDate() {
		return this.expiryDate;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param expiryDate
	 *            Library card expiry date
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setExpiryDate(Date expiryDate) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET expiryDate=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setDate(1, expiryDate);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			this.expiryDate = expiryDate;
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
	 * @return Foreign Key string describing type of borrower
	 */
	public BorrowerType getType() {
		return this.type;
	}

	/**
	 * Updates this object and the Borrower table
	 * 
	 * @param type
	 *            Foreign Key string describing type of borrower
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement returns a
	 *             ResultSet object
	 */
	public void setType(BorrowerType type) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Borrower SET type=? WHERE bid=?");
			ps.setInt(2, this.bid);
			
			ps.setString(1, type.getType());
			
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
}