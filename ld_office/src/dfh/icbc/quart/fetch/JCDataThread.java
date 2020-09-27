package dfh.icbc.quart.fetch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import dfh.model.JCProfitData;
import dfh.model.Users;
import dfh.service.implementations.GetdataServiceImpl;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.DateUtil;
import dfh.utils.PtUtil;

public class JCDataThread extends Thread {
	
	private static Logger log = Logger.getLogger(JCDataThread.class);
	private String executeTime = null ;

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	private IGetdateService getdateService;
	public JCDataThread(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

	/*@Override
	public void run() {
		if(StringUtils.isBlank(executeTime)){
			executeQuartz();
		}else{
			execute(executeTime);
		}
	}
	
	public  void executeQuartz(){
		try {
			Date date = new Date();
			SimpleDateFormat sdfHHH = new SimpleDateFormat("HH");
			Integer hh = Integer.parseInt(sdfHHH.format(date));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateTime = null;
			if (hh < 7) {
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.add(Calendar.DAY_OF_MONTH, -1);
				dateTime = formatter.format(cals.getTime());
			} else {
				dateTime = formatter.format(date);
			}
			String startTime=dateTime+"DAVIDPT00:00:00";
			String endTime=dateTime+"DAVIDPT23:59:59";
			
			String phpHtmlRegular=PtUtil.getSepPlayerBet(startTime, endTime,"both");
//			log.info("both-->"+phpHtmlRegular);
			if(phpHtmlRegular!=null && !phpHtmlRegular.equals("")){
				parsePTData(phpHtmlRegular,  dateTime);
			}else{
				log.error("*****获取pt-->both投注额失败！******"+startTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void execute(String time){

		try {
			String startTime=time+"DAVIDPT00:00:00";
			String endTime=time+"DAVIDPT23:59:59";
			
			String phpHtmlRegular=PtUtil.getSepPlayerBet(startTime, endTime,"both");
//			log.info("both-->"+phpHtmlRegular);
			if(phpHtmlRegular!=null && !phpHtmlRegular.equals("")){
				parsePTData(phpHtmlRegular,  time);
			}else{
				log.error("*****获取pt-->both投注额失败！******"+startTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	
	}*/
	
	
	public void parseJCData(String profit, String executeTime){
		JSONObject jsonObj = JSONObject.fromObject(profit);
		if (!jsonObj.containsKey("profit")) {
			log.error(jsonObj+" don't have profit element");
			return;
		}
		//获取数据
		//log.info("size-->"+jsonObj.getJSONArray("profit").size());
		JSONArray profits = JSONArray.fromObject(jsonObj.get("profit").toString());
		log.info("size-->"+profits.size());
		List<JCProfitData> jclist = new ArrayList<JCProfitData>();
		for (Object objectData : profits) {
			JSONObject jsonObjDate=JSONObject.fromObject(objectData);
			String PLAYERNAME=jsonObjDate.getString("ACCOUNTS");
			if(PLAYERNAME.startsWith("K")){
				JCProfitData jcd = new JCProfitData();
				jcd.setPlayerName(PLAYERNAME);
				jcd.setStartTime(DateUtil.parseDateForStandard(
						jsonObjDate.getString("BET_TIME")+" 00:00:00"));
				jcd.setEndTime(DateUtil.parseDateForStandard(
						jsonObjDate.getString("BET_TIME")+" 23:59:59"));
				jcd.setActual(jsonObjDate.getDouble("factAmounts"));
				jcd.setBonus(jsonObjDate.getDouble("planamounts"));//使用planamounts表示总的中奖金额
				jcd.setWin(jsonObjDate.getDouble("winprice"));
				jcd.setOrderSum(jsonObjDate.getDouble("TOTAL_NUM"));
				jcd.setCreateTime(DateUtil.now());
				//String RNUM=jsonObjDate.getString("RNUM");
				//String RNUM = "1";
				jclist.add(jcd);
			}else{
				log.error("发现不是K开头的账号，账号名为："+PLAYERNAME);
			}
		}
		if (jclist.isEmpty() || jclist.size()==0){
			return;
		}
		boolean rb = getdateService.updateJCData(jclist,executeTime);
		if(!rb){
			log.info("*************更新JC信息至数据库失败***********");
		}
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

}
