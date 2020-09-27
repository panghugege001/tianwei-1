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


	public static Date getStringFormat(String date) throws ParseException {

		if (date == null) {

			return null;
		}
		DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return SDF.parse(date);
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
}