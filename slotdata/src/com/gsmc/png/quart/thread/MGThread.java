package com.gsmc.png.quart.thread;
import java.util.List;
import java.util.concurrent.Callable;

import com.gsmc.png.model.MgData;
import com.gsmc.png.utils.MGSUtil;

public class MGThread implements Callable<List<MgData>>{
	
	private String startTime;
	private String endTime;
	
	public MGThread(String startTime,String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MgData> call() throws Exception {
		List<MgData> datas = MGSUtil.getbets(startTime, endTime);
		return datas;
	}
}
