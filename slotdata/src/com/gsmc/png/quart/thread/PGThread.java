package com.gsmc.png.quart.thread;
import java.util.List;
import java.util.concurrent.Callable;
import com.gsmc.png.model.PgData;
import com.gsmc.png.utils.PGUtil;

public class PGThread implements Callable<List<PgData>>{
	
	private String startTime;
	private String endTime;
	
	public PGThread(String startTime,String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	

	@Override
	public List<PgData> call() throws Exception {
		List<PgData> datas = PGUtil.getbets(startTime, endTime);
		return datas;
	}
}
