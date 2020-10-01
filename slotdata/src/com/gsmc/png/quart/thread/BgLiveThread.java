package com.gsmc.png.quart.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.collections.CollectionUtils;
import com.gsmc.png.model.BgData;
import com.gsmc.png.model.BgLiveBetRecordItem;
import com.gsmc.png.model.enums.GameKindEnum;
import com.gsmc.png.response.BgBetRecordResult;
import com.gsmc.png.utils.BGUtil;
import com.gsmc.png.utils.DateUtil;

/**
 * BG真人
 * 
 * @author lenovo
 *
 */
public class BgLiveThread implements Callable<List<BgData>> {

	private String startTime;
	private String endTime;

	public BgLiveThread(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public List<BgData> call() throws Exception {
		BgBetRecordResult bgBetRecordResult = BGUtil.getBetRecord(GameKindEnum.LIVE.getCode(),
				DateUtil.parseDateForYYYYmmDDHHSS(startTime), DateUtil.parseDateForYYYYmmDDHHSS(endTime));
		if (bgBetRecordResult == null || CollectionUtils.isEmpty(bgBetRecordResult.getItems())) {
			return null;
		}
		List<BgLiveBetRecordItem> list = bgBetRecordResult.getItems();
		if (list == null || list.isEmpty()) {
			return null;
		}
		String userName = null;
		List<BgData> bgBetRecords = new ArrayList<>();
		for (BgLiveBetRecordItem item : list) {
			BgData bgBetRecord = new BgData();
			bgBetRecord.setBillNo(String.valueOf(item.getOrderId()));
			userName = item.getLoginId();
			bgBetRecord.setAccount(userName);
			bgBetRecord.setGameType(item.getGameId());
			bgBetRecord.setGameKind(item.getPlayName());
			bgBetRecord.setFirstKind(GameKindEnum.LIVE.getCode());
			Date betTime = DateUtil.strToDate(item.getOrderTime());
			bgBetRecord.setBetTime(betTime);
			bgBetRecord.setGmtBetTime(DateUtil.getAMESToUS(betTime));
			bgBetRecord.setAmesTime(betTime);
			bgBetRecord.setRebateTime(DateUtil.dateToYMDHMS(betTime));
			bgBetRecord.setStatTime(DateUtil.dateToYMD(betTime));
			if (item.getOrderStatus() != null) {
				if (item.getOrderStatus() == 2 || item.getOrderStatus() == 4) {
					bgBetRecord.setSettle(1);
					bgBetRecord.setBetAmount(Math.abs(item.getbAmount() != null ? item.getbAmount() : 0));
					bgBetRecord.setValidAmount(item.getValidBet());
					bgBetRecord.setWinAmount(-item.getPayment());
				} else if (item.getOrderStatus() == 1) {
					bgBetRecord.setSettle(0);
					bgBetRecord.setBetAmount(Math.abs(item.getbAmount() != null ? item.getbAmount() : 0));
					bgBetRecord.setValidAmount(0D);
					bgBetRecord.setWinAmount(0D);
				} else {
					bgBetRecord.setSettle(1);
					bgBetRecord.setBetAmount(Math.abs(item.getbAmount() != null ? item.getbAmount() : 0));
					bgBetRecord.setValidAmount(0D);
					bgBetRecord.setWinAmount(0D);
				}
			} else {
				bgBetRecord.setSettle(1);
				bgBetRecord.setBetAmount(Math.abs(item.getbAmount() != null ? item.getbAmount() : 0));
				bgBetRecord.setValidAmount(0D);
				bgBetRecord.setWinAmount(0D);
			}
			bgBetRecord.setAddTime(new Date());
			bgBetRecord.setUpdateTime(new Date());
			bgBetRecords.add(bgBetRecord);
		}
		return bgBetRecords;
	}

}
