package com.gsmc.png.quart.thread;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;

import com.gsmc.png.model.BBinParam;
import com.gsmc.png.model.BbinData;
import com.gsmc.png.response.BBinBetRecordRep;
import com.gsmc.png.response.BBinRespBody;
import com.gsmc.png.utils.BBinUtils;
import com.gsmc.png.utils.DateUtil;

public class BBINThread implements Callable<List<BbinData>> {

	private String startTime;
	private String endTime;

	public BBINThread(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override
	public List<BbinData> call() throws Exception {
		BBinRespBody<ArrayList<BBinBetRecordRep>> respBody = null;
		BBinParam param = new BBinParam();
		param.setStartTime(DateUtil.dateToStr(DateUtil.parseDateForStandard(startTime), "HH:mm:ss"));
		param.setRoundDate(DateUtil.dateToStr(DateUtil.parseDateForStandard(startTime), DateUtil.YYYY_MM_DD));
		param.setEndTime(DateUtil.dateToStr(DateUtil.parseDateForStandard(endTime), "HH:mm:ss"));
		respBody = BBinUtils.getBetRecord(param);
		List<BBinBetRecordRep> list =  respBody.getData();
		List<BbinData> bbinList = new ArrayList<BbinData>();
		if(CollectionUtils.isNotEmpty(list)){
			for(BBinBetRecordRep rep : list){
				BbinData data = new BbinData();
				data.setBillNo(rep.getWagersId());
				data.setAccount(rep.getUsername());
				data.setBetAmount(rep.getBetAmount());
				data.setValidAmount(rep.getCommissionable());
				data.setAddTime(new Date());
				data.setBetTime(rep.getWagersDate());
				//注單結果(N:已取消,X:未結算,W:贏,L:輸,LW:贏一半,LL:輸一半,0:平手,S:等待中,D:未接受,C:註銷,F:非法下注,SC:系統註銷,DC:危險球註銷)
				//data.setSettle(rep.getSettle());
				data.setWinAmount(rep.getPayoff());
				bbinList.add(data);
			}
		}
		return bbinList;
	}
}
