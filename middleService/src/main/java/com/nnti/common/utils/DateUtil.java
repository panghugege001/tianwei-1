package com.nnti.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String YYMMDD = "yyMMdd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMMDDHH = "yyyyMMddHH";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYMMDDHHMMSS = "yyMMddHHmmss";

	public static String getYYMMDDHHmmssSSS(Date date) {
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		return yyMMddHHmmssSSS.format(date.getTime());
	}
	
	public static String format(Date date) {

		return format(YYYY_MM_DD_HH_MM_SS, date);
	}

	public static String getDateID() {
		SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return idFmt.format(new Date());
	}
	
	public static String format(String format, Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date);
	}

	public static Date parse(String date) {

		return parse(YYYY_MM_DD_HH_MM_SS, date);
	}

	public static Date parse(String format, String date) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {

			return sdf.parse(date);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static Timestamp getCurrentTimestamp() {

		return new Timestamp(System.currentTimeMillis());
	}

	public static Date getCurrentDate() {

		return new Date();
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
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static Date getTodayByEndHour() {

		return getTodayByEndHour(new Date());
	}

	public static Date getTodayByEndHour(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static Date getWeekByFirstDay() {

		return getWeekByFirstDay(new Date());
	}

	public static Date getWeekByFirstDay(Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

		return calendar.getTime();
	}

	// 获取指定数值的前几天或者后几天，date为负数，则日期相减，date为正数，则日期相加
	public static Date getDateByDateNumber(int date) {

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
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
		c.set(Calendar.MILLISECOND, 0);

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

	public static String getTimecha(Date start, Date end) {

		StringBuffer buffer = new StringBuffer();

		if (start != null && end != null) {

			long l = end.getTime() - start.getTime();

			Long day = l / (24 * 60 * 60 * 1000);
			Long hour = (l / (60 * 60 * 1000) - day * 24);
			Long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			Long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			if (day > 0) {

				buffer.append(day.intValue() + "天");
			}

			if (hour > 0) {

				buffer.append(hour.intValue() + "小时");
			}

			buffer.append(min.intValue() + "分");
			buffer.append(s.intValue() + "秒");
		}

		return buffer.toString();
	}

	public static Integer getOvertime(Date start, Date end, Integer flag) {

		Integer result = 0;

		if (start != null && end != null) {

			long l = end.getTime() - start.getTime();

			Long day = l / (24 * 60 * 60 * 1000);
			Long hour = (l / (60 * 60 * 1000) - day * 24);
			Long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			Long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			if (day.intValue() > 0) {

				return 1;
			}

			if (hour.intValue() > 0) {

				return 1;
			}

			if (min.intValue() > 5) {

				return 1;
			}

			if (min.intValue() == 5 && s.intValue() > 0) {

				return 1;
			}

			if (min.intValue() >= 1 && s.intValue() > 0 && flag == 0) {

				return 1;
			}
		}

		return result;
	}
	public static Object convertUTC8toUTC(String time) {
		
		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, -8);
			return standardFmt.format(c.getTime());
		} catch (ParseException e) {
			
		}
		return null;
	}
	
	//时区转换
	public static Object convertUTCXtoUTC(String time,int x) {
		
		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, -x);
			return standardFmt.format(c.getTime());
		} catch (ParseException e) {
			
		}
		return null;
	}
	
	public static String getYYMMDDHHmmssSSS4TransferNo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 5);
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");
		return yyMMddHHmmssSSS.format(calendar.getTime());
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public static long getTime(String times){
		Date d = new Date();
		long timeStemp = 0;
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = sf.parse(times);// 日期转换为时间戳
		} catch (ParseException e) {
			 e.printStackTrace();
		}
		timeStemp = d.getTime();
		return timeStemp;
	}
	
	public static String fmtyyyyMMdd(Date date) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		return yyyyMMdd.format(date);
	}
	
	public static void main(String[] args) throws Exception {

//		System.out.println(DateUtil.getYearByFirstDay(2018));
		System.out.println(getTime("2020-09-27 16:30:00"));
	}


}