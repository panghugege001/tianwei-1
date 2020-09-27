package com.gsmc.png.quart.thread;


import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import com.gsmc.png.model.PlatformData;
import com.gsmc.png.remote.TTGNetWinBean;
import com.gsmc.png.utils.TTGUtil;

public class TTGThread implements Callable<List<PlatformData>>{
	
	private Calendar searchDate;
	
	public TTGThread(Calendar searchDate) {
		this.searchDate = searchDate;
	}
	
	@Override
	public List<PlatformData> call() throws Exception {
		List<TTGNetWinBean> ttgNetWinBeanList = TTGUtil.searchTTGNetWin(searchDate);
		return TTGUtil.convertToPlatformData(ttgNetWinBeanList, searchDate);
	}
}
