package com.gsmc.png.quart.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.spider.PTBetVO;
import com.gsmc.png.spider.PTSpider;
import com.gsmc.png.utils.DateUtil;


public class PTBetUpdateThread extends Thread{

	private static Logger log = Logger.getLogger(PTBetUpdateThread.class);

	private final String PALY_START = "DY8";
	private final String isExistSql = "select uuid from platform_data where loginname=:name and platform=:platform and starttime=:start and endtime=:end";
	private final String updateBetSql = "update platform_data set bet=:bet, profit=:profit, updatetime=:now where uuid=:uuid";
	private final String addBetSql = "insert into platform_data(uuid, platform, loginname, bet, profit, starttime, endtime, updatetime) values(:uuid, :platform, :name, :bet, :profit, :start, :end, :now)";
	private final String selectPTXMSql = "select id from pt_data_new where PLAYERNAME=:name and STARTTIME=:start and ENDTIME=:end";
	private final String updatePTXMSql = "update pt_data_new set CREATETIME=:createTime, BETS_TIGER=:slotbet, WINS_TIGER=:slotIncome, PROGRESSIVE_BETS=:progressiveBet, PROGRESSIVE_WINS=:progressiveIncome, PROGRESSIVE_FEE=:progressiveFee where id=:id";
	private final String updatePTProgressiveWinsSql = "update pt_data_new set PROGRESSIVE_FEE=:progressiveFee where id=:id";

	private CommonService commonService;
	private PTBetVO betVO;
	private String type;
	private Date start;
	private Date end;

	public PTBetUpdateThread(CommonService commonService, PTBetVO betVO, String type, Date start, Date end){
		this.commonService = commonService;
		this.betVO = betVO;
		this.type = type;
		this.start = start;
		this.end = end;
	}

	private void upgradeBetRecord(){
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			if(PTSpider.Type.ProgressiveWins.getCode().equalsIgnoreCase(type)){
				if(betVO.getProgressiveFee().compareTo(0.0) > 0){
					params.put("progressiveFee", betVO.getProgressiveFee());
					params.put("name", betVO.getLoginName());
					params.put("start", start);
					params.put("end", end);
					Object id = commonService.getOneValue(selectPTXMSql, params);
					if(id != null){
						params.put("id", id);
						commonService.executeSql(updatePTProgressiveWinsSql, params);
					}else{
						throw new RuntimeException("更新错误，请先执行【抓取PT总投注数据】");
					}
				}
			}else if(type.equalsIgnoreCase(PTSpider.platform_data)){
				params.put("bet", betVO.getBets());
				params.put("profit", betVO.getProfit());
				params.put("now", new Date());
				params.put("name", betVO.getLoginName().replaceFirst(PALY_START, "").toLowerCase());
				params.put("platform", "pttiger");
				params.put("start", start);
				params.put("end", end);

				Object uuidObj = commonService.getOneValue(isExistSql, params);
				if(null != uuidObj){
					params.put("uuid", uuidObj.toString());
					commonService.executeSql(updateBetSql, params);
				}else{
					params.put("uuid", UUID.randomUUID().toString());
					commonService.executeSql(addBetSql, params);
				}
				this.printParams(params);
			}else{
				params.put("slotbet", betVO.getBets());
				params.put("slotIncome", betVO.getProfit());
				params.put("progressiveBet", betVO.getProgressiveBet());
				params.put("progressiveIncome", betVO.getProgressviceProfit());
				params.put("progressiveFee", betVO.getProgressiveFee());//pt后台 progressive wins栏位 
				params.put("name", betVO.getLoginName());
				params.put("start", start);
				params.put("end", end);
				params.put("createTime", DateUtil.getNow());
				Object id = commonService.getOneValue(selectPTXMSql, params);
				if(id != null){
					params.put("id", id);
					commonService.executeSql(updatePTXMSql, params);
				}else{
					throw new RuntimeException("更新错误，请先执行【抓取PT总投注数据】");
				}
			}
		} catch (RuntimeException e) {//定时器无该异常，仅抛给officeaction.updatePTSlotDataOfYesterday()，返回浏览器
			e.printStackTrace();
			log.error("PT投注数据更新异常：" + betVO.getLoginName() + ", info:" + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("PT投注数据更新异常：" + betVO.getLoginName() + " " + e.getMessage(), e);
		}

	}

	private void printParams(Map<String, Object> params) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String paramsString = "";
		for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
			if (stringObjectEntry.getKey().equals("now")||stringObjectEntry.getKey().equals("start")||stringObjectEntry.getKey().equals("end")) {
				paramsString+=String.format("%s=%s,", stringObjectEntry.getKey(), simpleDateFormat.format((Date) stringObjectEntry.getValue()));
			}else{
				paramsString+=String.format("%s=%s,", stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
			}
		}
		log.info( String.format("PT投注数据更新记录：(Type:%s) 参数:%s", type,paramsString));
	}

	public PTBetVO getBetVO() {
		return betVO;
	}

	public void setBetVO(PTBetVO betVO) {
		this.betVO = betVO;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public void run() {
		upgradeBetRecord();
	}
}