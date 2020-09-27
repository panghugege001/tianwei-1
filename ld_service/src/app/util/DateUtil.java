package app.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

//	private static final DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private static final DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	private DateUtil() {}
	
	public static String getCurrentDateFormat() {
		
		return getDateFormat(new Date());
	}
	
	public static String getDateFormat(Date date) {
		
		if (date == null) {
		
			return "";
		}
		DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return SDF.format(date);
	}
	public static String getDateFormatYYYY_MM_DD(Date date) {
		
		if (date == null) {
			
			return "";
		}
		DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYY_MM_DD.format(date);
	}
	
	public static String getCurrentDateFormat(String format) {
	
		return getDateFormat(new Date(), format);
	}

	public static String getDateFormat(Date date, String format) {
		
		if (date == null) {
			
			return "";
		}
		
		DateFormat sdf = new SimpleDateFormat(format);
		
		return sdf.format(date);
	}
	
	public static Date getDateFromDateStr(String dateStr) throws ParseException {
		DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return SDF.parse(dateStr);
	}
	
	public static Timestamp getCurrentTime() {
	
		Calendar currDate = Calendar.getInstance();
		currDate.setTime(new Date());
		
		return new Timestamp(currDate.getTimeInMillis());
	}
	
	public static Date getCurrentDate() {
	
		return new Date();
	}
	
	public static Date getDateByDay(String ymd) {
	
		try {
			DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
			return YYYY_MM_DD.parse(ymd);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Date getTodayByZeroHour() {
	
		return getTodayByZeroHour(new Date());
	}
	
	public static Date getTodayByZeroHour(Date date) {
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		
		return cal.getTime();
	}
	
	public static Date getWeekByFirstDay() {
		
		return getWeekByFirstDay(new Date());
	}

	public static Date getWeekByFirstDay(Date date) {
	
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		
		return c.getTime();
	}
	
	public static Date getMonthByFirstDay() {
	
		return getMonthByFirstDay(new Date());
	}
	
	public static Date getMonthByFirstDay(Date date) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		
		return c.getTime();
	}
	
	public static Date getYearByFirstDay() {
	
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		return getYearByFirstDay(year);
	}
	
	public static Date getYearByFirstDay(int year) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		
		return calendar.getTime();
	}
	
	public static Date getchangedDateyyyyMMddSSmmss(Date date , int dates){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cals = Calendar.getInstance();
		cals.setTime(date);
		cals.add(Calendar.DAY_OF_MONTH, dates);
		String dateTime = formatter.format(cals.getTime());
		try {
			return formatter.parse(dateTime) ;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String [] args) throws Exception {
		
		System.out.println(getDateFormat(getYearByFirstDay(2015)));
	}
	
}