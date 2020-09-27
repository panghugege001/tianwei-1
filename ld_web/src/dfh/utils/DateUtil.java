package dfh.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {

	private static Log log = LogFactory.getLog(DateUtil.class);
//	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//	private static SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private static SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd");
//	private static SimpleDateFormat yyyy_MM_dd_HH = new SimpleDateFormat("yyyy-MM-dd HH");
//	private static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
//	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
//	private static SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssssss");
//	private static GregorianCalendar grc = new GregorianCalendar();
private static GregorianCalendar grc = new GregorianCalendar();


	public static Date getDate(int year, int month, int day, int hour) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, --month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}


	public static DatabaseNow databaseNow;

	public DatabaseNow getDatabaseNow() {
		return databaseNow;
	}

	public void setDatabaseNow(DatabaseNow databaseNow) {
		DateUtil.databaseNow = databaseNow;
	}
	
	public static String getYYMMDDN() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date());
	}

	public static Timestamp convertToTimestamp(Date date) {
		if (date != null)
			return new Timestamp(date.getTime());
		return null;
	}
	public static Timestamp date2Timestampyyyy_MM_dd_HH(Date date,int hour) {
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c.getTimeInMillis());
	}
	
	public static String fmtyyyy_MM_dd_HH(Date date){
		SimpleDateFormat yyyy_MM_dd_HH = new SimpleDateFormat("yyyy-MM-dd HH");
		return yyyy_MM_dd_HH.format(date);
	}
	
	public static String fmtyyyy_MM_d(Date date){
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd");
		return yyyy_MM_d.format(date);
	}
	
	public static Date fmtyyyy_MM_d(String s) throws ParseException{
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd");
		return yyyy_MM_d.parse(s);
	}

	public static String fmtyyyyMMddHHmmss(Date date) {
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		return yyyyMMddHHmmss.format(date);
	}

	public static String fmtDateForBetRecods(Date date) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date);
	}

	public static String fmtyyyyMMdd(Date date) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		return yyyyMMdd.format(date);
	}

	public static String formatDateForStandard(Date date) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date);
	}

	public static Timestamp getCurrentTimestamp() {
		Calendar currDate = Calendar.getInstance();
		currDate.setTime(databaseNow.getDatabaseNow());
		return new Timestamp(currDate.getTimeInMillis());
	}

	public static Timestamp getDate(Date date, int fixedHours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// c.add(10, fixedHours);
		// log.info((new StringBuilder("修正小时数:")).append(fixedHours).toString());
		// String dateStr = sdf.format(c.getTime());
		// log.info((new StringBuilder("修正后时间:")).append(c.getTime().toLocaleString()).toString());
		return new Timestamp(c.getTimeInMillis());
	}

	public static String getDateFormat(String format, int fixedHours) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		// c.add(10, fixedHours);
		// log.info((new StringBuilder("修正小时数:")).append(fixedHours).toString());
		String dateStr = sdf.format(c.getTime());
		// log.info((new StringBuilder("修正后时间:")).append(c.getTime().toLocaleString()).toString());
		return dateStr;
	}

	public static Date getFirstDayOfMonth(Integer month, Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(databaseNow.getDatabaseNow());
		month = Integer.valueOf(month != null && month.intValue() >= 0 && month.intValue() <= 11 ? month.intValue() : calendar.get(2));
		year = Integer.valueOf(year != null && year.intValue() >= 0 ? year.intValue() : calendar.get(1));
		calendar.set(1, year.intValue());
		calendar.set(2, month.intValue());
		calendar.set(5, 1);
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}
	//获取当前月第一天 stanly 2017-40-25
	public static Date getStartTime(){

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);
		//String first = standardFmt.format(c.getTime());
		return c.getTime();
	}
	//获取当前月最后一天 stanly 2017-40-25
	public static Date getEndTime(){

		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}
	public static Date getLongAfter() {
		Date date = new Date(0);
		date.setYear(1000);
		return date;
	}

	public static Date getLongAgo() {
		return new Date(0);
	}

	public static String getDateFmtID() {
		SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssssss");
		return idFmt.format(new Date());
	}

	public static Date getOneHourAgo() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.add(Calendar.HOUR_OF_DAY, -1);
		return c.getTime();
	}

	public static Date get15MinutesAgo() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.add(Calendar.MINUTE, -15);
		return c.getTime();
	}

	public static String getOneHourAgoFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.add(Calendar.HOUR_OF_DAY, -1);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static Date getToday() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		return c.getTime();
	}

	public static String getTodayFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static Date getTomorrow() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.add(6, 1);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		return c.getTime();
	}

	public static String getTomorrowFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.add(6, 1);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static String getYYMMDD() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(databaseNow.getDatabaseNow());
	}

	public static Timestamp now() {
		return getCurrentTimestamp();
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
	
	public static Date fmtStandard(String s) throws ParseException{
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.parse(s);
	}
	public static String getDay(Date date) {
		grc.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(date);
	}

	/**
	 * 
	 * @param day
	 *            相减天数可以是负数
	 * @return
	 */
	public static String getMontHreduce(Date date, Integer day) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		grc.add(Calendar.DAY_OF_MONTH, day);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(grc.getTime());
	}

	/**
	 * 
	 * @param day
	 *            相减秒数可以是负数
	 * @return
	 */
	public static Date getMontSecond(Date date, Integer second) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		grc.add(Calendar.SECOND, second);
		return grc.getTime();
	}

	/**
	 * 
	 * @param day
	 *            相减天数可以是负数
	 * @return
	 */
	public static Date getMontToDate(Date date, Integer day) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		grc.add(Calendar.DAY_OF_MONTH, day);
		return new Timestamp(grc.getTimeInMillis());
	}

	/**
	 * 
	 * @param year
	 * 
	 * @return
	 */
	public static String getYear(Date date) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(date);
	}

	/**
	 * @return
	 */
	public static String getMonth(Date date) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat("M");
		return sdf.format(date);
	}
	
	public static Date getDateBegin(Date date){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		
		return c.getTime();
	}
	
	public static Date getDateEnd(Date date){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(11, 23);
		c.set(12, 59);
		c.set(13, 59);
		
		return c.getTime();
	}

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(DateUtil.fmtDateForBetRecods(DateUtil.getDateBegin(date)));
		System.out.println(DateUtil.fmtDateForBetRecods(DateUtil.getDateEnd(date)));


//		System.out.println(getDate(2010, 6, 10,12).toLocaleString());
		
		
		
	}

}
