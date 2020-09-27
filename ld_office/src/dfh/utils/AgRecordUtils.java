package dfh.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import dfh.security.EncryptionUtil;

public class AgRecordUtils {

	public static String apiUrl="http://gdc.api.agingames.com:8089";
	public static String apiUrlBetReport="http://gdc.api.agingames.com:8089/api/xmlDataActionAPI_queryBetReport?";
	public static String B20KEY = "mnqvtug0";

	public static String getBetRecord(String platformType,Date fromTime,Date toTime,String productId,Integer pageSize,Integer currentPage){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		try {
			
			Calendar calendarFromTime=Calendar.getInstance();
			calendarFromTime.setTime(fromTime);
			calendarFromTime.add(Calendar.HOUR, -12); //减填负数
			
			Calendar calendarToTime=Calendar.getInstance();
			calendarToTime.setTime(toTime);
			calendarToTime.add(Calendar.HOUR, -12); //减填负数
	
			String startTime=sf.format(calendarFromTime.getTime()).replace(" ", "_");
			String endTime=sf.format(calendarToTime.getTime()).replace(" ", "_");
			String params=startTime+endTime+productId+platformType+pageSize+B20KEY;
			String key=EncryptionUtil.encryptPassword(params);
			String url=apiUrlBetReport+"platformType="+platformType+"&fromTime="+startTime+"&key="+key+"&toTime="+endTime+"&productId="+productId+"&pageSize="+pageSize+"&page="+currentPage;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		
		return result;
	}

}
