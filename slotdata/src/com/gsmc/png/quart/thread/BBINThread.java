package com.gsmc.png.quart.thread;


import java.util.List;

import com.gsmc.png.model.BbinData;
import com.gsmc.png.service.interfaces.CommonService;

public class BBINThread implements Runnable{
	private CommonService commonService;
	private List<BbinData> datas;

	public BBINThread(CommonService commonService, List<BbinData> datas) {
		this.commonService = commonService;
		this.datas = datas;
	}

	@Override
	public void run() {
		commonService.saveOrUpdateAll(datas);
	}
}
