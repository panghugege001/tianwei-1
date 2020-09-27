package com.gsmc.png.quart.fetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gsmc.png.model.ag.AGData4OracleVO;
import com.gsmc.png.quart.thread.AGSlotThread;
import com.gsmc.png.quart.thread.AGThreadPoolUtil;
import com.gsmc.png.service.interfaces.IAGDataProcessorService;

public class FetchAGSlotJobService {
	
	private IAGDataProcessorService agDataProcessorService;

	private static Logger log = Logger.getLogger(FetchAGSlotJobService.class);

	
	public void processStatusData() {
		
		AGSlotThread agSlotThread2=new AGSlotThread(agDataProcessorService,"AGIN");
		AGThreadPoolUtil.getInstance().add(agSlotThread2);
		
	}
	
	
    public void checkBefor30Minit() {
		log.info("*************checkBefor30Minit start ********");
		
		List<AGData4OracleVO>betsList=new ArrayList<AGData4OracleVO>();
		String errormsg=null;
		long startTime=System.currentTimeMillis();   //获取开始时间 
		
		betsList=AGSlotThread.checkBefor30Minit(agDataProcessorService);
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
	 
	 
		long endTime=System.currentTimeMillis(); //获取结束时间 
		 
		if (flag) 
			errormsg="checkBefor30Minit..................数据修复成功";
		else 
			errormsg="提交失败:"+errormsg;
		
		log.info("程序运行时间： "+((endTime-startTime)/(1000 * 60))+"分钟 **********"+errormsg);
		
	}


	public IAGDataProcessorService getAgDataProcessorService() {
		return agDataProcessorService;
	}

	public void setAgDataProcessorService(IAGDataProcessorService agDataProcessorService) {
		this.agDataProcessorService = agDataProcessorService;
	}



}
