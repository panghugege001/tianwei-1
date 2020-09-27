package com.gsmc.png.utils;

import org.apache.log4j.Logger;

import com.gsmc.png.service.interfaces.IPNGDataProcessorService;

public class PNGDataProcessorThread extends Thread{
	
	private Logger log = Logger.getLogger(PNGDataProcessorThread.class); 
	private IPNGDataProcessorService pngDataProcessorService;
	private boolean wantToStop = false;
	
	public void run(){
		
		String currentThreadName = Thread.currentThread().getName();
		log.info(currentThreadName + "---- start");
		
		while(true){
			try {
				String msg = PNGMsgBuffer.pngMsgBuffer.take();
//				log.info(currentThreadName + " buffer size: " + PNGMsgBuffer.pngMsgBuffer.size());
				
				pngDataProcessorService = (IPNGDataProcessorService) SpringContextHelper.getBean("pngDataProcessorService");
				String result = pngDataProcessorService.processData(msg);
				
				log.info(currentThreadName + "---result:" + result);
			} catch (Exception e) {
				
				e.printStackTrace();
				
				if(this.wantToStop){
					log.info(currentThreadName + " want to stop");
					break;
				}
			}
		}
		
		log.info(currentThreadName + " stop");
	}
	
	public void init(){
		this.setName("PNGDataProcessorThread--" + ++StringUtil.count);
		this.start();
		
	}
	
	public void destroy(){
		this.wantToStop = true;
		this.interrupt();
	}

	public IPNGDataProcessorService getPngDataProcessorService() {
		return pngDataProcessorService;
	}

	public void setPngDataProcessorService(
			IPNGDataProcessorService pngDataProcessorService) {
		this.pngDataProcessorService = pngDataProcessorService;
	}

}
