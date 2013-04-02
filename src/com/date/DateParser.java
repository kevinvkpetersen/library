/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com.date;

import java.sql.*;
import java.util.Calendar;

public class DateParser {
	public static Date today() {
		Calendar cal = Calendar.getInstance();
		return new Date(cal.getTimeInMillis());
	}
	
	/**
	 * Converts a string of text in the format YYYY-MM-DD (ISO 8601) to SQL Date
	 * format.
	 * 
	 * @param dateString
	 *            Raw date string in the form YYYY-MM-DD
	 * @return SQL Date object
	 */
	public static Date convertToDate(String dateString) {
		int year = Integer.parseInt(dateString.substring(0, 4));
		int month = Integer.parseInt(dateString.substring(5, 7));
		int day = Integer.parseInt(dateString.substring(8));
		return convertToDate(year, month, day);
	}
	
	/**
	 * Converts a a year month and day of the month to SQL Date format.
	 * 
	 * @param year
	 *            The value used to set the YEAR calendar field.
	 * @param month
	 *            The value used to set the MONTH calendar field.
	 * @param day
	 *            The value used to set the DAY_OF_MONTH calendar field.
	 * @return The equivalent SQL Date object.
	 */
	public static Date convertToDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(year, month-1, day);
		return new Date(cal.getTimeInMillis());
	}
}