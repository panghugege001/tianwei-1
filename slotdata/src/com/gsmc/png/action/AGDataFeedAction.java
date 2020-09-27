package com.gsmc.png.action;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gsmc.png.model.ag.AGData4OracleVO;
import com.gsmc.png.quart.thread.AGSlotThread;
import com.gsmc.png.service.interfaces.IAGDataProcessorService;
import com.gsmc.png.utils.AgUtils;
import com.gsmc.png.utils.DateUtil;
import com.gsmc.png.utils.PNGMsgBuffer;


public class AGDataFeedAction extends SubActionSupport {
	
	private static final long serialVersionUID = -1794845349102668863L;
	private static Logger log = Logger.getLogger(AGDataFeedAction.class);
	private IAGDataProcessorService agDataProcessorService;
	private HttpServletRequest req;
	
	
	public void pngDataFeed(){
		
		try {
			
			req = this.getRequest();
			String ip = getIp(req);
			if(!"54.64.181.193".equals(ip)){
				log.warn("Unknown IP Access:" + ip);
				Gson200("success");
				return;
			}
			
			BufferedReader br = req.getReader();
			String line = "";
			StringBuilder sb = new StringBuilder();
			
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			
			PNGMsgBuffer.pngMsgBuffer.offer(sb.toString());
			
			log.info("pngDataFeed offer success! buffer size:" + PNGMsgBuffer.pngMsgBuffer.size());
			
			Gson200("success");
		} catch (Exception e) {
			log.error("pngDataFeed Exception:", e);
			Gson500("server error");
		}
		
	}
	
	/**
	 * 根据玩家时间获取流水
	 * */
	public void getPlayerBetsByDate(){
		
		Map<String,Object> result = null;
		try {
			req = this.getRequest();
			String timeStart = req.getParameter("timeStart");
			String timeEnd = req.getParameter("timeEnd");
			String loginname = req.getParameter("loginname");
			
			log.info("getPlayerBetsByDate timeStart:" + timeStart + " timeEnd:" + timeEnd + " loginname:" + loginname);
			
			result = this.agDataProcessorService.getPlayerBetsByDate(timeStart, timeEnd, loginname);
		} catch (Exception e) {
			
			result = new HashMap<String, Object>();
			result.put("success", false);
			result.put("message", "查询发生异常：" + e.getMessage());
			log.error("查询流水发生异常：", e);
		}
		
		Gson200(result);
	}
	
	
	/**
	 * 根据玩家时间获取流水
	 * */
	public void getRepairData(){
		
		Map<String,Object> result = null;
		req = this.getRequest();
		String timeStart = req.getParameter("timeStart");
		String errormsg=null;
		try {
			
			List<AGData4OracleVO>betsList=organizedata(timeStart);
			boolean flag = true ;
			List<AGData4OracleVO> ptDataTemp = null ;
			for (int i = 0; i < betsList.size(); i++) {
				
				if(null == ptDataTemp || ptDataTemp.size() == 0){
					ptDataTemp = new ArrayList<AGData4OracleVO>() ;
				}
				ptDataTemp.add(betsList.get(i)) ;
				if(ptDataTemp.size() == 500 || i == betsList.size()-1){
					errormsg  = agDataProcessorService.repairAgData(ptDataTemp);
					if(StringUtils.isNotBlank(errormsg)){
						flag = false ;
					}
					ptDataTemp = null ;
				}
			}
			
			if (flag) 
				errormsg="数据修复成功";
			else 
				errormsg="提交失败:"+errormsg;

			
			log.info("getRepairData timeStart:" + timeStart );
			result = new HashMap<String, Object>();
			result.put("success", true);
			result.put("message",errormsg);
		} catch (Exception e) {
			
			result = new HashMap<String, Object>();
			result.put("success", false);
			result.put("message", "查询发生异常：" + e.getMessage());
			log.error("查询流水发生异常：", e);
		}
		
		  log.info("*************checkBefor30Minit complete "+timeStart+"********");

		Gson200(result);
		
	}
	
	
	 public List<AGData4OracleVO> organizedata(String date){
		List<AGData4OracleVO>rtnData=new ArrayList<AGData4OracleVO>();
		  String cagent="B20";
		  Integer pageSize=500;
		

		  try {
			
		  Calendar calendarStartTime=Calendar.getInstance();
		  calendarStartTime.setTime(DateUtil.parseDateForStandard(date));
		  Date startTime=calendarStartTime.getTime();
		  String  beforeTime= agDataProcessorService.getAgLastTime("AGIN");
		  Calendar calendarLastTime=Calendar.getInstance();
		
		

		  if (calendarStartTime.getTime().getDate()==calendarLastTime.getTime().getDate()) {
			  calendarLastTime.setTime(DateUtil.parseDateForStandard(beforeTime));
			  calendarLastTime.set(Calendar.SECOND, -60*5);
		  }else{
			  calendarLastTime.setTime(calendarStartTime.getTime());
			  calendarLastTime.add(Calendar.HOUR, 23); 
			  calendarLastTime.add(Calendar.MINUTE, 59);
			  calendarLastTime.add(Calendar.SECOND, 59);
		  }
		  
		 Date endTime=calendarLastTime.getTime();
	     String xml=AgUtils.getBetRecordByDate("AGIN",startTime, endTime, cagent, pageSize,1);
	     rtnData=AGSlotThread.getReRecordData(xml, startTime,endTime,cagent,"AGIN",pageSize,1);
		  } catch (Exception e) {
			  e.printStackTrace();
			  return rtnData;
		  }
		return rtnData;
	}
	

	public IAGDataProcessorService getAgDataProcessorService() {
		return agDataProcessorService;
	}

	public void setAgDataProcessorService(IAGDataProcessorService agDataProcessorService) {
		this.agDataProcessorService = agDataProcessorService;
	}


}
