package com.gsmc.png.quart.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.gsmc.png.model.ag.AGData4OracleVO;
import com.gsmc.png.model.ag.AgRecordResponse;
import com.gsmc.png.service.interfaces.IAGDataProcessorService;
import com.gsmc.png.utils.AgUtils;
import com.gsmc.png.utils.DateUtil;
import com.gsmc.png.utils.DomOperator;


public class AGSlotThread extends Thread{
	
	private IAGDataProcessorService iagDataProcessorService;
	
	private String	platformType = null;
	
	private static Logger log = Logger.getLogger(AGSlotThread.class);

	
	public AGSlotThread(IAGDataProcessorService iagDataProcessorService,String platformType) {
		this.iagDataProcessorService = iagDataProcessorService;
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

			Date now=new Date();	
			
			Calendar calendarLastTime=Calendar.getInstance();
			
			calendarLastTime.setTime(sf.parse(iagDataProcessorService.getAgLastTime(platformType)));
			log.info("*****获取AG "+platformType+" 最后更新时间= "+calendarLastTime.getTime()+"******");
			Calendar calendarToTime=Calendar.getInstance();
			calendarToTime.setTime(calendarLastTime.getTime());			


			    int times = AgUtils.getDatePoor(now,calendarLastTime.getTime(),AgUtils.delay);
			    
				for (int i = 0; i < times; i++) {
                       calendarToTime.add(Calendar.MINUTE, +5 ); 
   
                       if (i!=0) 
  			        	     calendarLastTime.add(Calendar.MINUTE, +5);  //获取 相差小时大于2 添加开始时间  01:00:00
                       
			         String xml=AgUtils.getBetRecord(platformType,calendarLastTime.getTime(), calendarToTime.getTime(), AgUtils.productId, AgUtils.pageSize,AgUtils.currentPage);
			         AGSlotThread.getData(xml, calendarLastTime.getTime(),calendarToTime.getTime(),AgUtils.productId,platformType,AgUtils.pageSize,AgUtils.currentPage,iagDataProcessorService);
					 log.info("*****"+sf.format(calendarLastTime.getTime())+" to "+sf.format(calendarToTime.getTime())+" 获取AG "+platformType+" 投注额成功！******");
			    }
				
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取AG "+platformType+" API 接口 错误 /发生错误!!!");
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	 //获取数据保存
	public static  AgRecordResponse getData(String xml ,Date fromTime,Date toTime,String productId,String platformType,Integer pageSize,Integer currentPage,IAGDataProcessorService iagDataProcessorService){

			List<AGData4OracleVO>listData=new ArrayList<AGData4OracleVO>();
			
			
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
		    			AgUtils.recordData(xml,createtime,listData);      
		    		else 
		    			AgUtils.recordData(AgUtils.getBetRecord(platformType, fromTime, toTime, productId, pageSize,i+1),createtime,listData);
		        }
		    	
		    	iagDataProcessorService.processInsertAgData(listData,toTime,platformType);
	  	 	  
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	log.error(" 获取AG "+platformType+" API 接口 错误2 /发生错误2!!!");			
		    }
		    return null;
	}
		
	
	

	/**
	 * 获取游戏平台 最后时间 前半小时检查数据
	 * @param iagDataProcessorService
	 * @return
	 */
	public static List<AGData4OracleVO> checkBefor30Minit(IAGDataProcessorService iagDataProcessorService ) {

	
		List<AGData4OracleVO> list = new ArrayList<AGData4OracleVO>();

		try {

			String beforeTime = iagDataProcessorService.getAgLastTime("AGIN");
			Calendar calendarTime = Calendar.getInstance();
			calendarTime.setTime(DateUtil.parseDateForStandard(beforeTime));
			calendarTime.set(Calendar.SECOND, -60 * 55);
			Date startTime = calendarTime.getTime();
			calendarTime.set(Calendar.SECOND, +60 * 40);
			Date endTime = calendarTime.getTime();

			String xml = AgUtils.getBetRecordByDate("AGIN", startTime, endTime, AgUtils.productId, AgUtils.pageSize,AgUtils.currentPage);
			list = AGSlotThread.getReRecordData(xml, startTime, endTime, AgUtils.productId, "AGIN", AgUtils.pageSize, AgUtils.currentPage);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("*************checkBefor30Minit error ********");
			return null;
		}

		return list;
	}
	
	// 获取补录数据保存
	public static List<AGData4OracleVO> getReRecordData(String xml, Date fromTime, Date toTime, String productId,String platformType, Integer pageSize, Integer currentPage) {

		List<AGData4OracleVO> list = new ArrayList<AGData4OracleVO>();

		Document document = DomOperator.string2Document(xml);
		Element root = document.getRootElement();
		Element info = root.element("Info");
		Integer total = Integer.valueOf(info.elementText("total")); // 总数据
		Integer pageSize2 = Integer.valueOf(info.elementText("pageSize")); // 当前页
		Integer nextPage = total / pageSize2 + 1; // 页面数

		Date createtime = new Date();
		for (int i = 0; i < nextPage; i++) {
			if (i == 0)
				AgUtils.recordData(xml, createtime, list);
			else {
				try {
					AgUtils.recordData(AgUtils.getBetRecordByDate(platformType, fromTime, toTime, productId, pageSize, i + 1),createtime, list);
				} catch (Exception e) {
					log.info("超时在继续进行    页数" + (i + 1));
					AgUtils.recordData(AgUtils.getBetRecordByDate(platformType, fromTime, toTime, productId, pageSize, i + 1),createtime, list);
				}

			}
		}

		return list;
	}


	

	public IAGDataProcessorService getIagDataProcessorService() {
		return iagDataProcessorService;
	}

	public void setIagDataProcessorService(IAGDataProcessorService iagDataProcessorService) {
		this.iagDataProcessorService = iagDataProcessorService;
	}


	
}
