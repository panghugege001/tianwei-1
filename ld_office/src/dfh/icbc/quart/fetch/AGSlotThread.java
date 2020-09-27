package dfh.icbc.quart.fetch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.model.AgDataRecord;
import dfh.model.enums.AgErrorType;
import dfh.model.notdb.AgRecordResponse;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.AgRecordUtils;
import dfh.utils.DomOperator;
import dfh.utils.StringUtil;

public class AGSlotThread extends Thread{
	
	private IGetdateService getdateService;
	private String	productId = "B20";
	private  String	platformType = null;
	private Integer	pageSize =1000;
	private Integer	currentPage =1;
	private static Logger log = Logger.getLogger(AGSlotThread.class);

	
	public AGSlotThread(IGetdateService getdateService,String platformType) {
		this.getdateService = getdateService;
		this.platformType = platformType;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		executeQuartz();
	}
	
	
	public void executeQuartz() {
		 log.info("*****正在  获取AG "+platformType+" 投注额 洗码！******\n");
		try{
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		    long nd = 1000 * 24 * 60 * 60; 
		    long nh = 1000 * 60 * 60;
			Date now=new Date();	
			
			Calendar calendarLastTime=Calendar.getInstance();
			
			calendarLastTime.setTime(sf.parse(getdateService.getAgSlotLastTime(platformType)));
			log.info("*****获取AG "+platformType+" 最后更新时间= "+calendarLastTime.getTime()+"******");
			Calendar calendarToTime=Calendar.getInstance();
			calendarToTime.setTime(calendarLastTime.getTime());			

			    
			    long diff = now.getTime() - calendarLastTime.getTime().getTime();  // 获得两个时间的毫秒时间差异
//			    int hour = (int) (diff % nd / nh);    // 获取 相差小时 根据最后/最新 时间  来收录 数据
			    int hour = (int) (diff /(3600*1000));    // 获取 相差小时 根据最后/最新 时间  来收录 数据
				for (int i = 0; i < hour; i++) {
					
                   if (i!=0&&calendarLastTime.get(Calendar.SECOND)!=0) 
                   	 calendarLastTime.add(Calendar.SECOND, -1);  
                   
					     calendarToTime.add(Calendar.HOUR, +1 );    //获取 最后时间 +1 小时 为 查询到最后时间  （1）
					     
					if (calendarLastTime.get(Calendar.SECOND)!=0) 
						 calendarLastTime.add(Calendar.SECOND,+1);  //获取 最后时间  +1 分钟 整合成 正数 00:00:00
					
					if (calendarToTime.get(Calendar.SECOND)==0) 
						 calendarToTime.add(Calendar.SECOND,-1);      //首次  获取 最后时间  -1 分钟 默认为 当天查询到最后时间 00:59:59  （1）
					
			        if (i!=0) 
			        	 calendarLastTime.add(Calendar.HOUR, +1);  //获取 相差小时大于2 添加开始时间  01:00:00
			         
			         String xml=AgRecordUtils.getBetRecord(platformType,calendarLastTime.getTime(), calendarToTime.getTime(), productId, pageSize,currentPage);
			         AGSlotThread.getData(xml, calendarLastTime.getTime(),calendarToTime.getTime(),productId,platformType,pageSize,currentPage,getdateService);
					 log.info("*****"+sf.format(calendarLastTime.getTime())+" to "+sf.format(calendarToTime.getTime())+" 获取AG "+platformType+" 投注额成功！******");
			    }
				
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取AG "+platformType+" API 接口 错误 /发生错误!!!");
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	 //获取数据保存
		public static  AgRecordResponse getData(String xml ,Date fromTime,Date toTime,String productId,String platformType,Integer pageSize,Integer currentPage,IGetdateService getdateService){

			Map<String,AgDataRecord>data=new HashMap<String,AgDataRecord>();  
			
			Document document=DomOperator.string2Document(xml);
		    Element root =document.getRootElement();
		    Element info = root.element("Info");  
		    Integer total=Integer.valueOf(info.elementText("total"));          //总数据
		    Integer pageSize2=Integer.valueOf(info.elementText("pageSize"));   //当前页
		    Integer nextPage= total/pageSize2 +1;                              //页面数
		    
		    Date createtime=new Date();
		    
		    try {
				
		    	for (int i = 0; i < nextPage; i++) {
		    		if (i==0) 
		    			recordData(xml,createtime,fromTime,toTime,data,platformType);      
		    		else 
		    			recordData(AgRecordUtils.getBetRecord(platformType, fromTime, toTime, productId, pageSize,i+1),createtime,fromTime, toTime,data,platformType);
		        }
		    
	  	 	  getdateService.processInsertAgSlotNewData(data,toTime,platformType);
	  	 	  
		    } catch (Exception e) {
		    	log.error(" 获取AG "+platformType+" API 接口 错误2 /发生错误2!!!");			
		    }
		    return null;
	    }
		
		 //获取补录数据保存
		public static  Map<String, AgDataRecord> getReRecordData(String xml ,Date fromTime,Date toTime,String productId,String platformType,Integer pageSize,Integer currentPage){

			Map<String,AgDataRecord>data=new HashMap<String,AgDataRecord>();  
			Document document=DomOperator.string2Document(xml);
		    Element root =document.getRootElement();
		    Element info = root.element("Info");  
		    Integer total=Integer.valueOf(info.elementText("total"));          //总数据
		    Integer pageSize2=Integer.valueOf(info.elementText("pageSize"));   //当前页
		    Integer nextPage= total/pageSize2 +1;                              //页面数
		    
		    Date createtime=new Date();
		    	for (int i = 0; i < nextPage; i++) {
		    		if (i==0) 
		    			recordData(xml,createtime,fromTime,toTime,data,platformType);      
		    		else 
		    			recordData(AgRecordUtils.getBetRecord(platformType, fromTime, toTime, productId, pageSize,i+1),createtime,fromTime, toTime,data,platformType);
		        }
		    return data;
	  }
		
	

		//获取 api 接口 xml 类型 数据解析
		public  static void recordData(String xml,Date createtime,Date fromTime,Date toTime,Map<String,AgDataRecord>data,String platformType){
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

		    
			 if (StringUtil.equals(aResponse.getErrorCode(), AgErrorType.SUCCESS.getErrormsg())) {
			    	
			  	    Element row = root.element("ResultSet"); 
			  	    
			  	    Iterator<Element> itRow = row.elementIterator(); 
			  	     String playerName= "";
			    	 String satrtWith= "";
			  	    while (itRow.hasNext()) {  
			  	        // 获取某个子节点对象  
			  	     Element e = itRow.next();  
			  	     if (StringUtil.equals(platformType, "MG")) {
			  	    	 satrtWith="b20_ki_";
						 playerName= e.attributeValue("playerName").substring(7);
					 }else {
						 satrtWith="ki_";
						 playerName= e.attributeValue("playerName").substring(3);
					}
			  	     
			  	     if (e.attributeValue("playerName").startsWith(satrtWith)) {
			  	  	 String betamount= e.attributeValue("betAmount");
			  	   	 String validbetamount= e.attributeValue("validBetAmount");  
			  	     String netamount= e.attributeValue("netAmount");  
			  	  	 String platform= e.attributeValue("platformType");
							if (data.get(playerName) == null) {
								AgDataRecord aRecord=new AgDataRecord(playerName, 1, Double.parseDouble(betamount), Double.parseDouble(validbetamount), -1*Double.parseDouble(netamount), 0.0, platform,createtime,fromTime, toTime);
								data.put(playerName, aRecord);
							} else {
								AgDataRecord aRecord=data.get(playerName);
								aRecord.setBetamount(aRecord.getBetamount()+Double.parseDouble(betamount));
								aRecord.setBetcount(aRecord.getBetcount()+1);
								aRecord.setValidbetamount(aRecord.getValidbetamount()+Double.parseDouble(validbetamount));
								aRecord.setNetamount(aRecord.getNetamount()+(-1*Double.parseDouble(netamount)));
								data.put(playerName, aRecord);
							}
					} else {
						continue;
					}
				}
			  	    
			 }else {
					log.info("...............AG 系统返水 错误="+AgErrorType.getErrorMsg(aResponse.getErrorCode()));
			 }
		}
	
	public  IGetdateService getGetdateService() {
		return getdateService;
	}

	public  void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
	
}
