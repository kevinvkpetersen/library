package com.date;

import java.sql.*;
import java.util.Calendar;

public class DateParser {
	/**
	 * Converts a string of text in the format YYYY-MM-DD (ISO 8601) to SQL Date
	 * format.
	 * 
	 * @param dateString
	 *            Raw date string in the form YYYY-MM-DD
	 * @return SQL Date object
	 */
	public static Date parseString(String dateString) {
		int year = Integer.parseInt(dateString.substring(0, 4));
		int month = Integer.parseInt(dateString.substring(5, 7));
		int day = Integer.parseInt(dateString.substring(8));
		return convertToDate(year, month, day);
	}
	
	public static Date convertToDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(year, month-1, day);
		return new Date(cal.getTimeInMillis());
	}
}