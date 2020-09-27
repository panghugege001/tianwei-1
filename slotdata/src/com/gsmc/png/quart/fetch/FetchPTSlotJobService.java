package com.gsmc.png.quart.fetch;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import com.gsmc.png.quart.thread.PTBetUpdateThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.service.interfaces.PtDataService;
import com.gsmc.png.spider.PTBetVO;
import com.gsmc.png.spider.PTSpider;
import com.gsmc.png.spider.PTSpider.Type;

import java.util.*;

public class FetchPTSlotJobService {
	
	private static Logger log = Logger.getLogger(FetchPTSlotJobService.class);

	final CommonService commonService;
	final PtDataService ptDataService;

	public FetchPTSlotJobService(CommonService commonService, PtDataService ptDataService) {
		this.commonService = commonService;
		this.ptDataService = ptDataService;
	}

	private List<PTBetVO> compress (List<PTBetVO> betsList, List<PTBetVO> progressiveBetsList){
		Map<String, PTBetVO> ptslotBetMap = new HashMap<String, PTBetVO>();
		for (PTBetVO betVO : betsList) {
			ptslotBetMap.put(betVO.getLoginName(), betVO);
		}
		for (PTBetVO betVO2 : progressiveBetsList) {
			PTBetVO vo = ptslotBetMap.get(betVO2.getLoginName());
			if(vo != null){
				vo.setBets(vo.getBets() + betVO2.getBets());
				vo.setProfit(vo.getProfit() + betVO2.getProfit());
			} else {
				betsList.add(betVO2);
			}
		}
		return betsList;
	}

	public void execute(){
		try {
			List<PTBetVO> betsList = PTSpider.spider("today", Type.Slot_Machines);//Slot Machines
			//List<PTBetVO> progressiveBetsList = PTSpider.spider("today", Type.Progressive_Slot_Machines); // Progressive Slot Machines
			//this.compress(betsList, progressiveBetsList);

			Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
			Date endTime = new DateTime().plusDays(1).withTimeAtStartOfDay().minusSeconds(1).toDate();
			for (PTBetVO betVO : betsList) {
				ptDataService.execute(new PTBetUpdateThread(commonService, betVO, PTSpider.platform_data, startTime, endTime));
			}
			if(LocalDateTime.now().getHourOfDay() <= 2){   //3点前同时更新前一天的数据，前一天数据半小时更新一次
				List<PTBetVO> yesterdayBetsList = new ArrayList<PTBetVO>();
				Date yesterdayStart = new DateTime().minusDays(1).withTimeAtStartOfDay().toDate();
				Date yesterdayend = new DateTime().withTimeAtStartOfDay().minusSeconds(1).toDate();
				if(LocalDateTime.now().getMinuteOfHour() <= 3){
					//定时器设定的3分钟扫描一次，介于0:00 - 0:03 之间时，立即扫描一次昨天的数据
					yesterdayBetsList = PTSpider.spider("yesterday", Type.Slot_Machines);//Slot Machines
					//List<PTBetVO> yesterdayProgressiveBetsList = PTSpider.spider("yesterday", Type.Progressive_Slot_Machines); // Progressive Slot Machines
					//this.compress(yesterdayBetsList, yesterdayProgressiveBetsList);
				}else{
					//获取昨天PT数据的最后更新时间
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("ytStart", yesterdayStart);
					Object lastUpdateTimeObj = commonService.getOneValue("select updatetime from platform_data where starttime=:ytStart ORDER BY updatetime desc limit 1", params);
					if(null != lastUpdateTimeObj){
						Date lastUpdateTime = (Date) lastUpdateTimeObj;
						if(new Date().getTime() - lastUpdateTime.getTime() >= 20*60*1000){   //20分钟更新一次
							yesterdayBetsList = PTSpider.spider("yesterday", Type.Slot_Machines);//Slot Machines
							//List<PTBetVO> yesterdayProgressiveBetsList = PTSpider.spider("yesterday", Type.Progressive_Slot_Machines); // Progressive Slot Machines
							//this.compress(yesterdayBetsList, yesterdayProgressiveBetsList);
						}
					}
				}

				for (PTBetVO betVO2 : yesterdayBetsList) {
					ptDataService.execute(new PTBetUpdateThread(commonService, betVO2, PTSpider.platform_data, yesterdayStart, yesterdayend));
				}
			}
		} catch (Exception e) {
			log.error("爬取PT投注数据错误：" + e.getMessage(), e);
		}
	}

}
