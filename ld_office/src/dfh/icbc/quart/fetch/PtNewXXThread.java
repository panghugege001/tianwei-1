package dfh.icbc.quart.fetch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import net.sf.json.JSONObject;
import dfh.model.Users;
import dfh.service.implementations.GetdataServiceImpl;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.PtUtil;

public class PtNewXXThread extends Thread {
	
	private static Logger log = Logger.getLogger(PtNewXXThread.class);
	private String executeTime = null ;

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	private IGetdateService getdateService;
	public PtNewXXThread(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

	@Override
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
			log.info("both-->"+phpHtmlRegular);
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
			log.info("both-->"+phpHtmlRegular);
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
	
	}
	
	
	public void parsePTData(String phpHtml,String dateTime){
		JSONObject jsonObj = JSONObject.fromObject(phpHtml);
		if (!jsonObj.containsKey("result")) {
			return;
		}
		//获取数据
		log.info("size-->"+jsonObj.getJSONArray("result").size());
		
		for (Object objectData : jsonObj.getJSONArray("result")) {
			JSONObject jsonObjDate=JSONObject.fromObject(objectData);
			String PLAYERNAME=jsonObjDate.getString("PLAYERNAME");
			if(PLAYERNAME.startsWith("K")){
				String FULLNAME=jsonObjDate.getString("FULLNAME");
				String VIPLEVEL=jsonObjDate.getString("VIPLEVEL");
				String COUNTRY=jsonObjDate.getString("COUNTRY");
				String GAMES=jsonObjDate.getString("GAMES");
				String CURRENCYCODE=jsonObjDate.getString("CURRENCYCODE");
				String BETS=jsonObjDate.getString("BETS");
				String WINS=jsonObjDate.getString("WINS");
				String INCOME=jsonObjDate.getString("INCOME");
//				String RNUM=jsonObjDate.getString("RNUM");
				String RNUM = "1";
				
				Boolean b=getdateService.dealNewPtData(PLAYERNAME,FULLNAME,VIPLEVEL,COUNTRY,GAMES,CURRENCYCODE,BETS,WINS,INCOME,RNUM,dateTime);
			    if(b){
//			    	log.info("*************"+jsonObjDate.toString()+"*******数据库成功***********");
			    }else{
			    	log.info("*************"+jsonObjDate+"*******数据库失败***********");
			    }
			}else{
				//log.info("*************"+jsonObjDate+"******************");
			}
		}
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

}
