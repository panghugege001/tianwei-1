package dfh.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {	

	private static SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	private static SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	public static String getYYMMDDHHmmssSSS(Date date) {
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		return yyMMddHHmmssSSS.format(date.getTime());
	}
	
	public static String fmtyyMMddHHmmss(Date date) {
		SimpleDateFormat yyMMddHHmmss = new SimpleDateFormat("yyMMddHHmmss");
		return yyMMddHHmmss.format(date);
	}

	public static String yyyyMM(Date date) {
		return yyyyMM.format(date);
	}

	public static String yyyy(Date date) {
		return yyyy.format(date);
	}

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

	public static String getWhichWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
	}

	public static Date parse_YYYY_MM_DD(String date) throws ParseException {
		return YYYY_MM_DD.parse(date);
	}

	//获取当前月第一天 stanly 2017-40-25
	public static Date getStartTime() {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);
		//String first = standardFmt.format(c.getTime());
		return c.getTime();
	}

	//获取当前月第一天 stanly 2017-40-25
	public static Date getDateStartOfMonth(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);
		//String first = standardFmt.format(c.getTime());
		return c.getTime();
	}

	//获取当前月最后一天 stanly 2017-40-25
	public static Date getDateEndOfMonth(Date date) {

		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}

	//获取当前月最后一天 stanly 2017-40-25
	public static Date getEndTime() {

		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}

	public static DatabaseNow databaseNow;

	public DatabaseNow getDatabaseNow() {
		return databaseNow;
	}

	public void setDatabaseNow(DatabaseNow databaseNow) {
		DateUtil.databaseNow = databaseNow;
	}

	public static Timestamp convertToTimestamp(Date date) {
		if (date != null)
			return new Timestamp(date.getTime());
		return null;
	}

	public static Timestamp date2Timestampyyyy_MM_dd_HH(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c.getTimeInMillis());
	}

	public static String fmtyyyy_MM_dd_HH(Date date) {
		SimpleDateFormat yyyy_MM_dd_HH = new SimpleDateFormat("yyyy-MM-dd HH");
		return yyyy_MM_dd_HH.format(date);
	}

	public static String fmtyyyy_MM_d(Date date) {
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd");
		return yyyy_MM_d.format(date);
	}

	public static Date getYesterday() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.add(Calendar.DATE, -1);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		return c.getTime();
	}

	public static Date fmtyyyy_MM_d(String s) throws ParseException {
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd");
		return yyyy_MM_d.parse(s);
	}

	public static String getYYMMDDN() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date());
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

	public static Date getBeginOfDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
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

	/**
	 * @param day 相减天数可以是负数
	 * @return
	 */
	public static String getMontHreduce(Date date, Integer day) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		grc.add(Calendar.DAY_OF_MONTH, day);
		return standardFmt.format(grc.getTime());
	}

	/**
	 * @param day 相减秒数可以是负数
	 * @return
	 */
	public static Date getMontSecond(Date date, Integer second) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		grc.add(Calendar.SECOND, second);
		return grc.getTime();
	}

	/**
	 * @param day 相减天数可以是负数
	 * @return
	 */
	public static Date getMontToDate(Date date, Integer day) {
		GregorianCalendar grc = new GregorianCalendar();
		grc.setTime(date);
		grc.add(Calendar.DAY_OF_MONTH, day);
		return new Timestamp(grc.getTimeInMillis());
	}

	public static Date fmtStandard(String s) throws ParseException {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.parse(s);
	}

	/**
	 * @param year
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

	/**
	 * 获得今天零点
	 *
	 * @return Date
	 */
	public static Date getTodayZeroHour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}

	public static Date getTodayEndHour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		return cal.getTime();
	}

	/**
	 * 获得指定日期所在当月第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getStartDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		date = c.getTime();
		return date;
	}
	public static Date processDayStart(Date date) throws Exception{ 
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		Date ntStart = standardFmt.parse(YYYY_MM_DD.format(date)+" 00:00:00");
		return ntStart;
	}
	
	public static Date processDayEnd(Date date) throws Exception{
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		Date ntEnd = standardFmt.parse(YYYY_MM_DD.format(date)+" 23:59:59");
		return ntEnd;
	}
	/**
	 * 取得当前日期所在周的第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		return c.getTime();
	}

	/**
	 * 获取某年第一天日期
	 *
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	public static Date getchangedDate(int dates) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cals = Calendar.getInstance();
		cals.setTime(new Date());
		cals.add(Calendar.DAY_OF_MONTH, dates);
		String dateTime = formatter.format(cals.getTime());
		try {
			return fmtyyyy_MM_d(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getDateEnd(Date date) {
		Calendar cals = Calendar.getInstance();
		cals.setTime(date);
		cals.add(Calendar.DAY_OF_MONTH, 1);
		cals.set(Calendar.HOUR_OF_DAY, 0);
		cals.set(Calendar.MINUTE, 0);
		cals.set(Calendar.SECOND, 0);
		date = cals.getTime();
		return date;
	}

	public static Date getDateStart(Date date) {
		Calendar cals = Calendar.getInstance();
		cals.setTime(date);
		cals.add(Calendar.DAY_OF_MONTH, 0);
		cals.set(Calendar.HOUR_OF_DAY, 0);
		cals.set(Calendar.MINUTE, 0);
		cals.set(Calendar.SECOND, 0);
		date = cals.getTime();
		return date;
	}

	public static String getYYMMDDHHmmssSSS() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 5);
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");
		return yyMMddHHmmssSSS.format(calendar.getTime());
	}

	public static String getYYMMDDHHmmssSSS4TransferNo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 5);
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");
		return yyMMddHHmmssSSS.format(calendar.getTime());
	}


	/**
	 * 获取z天前的日期
	 *
	 * @param z
	 * @return
	 * @throws ParseException
	 */
	public static String getbeforeDay(int z) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -z);
		Date monday = c.getTime();
		String preMonday = sdf.format(monday);
		return preMonday;
	}

	public static String getNow(){
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(new Date());
	}
	

	public static String getOneMonthAgoFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static String getDateEncodeStr(Date date) {
		String str = fmtDateForBetRecods(date);
		return str.subSequence(0, 10) + "%20" + str.subSequence(11, str.length());
	}

	/**
	 * 根据指定日期往后推几天，整数标示加几天  负数减几天
	 *
	 * @param time 基础时间
	 * @param i    整数标示加几天  负数减几天
	 * @return
	 */
	public static Date getDateAfter(Date time, int i) {
		if (time == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + i);
		return c.getTime();
	}

	public static String fmtYYYY_MM_DD(Date date) {
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYY_MM_DD.format(date);
	}

	public static Date ntStart() throws Exception {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		Date ntStart = standardFmt.parse(YYYY_MM_DD.format(new Date()) + " 00:00:00");
		return ntStart;
	}

	public static Date ntEnd() throws Exception {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		Date ntEnd = standardFmt.parse(YYYY_MM_DD.format(new Date()) + " 23:59:59");
		return ntEnd;
	}

	public static Date parseDateForYYYYMMDD(String dateText) {
		try {
			SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd");
			return yyyy_MM_d.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getDateBegin(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);

		return c.getTime();
	}

	public static Date getDateDone(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(11, 23);
		c.set(12, 59);
		c.set(13, 59);

		return c.getTime();
	}
	public static Date getDate(int i) {
		Calendar cals = Calendar.getInstance();
		cals.setTime(new Date());
		cals.add(Calendar.DAY_OF_MONTH, i);
		cals.set(Calendar.HOUR_OF_DAY, 0);
		cals.set(Calendar.MINUTE, 0);
		cals.set(Calendar.SECOND, 0);
		return cals.getTime();
	}

	public static void main(String[] args) {
		System.out.println(getDate(1));
	}
}
