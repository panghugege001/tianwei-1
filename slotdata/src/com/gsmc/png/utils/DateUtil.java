package com.gsmc.png.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class DateUtil {

	private static Logger log = Logger.getLogger(DateUtil.class);

	public static String fmtDate(String fmt, Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(fmt);
		return defaultFormat.format(date);
	}

	/**
	 * 获取几天之前/后的Calendar
	 * 
	 * @param days
	 *            如：-2表示2天之前； 0表示现在； 1表示1天之后即明天。
	 * @return
	 */
	public static Calendar getDateBeforeAfter(int days) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
		return now;
	}

	public static String getDateFmtID() {
		SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssssss");
		return idFmt.format(new Date());
	}

	public static String fmtyyyyMMdd(Date date) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		return yyyyMMdd.format(date);
	}

	public static String fmtyyyyMMddHHmmss(Date date) {
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		return yyyyMMddHHmmss.format(date);
	}

	public static String formatDateForStandard(Date date) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date);
	}

	public static String formatDateForQt(Date date) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date).replace(" ", "T");
	}

	public static String UTCStringtODefaultString(String UTCString) {
		try {
			UTCString = UTCString.replace("Z", " UTC");
			SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = utcFormat.parse(UTCString);
			return defaultFormat.format(date);
		} catch (ParseException pe) {
			pe.printStackTrace();
			return null;
		}
	}

	public static String UTCToUTC8(String time) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, -8);
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String utc8 = df2.format(c.getTime());
			return utc8;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertUTC8toUTC(String time) {

		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, -8);
			return standardFmt.format(c.getTime());
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTC8toUTC ParseException, time:" + time, e);
		}

		return null;
	}

	public static String convertUTC12toUTC(String time) {

		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, -12);
			return standardFmt.format(c.getTime());
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTC12toUTC ParseException, time:" + time, e);
		}

		return null;
	}

	public static Date convertUTCtoUTC8(String time) {

		try {
			SimpleDateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = UTC.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, 8);
			return c.getTime();
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTCtoUTC8 ParseException, time:" + time, e);
		}

		return null;
	}

	/**
	 * UTC-4转换成UTC+8
	 * 
	 * @param time
	 * @return
	 */
	public static Date convertUTCfu4toUTC8(String time) {

		try {
			SimpleDateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = UTC.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, 12);
			return c.getTime();
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTCfu4toUTC8 ParseException, time:" + time, e);
		}

		return null;
	}

	/**
	 * UTC+8转换成UTC-4
	 * 
	 * @param time
	 * @return
	 */
	public static Date convertUTC8toUTCfu4(String time) {

		try {
			SimpleDateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = UTC.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, -12);
			return c.getTime();
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTC8toUTCfu4 ParseException, time:" + time, e);
		}

		return null;
	}

	public static Date convertUTCtoUTC8SubStr(String time) {

		try {

			time = time.replace("T", " ").substring(0, 19);

			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.HOUR_OF_DAY, 8);
			return c.getTime();
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTCtoUTC8SubStr ParseException, time:" + time, e);
		}

		return null;
	}

	/**
	 * 
	 * @param time
	 *            时间
	 * @return
	 */
	public static String getStandardFmtTime(String time) {

		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = time.replace("T", " ").substring(0, 19);
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return standardFmt.format(c.getTime());
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTCtoUTC8SubStr ParseException, time:" + time, e);
		}

		return null;
	}

	public static Date parseDateForStandard(String dateText) {
		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return standardFmt.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String fmtDate(Date date) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date);
	}

	public static final Pattern P_TIME = Pattern
			.compile("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$");

	/**
	 * 
	 * @methods getchangedDate
	 * @description
	 *              <p>
	 *              当前时间往后推几天
	 *              </p>
	 * @author erick
	 * @date 2014年11月17日 上午10:46:31
	 * @param dates
	 *            整数标示加几天 负数减几天
	 * @return 参数说明
	 * @return String 返回结果的说明
	 */
	public static String getchangedDate(int dates) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cals = Calendar.getInstance();
		cals.setTime(new Date());
		cals.add(Calendar.DAY_OF_MONTH, dates);
		String dateTime = formatter.format(cals.getTime());
		return dateTime;
	}

	public static Date parseDateForYYYYmmDD(String dateText) {
		try {
			SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
			return YYYY_MM_DD.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseDateForYYYYmmDDHHSS(String dateText) {
		try {
			SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return YYYY_MM_DD.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String fmtYYYY_MM_DD(Date date) {
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYY_MM_DD.format(date);
	}

	public static String getNow() {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(new Date());
	}

	public static String transferLongToDate(String dateFormat, Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	public static String getDateTime(long date) {
		String startTime = transferLongToDate("yyyy-MM-dd'T'HH:mm:ss", date);// 8分钟之前
		SimpleDateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String tc = UTC.format(convertUTC8toUTCfu4(startTime)) + "-04:00";
		return tc;
	}

	/**
	 * 字符串时间戳
	 * 
	 * @return
	 */
	public static long getTime(String times) {
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

	/**
	 * 时间戳转date
	 * 
	 * @param time
	 * @return
	 */
	public static Date getTimeStampToDateTime(long time) {
		Date date = new Date(time);
		return date;
	}

	public static Date getUSToAMES(Date date) {
		return getHour(date, -12);
	}

	public static Date getHour(Date date, int n) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(10, n);
			return cal.getTime();
		} catch (Exception var3) {
			return null;
		}
	}

	public static String dateToYMDHMS(Date date) {
		return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String dateToStr(Date date, String pattern) {
		return dateToStr(date, pattern, Locale.getDefault());
	}

	public static String dateToStr(Date date, String pattern, Locale locale) {
		if (date == null) {
			return null;
		} else {
			DateFormat dateformat = simpleDateFormat(pattern, locale);
			return dateformat.format(date);
		}
	}

	public static SimpleDateFormat simpleDateFormat(String format, Locale locale) {
		return new SimpleDateFormat(format, locale);
	}

	public static SimpleDateFormat simpleDateFormat(String format) {
		return simpleDateFormat(format, Locale.getDefault());
	}
	
    public static Date getAMESToUS(Date date) {
        return getHour(date, 12);
    }
    
    public static String dateToYMD(Date date) {
        return dateToStr(date, "yyyy-MM-dd");
    }

	public static Date strToDate(String str) {
		if (StringUtil.isBlank(str)) {
			return null;
		} else {
			DateFormat dateformat = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date tmp = null;

			try {
				tmp = dateformat.parse(str);
			} catch (ParseException var8) {
				dateformat = simpleDateFormat("yyyy-MM-dd HH:mm");

				try {
					tmp = dateformat.parse(str);
				} catch (ParseException var7) {
					dateformat = simpleDateFormat("yyyy-MM-dd");

					try {
						tmp = dateformat.parse(str);
					} catch (ParseException var6) {
						tmp = new Date();
					}
				}
			}

			return tmp;
		}
	}

	public static void main(String[] args) {
		// System.out.println(DateUtil.getTimeStampToDateTime(1601190180000L));
	}
}
