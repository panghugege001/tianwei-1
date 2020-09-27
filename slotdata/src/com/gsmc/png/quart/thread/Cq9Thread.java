package com.gsmc.png.quart.thread;
import java.util.List;
import java.util.concurrent.Callable;
import com.gsmc.png.model.CqData;
import com.gsmc.png.utils.CQ9Util;

public class Cq9Thread implements Callable<List<CqData>>{
	
	private String startTime;
	private String endTime;
	
	public Cq9Thread(String startTime,String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<CqData> call() throws Exception {
		List<CqData> datas = CQ9Util.getbets(startTime, endTime);
		return datas;
	}
}
