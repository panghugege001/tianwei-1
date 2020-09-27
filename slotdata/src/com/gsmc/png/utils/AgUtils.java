package com.gsmc.png.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.gsmc.png.model.ag.AGData4OracleVO;
import com.gsmc.png.model.ag.AgData777;
import com.gsmc.png.model.ag.AgDataE68;
import com.gsmc.png.model.ag.AgDataHU;
import com.gsmc.png.model.ag.AgDataL8;
import com.gsmc.png.model.ag.AgDataLD;
import com.gsmc.png.model.ag.AgDataMZC;
import com.gsmc.png.model.ag.AgDataQLE;
import com.gsmc.png.model.ag.AgDataQY;
import com.gsmc.png.model.ag.AgDataUFA;
import com.gsmc.png.model.ag.AgDataULE;
import com.gsmc.png.model.ag.AgDataWS;
import com.gsmc.png.model.ag.AgDataZB;
import com.gsmc.png.model.ag.AgRecordResponse;

public class AgUtils {

	public static String apiUrl="http://gdc.api.agingames.com:8089";
	public static String apiUrlBetReport="http://gdc.api.agingames.com:8089/api/xmlDataActionAPI_queryBetReport?";
	public static String B20KEY = "mnqvtug0";
	
	
	public static String	productId = "B20";
	public static Integer	pageSize =500;
	public static Integer	currentPage =1;
	public static Integer   delay =5;
	
	private static Logger log = Logger.getLogger(AgUtils.class);

	
	public static String getBetRecord(String platformType,Date fromTime,Date toTime,String productId,Integer pageSize,Integer currentPage){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		try {
			
			Calendar calendarFromTime=Calendar.getInstance();
			calendarFromTime.setTime(fromTime);
			calendarFromTime.add(Calendar.HOUR, -12); //减填负数
			calendarFromTime.add(Calendar.SECOND, +1); //减填负数
			
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
	
	
	public static String getBetRecordByDate(String platformType,Date fromTime,Date toTime,String productId,Integer pageSize,Integer currentPage){
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
	
	/** 获取两个时间的时间查 如1天2小时30分钟 */
	public static int getDatePoor(Date endDate, Date nowDate,int delay) {

	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
//	    long day = diff / nd;
	    // 计算差多少小时
//	    long hour = diff % nd / nh;
	    // 计算差多少分钟
//	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
//	    // long sec = diff % nd % nh % nm / ns;
	    int diffMin = (int) (((diff / nm)/delay)) ;
	    return diffMin;
	}
	
	
	//获取 api 接口 xml 类型 数据解析
	public  static void recordData(String xml,Date createtime,List<AGData4OracleVO> listData){
			
			
			Document document=DomOperator.string2Document(xml);
		    Element root =document.getRootElement();
		    Element info = root.element("Info");  
		    Integer total=Integer.valueOf(info.elementText("total"));
		    Integer pageSize=Integer.valueOf(info.elementText("pageSize"));
		    Integer currentPage=Integer.valueOf(info.elementText("currentPage"));
		
		    AgRecordResponse aResponse=new AgRecordResponse();
		    aResponse.setErrorCode(info.elementText("errorCode"));
		    aResponse.setCurrentPage(currentPage);
		    aResponse.setTotal(total);
		    aResponse.setPageSize(pageSize);
		    
			 if (StringUtil.equals(aResponse.getErrorCode(),"00000")) {
			    	
			  	    Element row = root.element("ResultSet"); 
			  	    
			  	    Iterator<Element> itRow = row.elementIterator(); 
			  	    
			  	    while (itRow.hasNext()) {  
			  	        // 获取某个子节点对象  
			  	     Element e = itRow.next();  
			  	  	
			  	    	 String billno= e.attributeValue("billNo");
				  	  	 String playerName= e.attributeValue("playerName");
				  	     String beforeCredit =e.attributeValue("beforeCredit");
				  	     String betTime=e.attributeValue("betTime");
				  	  	 String betamount= e.attributeValue("betAmount");
				  	   	 String validbetamount= e.attributeValue("validBetAmount");  
				  	     String netamount= e.attributeValue("netAmount");  
				  	  	 String platform= e.attributeValue("platformType");
				  		 String gameType= e.attributeValue("gameType");
				  		 

				  		AGData4OracleVO aRecord=createVO(e.attributeValue("playerName"));
				  		aRecord.setBillNo(billno);
				  		aRecord.setPlayerName(playerName);
				  		aRecord.setBeforeCredit(Double.parseDouble(beforeCredit));
				  		aRecord.setBetamount(Double.parseDouble(betamount));
				  		aRecord.setValidbetamount(Double.parseDouble(validbetamount));
				  		aRecord.setNetamount(Double.parseDouble(netamount));
				  		aRecord.setJackpot(0.0);
				  		aRecord.setBetTime(DateUtil.parseDateForStandard(betTime));
				  		aRecord.setLastupdate(DateUtil.parseDateForStandard(betTime));
				  		aRecord.setCreatetime(createtime);
				  		aRecord.setGameType(gameType);
				  		aRecord.setPlatform(platform);
			  		    listData.add(aRecord);

				}
			  	    
			 }else {
//					log.info("...............AG  系统返水 错误="+AgErrorType.getErrorMsg(aResponse.getErrorCode()));
			 }
		}
		
	
	
	private static AGData4OracleVO createVO(String playername) {
		AGData4OracleVO agData4OracleVO = null;
		if(playername != null && playername.startsWith("qi_")){
			
			agData4OracleVO = new AgDataQY();
		} else if(playername != null && playername.startsWith("ei_")){
			
			agData4OracleVO = new AgDataE68();
		} else if(playername != null && playername.startsWith("li_")){
			
			agData4OracleVO = new AgDataL8();
		} else if(playername != null && playername.startsWith("gi_")){
			
			agData4OracleVO = new AgData777();
		} else if(playername != null && playername.startsWith("bi_")){
			
			agData4OracleVO = new AgDataUFA();
		} else if(playername != null && playername.startsWith("ai_")){
			
			agData4OracleVO = new AgDataULE();
		} else if(playername != null && playername.startsWith("ci_")){
			
			agData4OracleVO = new AgDataQLE();
		} else if(playername != null && playername.startsWith("di_")){
			
			agData4OracleVO = new AgDataMZC();
		} else if(playername != null && playername.startsWith("wi_")){
			
			agData4OracleVO = new AgDataWS();
		} else if(playername != null && playername.startsWith("zb_")){
			
			agData4OracleVO = new AgDataZB();
		} else if(playername != null && playername.startsWith("ti_")){
			
			agData4OracleVO = new AgDataHU();
		} else if(playername != null && playername.startsWith("ki_")){
			
			agData4OracleVO = new AgDataLD();
		} else {
			log.error("错误用户前缀：" + playername);
		}
		return agData4OracleVO;
	}


}
