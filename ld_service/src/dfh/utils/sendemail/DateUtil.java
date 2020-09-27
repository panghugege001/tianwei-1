package dfh.utils.sendemail;


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
//	private static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
//	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
//	private static SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssssss");
//	private static SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");
//	private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
//	private static GregorianCalendar grc = new GregorianCalendar();
	
	
	public static Date getYYYY_MM_DD() throws ParseException{
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYY_MM_DD.parse(YYYY_MM_DD.format(new Date()));
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
	
	public static String getYYMMDDN() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date());
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
	
	public static String getNow(){
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(new Date());
	}

	/*public static DatabaseNow databaseNow;

	public DatabaseNow getDatabaseNow() {
		return databaseNow;
	}

	public void setDatabaseNow(DatabaseNow databaseNow) {
		DateUtil.databaseNow = databaseNow;
	}*/

	public static Timestamp convertToTimestamp(Date date) {
		if (date != null)
			return new Timestamp(date.getTime());
		return null;
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
	
	public static String fmtYYYY_MM_DD(Date date){
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYY_MM_DD.format(date);
	}

	public static String startYyyyMM(){
		Calendar currDate = Calendar.getInstance();
		Calendar lastDate = (Calendar) currDate.clone();  
		lastDate.add(Calendar.MONTH, -1);  
		SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");
		return YYYY_MM.format(lastDate.getTime());
	}
	
	public static String endYyyyMM(){
		Calendar currDate = Calendar.getInstance();
		Calendar lastDate = (Calendar) currDate.clone();  
		lastDate.add(Calendar.MONTH, -1);  
		SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");
		return YYYY_MM.format(lastDate.getTime());
	}
	
	public static String formatDateForStandard(Date date) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date);
	}

	public static Timestamp getCurrentTimestamp() {
		Calendar currDate = Calendar.getInstance();
		currDate.setTime(new Date());
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
		c.setTime(new Date());
		// c.add(10, fixedHours);
		// log.info((new StringBuilder("修正小时数:")).append(fixedHours).toString());
		String dateStr = sdf.format(c.getTime());
		// log.info((new StringBuilder("修正后时间:")).append(c.getTime().toLocaleString()).toString());
		return dateStr;
	}

	public static Date getFirstDayOfMonth(Integer month, Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
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
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, -1);
		return c.getTime();
	}

	public static Date get15MinutesAgo() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE, -15);
		return c.getTime();
	}

	public static String getOneHourAgoFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, -1);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static Date getToday() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		return c.getTime();
	}

	public static String getTodayFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static Date getTomorrow() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(6, 1);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		return c.getTime();
	}

	public static String getTomorrowFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(6, 1);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(c.getTime());
	}

	public static String getYYMMDD() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date());
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
	
	public static Date parseDateForYYYYmmDD(String dateText) {
		try {
			SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
			return YYYY_MM_DD.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
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
	
	public static String getTimecha(Timestamp start,Timestamp end){
		StringBuffer buffer=new StringBuffer();
		String result="";
		if(end!=null){
			long l=end.getTime()-start.getTime();
			Long day=l/(24*60*60*1000);
			Long hour=(l/(60*60*1000)-day*24);
			Long min=((l/(60*1000))-day*24*60-hour*60);
			Long s=(l/1000-day*24*60*60-hour*60*60-min*60);
			if(day>0){
				buffer.append(day.intValue()+"天");
			}
			if(hour>0){
				buffer.append(hour.intValue()+"小时");
			}
			buffer.append(min.intValue()+"分");
			buffer.append(s.intValue()+"秒");
			result=buffer.toString();
		}
		return result;
	}
	
	public static Integer getOvertime(Timestamp start,Timestamp end){
		Integer result=0;
		if(end!=null){
			long l=end.getTime()-start.getTime();
			Long day=l/(24*60*60*1000);
			Long hour=(l/(60*60*1000)-day*24);
			Long min=((l/(60*1000))-day*24*60-hour*60);
			Long s=(l/1000-day*24*60*60-hour*60*60-min*60);
			if(day.intValue()>0){
				return 1;
			}
			if(hour.intValue()>0){
				return 1;
			}
			if(min.intValue()>5){
				return 1;
			}
			if(min.intValue()==5&&s.intValue()>0){
				return 1;
			}
		}
		return result;
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
	
	public static Date ptStart() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfHH = new SimpleDateFormat("HH");
		Date date=new Date();
		Integer hh = Integer.parseInt(dfHH.format(new Date()));
		Date start = null;
		if (hh < 12) {
			start = formatter.parse(df.format(date) + " 12:00:00");
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.add(Calendar.DATE, -1); // 减1天
			start = cal.getTime();
		} else {
			start = formatter.parse(df.format(date) + " 12:00:00");
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			start = cal.getTime();
		}
		return start;
	}
	
	public static Date ptEnd() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfHH = new SimpleDateFormat("HH");
		Date date=new Date();
		Integer hh = Integer.parseInt(dfHH.format(date));
		Date end = null;
		if (hh < 12) {
			end = formatter.parse(df.format(date) + " 12:00:00");
			Calendar calend = Calendar.getInstance();
			calend.setTime(end);
			end = calend.getTime();
		} else {
			end = formatter.parse(df.format(date) + " 12:00:00");
			Calendar calend = Calendar.getInstance();
			calend.setTime(end);
			calend.add(Calendar.DATE, +1); // 加1天
			end = calend.getTime();
		}
		return end;
	}
	
	public static boolean nowIsMonday(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.DAY_OF_WEEK)==2?true:false;
	}
	
	
	/**
	 * 
	* @methods getchangedDate 
	* @description <p>当前时间往后推几天</p> 
	* @author erick
	* @date 2014年11月17日 上午10:46:31
	* @param dates 整数标示加几天  负数减几天
	* @return 参数说明
	* @return String 返回结果的说明
	 */
	public static String getchangedDate(int dates){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cals = Calendar.getInstance();
		cals.setTime(new Date());
		cals.add(Calendar.DAY_OF_MONTH, dates);
		String dateTime = formatter.format(cals.getTime());
		return dateTime ;
	}


	public static void main(String[] args) {
		// System.out.println(getDateFmtID());

//		System.out.println(getDate(2010, 6, 10,12).toLocaleString());
//		Timestamp timestamp = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -7*24+12);
//		Timestamp a2 = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(7*24-12));
//		System.out.println(DateUtil.formatDateForStandard(timestamp));
//		System.out.println(DateUtil.formatDateForStandard(a2));
//		Double double1=4.56;
//		System.out.println(double1.intValue());
	}

}
