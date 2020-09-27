package com.gsmc.png.action;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.gsmc.png.service.interfaces.IPNGDataProcessorService;
import com.gsmc.png.utils.PNGMsgBuffer;


public class PNGDataFeedAction extends SubActionSupport {
	
	
	private static Logger log = Logger.getLogger(PNGDataFeedAction.class);
	private IPNGDataProcessorService pngDataProcessorService;
	private HttpServletRequest req;
	
	
	public void pngDataFeed(){
		
		try {
			
			req = this.getRequest();
			String ip = getIp(req);
			if(!"52.192.232.167".equals(ip)){
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
			
			result = this.pngDataProcessorService.getPlayerBetsByDate(timeStart, timeEnd, loginname);
		} catch (Exception e) {
			
			result = new HashMap<String, Object>();
			result.put("success", false);
			result.put("message", "查询发生异常：" + e.getMessage());
			log.error("查询流水发生异常：", e);
		}
		
		Gson200(result);
	}

	public IPNGDataProcessorService getPngDataProcessorService() {
		return pngDataProcessorService;
	}

	public void setPngDataProcessorService(
			IPNGDataProcessorService pngDataProcessorService) {
		this.pngDataProcessorService = pngDataProcessorService;
	}
	
}
