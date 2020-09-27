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
	
	public static String getYYMMDDHHmmssSSS(Date date) {
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		return yyMMddHHmmssSSS.format(date.getTime());
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
	
	/**
	 * 时间字符串往后推几分钟
	 * @param dates 整数标示加几天  负数减几天
	 * @param date 2015/7/7
	 * @return
	 * @throws ParseException 
	 */
	public static String getchangedDateMinStr(int min,String date) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cals = Calendar.getInstance();
		cals.setTime(formatter.parse(date));
		cals.add(Calendar.MINUTE, min);
		String dateTime = formatter.format(cals.getTime());
		return dateTime ;
	}
	
	/**
	 * 
	 * @param time 时间
	 * @return
	 */
	public static String getStandardFmtTime(String time) {
		
		try {
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = time.replace("T", " ").substring(0,19);
			Date date = standardFmt.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return standardFmt.format(c.getTime());
		} catch (ParseException e) {
			log.error(Thread.currentThread().getName() + "---convertUTCtoUTC8SubStr ParseException, time:" + time, e);
		}
		
		return null;
	}
	
	public static String fmtyyMMddHHmmss(Date date) {
		SimpleDateFormat yyMMddHHmmss = new SimpleDateFormat("yyMMddHHmmss");
		return yyMMddHHmmss.format(date);
	}
	
	public static String formatMMddHH(Date date){
		SimpleDateFormat MMddHH = new SimpleDateFormat("MMddHH");
		return MMddHH.format(date);
	}
	public static String formatDateForQt(Date date){
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(date).replace(" ", "T");
	}
	//获取指定日期的上周一
	public static Date getLastWeekMonday(Date date) {    
       Date a = getMontToDate(date, -1);
       Calendar cal = Calendar.getInstance();
       cal.setTime(a);
       cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周
       cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
       
       return cal.getTime();    
    }
	private static SimpleDateFormat yyMMdd00 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static SimpleDateFormat yyMMdd23 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
	public static String fmtyyMMdd00(Date date) {
		return yyMMdd00.format(date);
	}
	public static String fmtyyMMdd23(Date date) {
		return yyMMdd23.format(date);
	}
	public static Date fmtyyMMdd00d(String date) {
		try {
			return yyMMdd00.parse(date);
		}catch (Exception e){
			return null;
		}

	}
	public static Date fmtyyMMdd23d(String date) {
		try {
			return yyMMdd23.parse(date);
		}catch (Exception e){
			return null;
		}
	}
	
	public static Timestamp toTimestamp(String format) {
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {  
		    Date date = standardFmt.parse(format);  
		    Timestamp t1 = new Timestamp(date.getTime());  
		    return t1;
		} catch (ParseException e) {  
		    e.printStackTrace();  
		}
		return null;  
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
	//获取指定日期的上周日
	public static Date  getLastWeekSunday(Date date) {
        Date a = getMontToDate(date, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        return cal.getTime();    
    }
	
	public static Date getYYYY_MM_DD() {
		try {
			SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
			return YYYY_MM_DD.parse(YYYY_MM_DD.format(new Date()));
		} catch (ParseException e) {
			throw new RuntimeException("时间转换错误",e);
		}
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
	
	public static Date parseDateForYYYYmmDD(String dateText) {
		try {
			SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
			return YYYY_MM_DD.parse(dateText);
		} catch (ParseException e) {
			try {
				SimpleDateFormat MMddyy = new SimpleDateFormat("MM/dd/yy");
				return MMddyy.parse(dateText);
			} catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static String getNow(){
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.format(new Date());
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
	
	
	public static Date fmtStandard(String s) throws ParseException{
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return standardFmt.parse(s);
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
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static String getTodayFormat() {
		Calendar c = Calendar.getInstance();
		c.setTime(databaseNow.getDatabaseNow());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
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
	
	public static Date ntStart() throws Exception{
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		Date ntStart = standardFmt.parse(YYYY_MM_DD.format(new Date())+" 00:00:00");
		return ntStart;
	}
	
	public static Date ntEnd() throws Exception{
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		Date ntEnd = standardFmt.parse(YYYY_MM_DD.format(new Date())+" 23:59:59");
		return ntEnd;
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

	public static String fmtYYYY_MM_DD(Date date){
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYY_MM_DD.format(date);
	}
	
	/**
	 * 时间字符串往后推几天
	 * @param dates 整数标示加几天  负数减几天
	 * @param date 2015/7/7
	 * @return
	 */
	public static String getchangedDateStr(int dates,String date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cals = Calendar.getInstance();
		cals.setTime(parseDateForYYYYmmDD(date));
		cals.add(Calendar.DAY_OF_MONTH, dates);
		String dateTime = formatter.format(cals.getTime());
		return dateTime ;
	}
	
	/**
	 * 根据指定日期往后推几天，整数标示加几天  负数减几天
	 * @param time 基础时间
	 * @param i 整数标示加几天  负数减几天
	 * @return
	 * */
	public static Date getDateAfter(Date time, int i) {
		if(time == null){
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.set(Calendar.DATE,c.get(Calendar.DATE) + i);
		return c.getTime();
	}
	
	public static String getDateTimeStr4MG(Date date){
		SimpleDateFormat sdf4MG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf4MG.format(date);
	}

	public static String fmtYYYY_MM_DDToStandard(String dstr) throws ParseException{
		SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat Standard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return Standard.format(YYYY_MM_DD.parse(dstr));
	}

}
