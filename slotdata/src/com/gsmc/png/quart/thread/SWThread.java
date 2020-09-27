package com.gsmc.png.quart.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.gsmc.png.model.SwData;
import com.gsmc.png.utils.SWUtil;

public class SWThread implements Callable<List<SwData>> {

	private String startTime;
	private String endTime;

	public SWThread(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public List<SwData> call() throws Exception {
		List<SwData> list = new ArrayList<SwData>();
		
		Integer page =1;
		while (true) {
			List<SwData> datas = SWUtil.getPlayerBetByHistory(page,startTime, endTime);
			page++;
			if(datas == null){
				break;
			}
			list.addAll(datas);
		}
		return list;
	}
}
