package com.tagcash.waalah.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {

	private static final int SECOND = 1;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;
	private static final int WEEK = 7 * DAY;
	private static final int MONTH = 30 * DAY;

	public static String dateStringToOtherDateString(String value, String inFormat, String outFormat) {
		SimpleDateFormat format = new SimpleDateFormat(inFormat);
		SimpleDateFormat toformat = new SimpleDateFormat(outFormat);
		Date date;
		String returnValue = null;
		try {  
			date = format.parse(value);
			returnValue = toformat.format(date);
		} catch (Exception e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}
		return returnValue;
	}

	/*
	 * Date -> yyyyMMdd
	 * Date -> MMdd
	 * Date -> yyyy/MM/dd
	 * Date -> dd/MM/yyyy
	 */
	public static String dateToString(Date date, String strformat) {
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		return format.format(date);
	}

	/*
	 * yyyyMMdd -> Date 
	 * MMdd -> Date 
	 * yyyy/MM/dd -> Date 
	 * dd/MM/yyyy -> Date 
	 */
	public static Date stringToDate(String strDate, String strformat) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	@SuppressWarnings("deprecation")
	public static int getAgeFromDateString(String strDate, String strformat) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		try {
			date = format.parse(strDate);
			Calendar cal = Calendar.getInstance();
			Date cur_date = cal.getTime();

			int age = cur_date.getYear() - date.getYear(); // + 1;
			if (age < 1)
				age = 1;
			return age;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;
	}
	
	public static String convertUTC0TimeToLocalTime(String datetime) {
		String formattedTime = null;
		DateFormat df = new SimpleDateFormat(Constants.DATE_STRING_FORMAT, Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		try {
			Date date = df.parse(datetime);
			formattedTime = dateToString(date, Constants.DATE_STRING_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
			formattedTime = "";
		}
		return formattedTime;
	}
	
	/*
	 *  
	 * "yyyy-MM-dd'T'HH:mm:ss"  ->  "3 hours ago"
	 * 
	 */
	public static String convertDateStringToString(String datetime) {
		String formattedTime = null;
		DateFormat df = new SimpleDateFormat(Constants.DATE_STRING_FORMAT, Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		try {
			Date date = df.parse(datetime);
			double timeInterval = (System.currentTimeMillis() - date.getTime() )/ 1000;
			System.out.println("timeInterval = "+timeInterval);

			if (timeInterval <= 0) {
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_seconds_ago), 1);
			}
			else if (timeInterval < MINUTE) {
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_seconds_ago), (int)timeInterval);
			} else if (timeInterval < HOUR) {
				int minutes = (int)Math.floor((double)timeInterval/MINUTE);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_minutes_ago), minutes);
			} else if (timeInterval < DAY) {
				int hours = (int) Math.floor((double)timeInterval/HOUR);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_hours_ago), hours);
			} else if (timeInterval < WEEK) {
				int days = (int) Math.floor((double)timeInterval/DAY);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_days_ago), days);
			} else if (timeInterval < MONTH) {
				int weeks = (int) Math.floor((double)timeInterval/WEEK);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_weeks_ago), weeks);
			} else {
				int months = (int) Math.floor((double)timeInterval/MONTH);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_months_ago), months);
			}
		} catch ( Exception e) {
			e.printStackTrace();
			formattedTime = "@";
		}
		return formattedTime;
	}
	
	/*
	 *  
	 * Date  ->  "3 hours ago"
	 * 
	 */
	public static String convertDateTimeToString(Date date) {
		String formattedTime = null;
		try {
			double timeInterval = (System.currentTimeMillis() - date.getTime() )/ 1000;

			if (timeInterval < 1*MINUTE) {
				if( timeInterval == 1) {
					formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_seconds_ago), 1);
				} else {
					formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_minutes_ago), 1);
				}
			} else if (timeInterval < 2*MINUTE) {
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_minutes_ago), 1);
			} else if (timeInterval < 45*MINUTE) {
				int minutes = (int)Math.floor((double)timeInterval/MINUTE);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_minutes_ago), minutes);
			} else if (timeInterval < 90*MINUTE) {
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_hours_ago), 1);
			} else if (timeInterval < 24*HOUR) {
				int hours = (int) Math.floor((double)timeInterval/HOUR);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_hours_ago), hours);
			} else if (timeInterval < 48*HOUR) {
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_days_ago), 1);
			} else if (timeInterval < 30*DAY) {
				int days = (int) Math.floor((double)timeInterval/DAY);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_days_ago), days);
			} else if (timeInterval < 52*WEEK) {
				int weeks = (int) Math.floor((double)timeInterval/WEEK);
				formattedTime = String.format( WAApplication.getContext().getResources().getString(R.string.checkin_time_weeks_ago), weeks);
			} else {
				formattedTime = "@";
			}
		} catch ( Exception e) {
			e.printStackTrace();
			formattedTime = "@";
		}
		return formattedTime;
	}
}
