package com.gsmc.png.quart.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.collections.CollectionUtils;

import com.gsmc.png.model.BgByBetRecordItem;
import com.gsmc.png.model.BgData;
import com.gsmc.png.model.enums.GameKindEnum;
import com.gsmc.png.response.BgBetRecordResult;
import com.gsmc.png.utils.BGUtil;
import com.gsmc.png.utils.DateUtil;

/**
 * BG捕鱼
 * @author lenovo
 *
 */
public class BgByThread implements Callable<List<BgData>> {

	private String startTime;
	private String endTime;

	public BgByThread(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override
	public List<BgData> call() throws Exception {
		BgBetRecordResult bgBetRecordResult = BGUtil.getBetRecord(GameKindEnum.BY.getCode(),
				DateUtil.parseDateForYYYYmmDDHHSS(startTime), DateUtil.parseDateForYYYYmmDDHHSS(endTime));
		if (bgBetRecordResult == null || CollectionUtils.isEmpty(bgBetRecordResult.getItems())) {
			return null;
		}
		List<BgByBetRecordItem> list = bgBetRecordResult.getItems();
		if (list == null || list.isEmpty()) {
			return null;
		}
		String userName = null;
		List<BgData> bgBetRecords = new ArrayList<>();
		for (BgByBetRecordItem item : list) {
			BgData bgBetRecord = new BgData();
			bgBetRecord.setBillNo(String.valueOf(item.getBetId()));
			userName = item.getLoginId();
			bgBetRecord.setAccount(userName);
			bgBetRecord.setGameType(item.getGameType());
			bgBetRecord.setGameKind("");
			bgBetRecord.setFirstKind(GameKindEnum.BY.getCode());
			Date betTime = DateUtil.strToDate(item.getOrderTime());
			bgBetRecord.setBetTime(betTime);
			bgBetRecord.setGmtBetTime(DateUtil.getAMESToUS(betTime));
			bgBetRecord.setAmesTime(betTime);
			bgBetRecord.setRebateTime(DateUtil.dateToYMDHMS(betTime));
			bgBetRecord.setStatTime(DateUtil.dateToYMD(betTime));
			bgBetRecord.setAddTime(new Date());
	        bgBetRecord.setUpdateTime(new Date());
	        bgBetRecord.setSettle(1);
	        bgBetRecord.setBetAmount(item.getBetAmount());
	        bgBetRecord.setValidAmount(item.getValidAmount());
	        bgBetRecord.setWinAmount(-item.getPayout());
			bgBetRecords.add(bgBetRecord);
		}
		return bgBetRecords;
	}
}
