package com.gsmc.png.quart.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.gsmc.png.model.AgData;
import com.gsmc.png.utils.AGINUtil;


public class AGThread implements Callable<List<AgData>> {

	private String startTime;
	private String endTime;

	public AGThread(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public List<AgData> call() throws Exception {
		List<AgData> ListAll = new ArrayList<AgData>();
		
		Integer page =1;
		while (true) {
			List<AgData> resultList = new ArrayList<AgData>();
			
			List<AgData> list1 = AGINUtil.getAginorders(startTime, endTime,page);
			if(list1.size() > 0 ){
				resultList.addAll(list1);
			}
			List<AgData> list2 = AGINUtil.getslotorders_ex(startTime, endTime,page);
			if(list2.size() > 0 ){
				resultList.addAll(list2);
			}
			List<AgData> list3 = AGINUtil.getyoplayorders_ex(startTime, endTime,page);
			if(list3.size() > 0 ){
				resultList.addAll(list3);
			}
			
			ListAll.addAll(resultList);
			if(resultList.size() == 0){
				break;
			}else{
				page ++;
			}
		}
		return ListAll;
	}
}
