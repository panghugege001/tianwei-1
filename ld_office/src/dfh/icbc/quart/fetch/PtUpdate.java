package dfh.icbc.quart.fetch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import dfh.service.interfaces.IGetdateService;
import dfh.utils.DateUtil;
import dfh.utils.PtUtil;

public class PtUpdate{
	
	private static Logger log = Logger.getLogger(PtUpdate.class);
	
	public static PtUpdate instance = null; 
	

	public static void execute(IGetdateService getdateService) {
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
//			String startTime=dateTime+"DAVIDPT00:00:00";
//			String endTime=dateTime+"DAVIDPT23:59:59";
			String startTime=dateTime;
			String endTime=DateUtil.getchangedDateStr(1, dateTime);
			
			String phpHtml=PtUtil.getPlayerAllBet(startTime, endTime);
			if(phpHtml!=null && !phpHtml.equals("")){
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (!jsonObj.containsKey("result")) {
					return;
				}
				//获取数据
				for (Object objectData : jsonObj.getJSONArray("result")) {
					JSONObject jsonObjDate=JSONObject.fromObject(objectData);
					String PLAYERNAME=jsonObjDate.getString("PLAYERNAME");
					if(PLAYERNAME.startsWith("K")){
						String CODE=jsonObjDate.getString("CODE");
						String CURRENCYCODE=jsonObjDate.getString("CURRENCYCODE");
						String ACTIVEPLAYERS=jsonObjDate.getString("ACTIVEPLAYERS");
						String BALANCECHANGE=jsonObjDate.getString("BALANCECHANGE");
						String DEPOSITS=jsonObjDate.getString("DEPOSITS");
						String WITHDRAWS=jsonObjDate.getString("WITHDRAWS");
						String BONUSES=jsonObjDate.getString("BONUSES");
						String COMPS=jsonObjDate.getString("COMPS");
						String PROGRESSIVEBETS=jsonObjDate.getString("PROGRESSIVEBETS");
						String PROGRESSIVEWINS=jsonObjDate.getString("PROGRESSIVEWINS");
						String BETS=jsonObjDate.getString("BETS");
						String WINS=jsonObjDate.getString("WINS");
						String NETLOSS=jsonObjDate.getString("NETLOSS");
						String NETPURCHASE=jsonObjDate.getString("NETPURCHASE");
						String NETGAMING=jsonObjDate.getString("NETGAMING");
						String HOUSEEARNINGS=jsonObjDate.getString("HOUSEEARNINGS");
//						String RNUM=jsonObjDate.getString("RNUM");
						String RNUM="1";
						Boolean b=getdateService.processNewStatusData(PLAYERNAME,CODE,CURRENCYCODE,ACTIVEPLAYERS,BALANCECHANGE,DEPOSITS,WITHDRAWS,BONUSES,COMPS,PROGRESSIVEBETS,PROGRESSIVEWINS,BETS,WINS,NETLOSS,NETPURCHASE,NETGAMING,HOUSEEARNINGS,RNUM,dateTime);
					    if(b){
					    	//log.info("*************"+jsonObjDate+"*******数据库成功***********");
					    }else{
					    	log.info("*************"+jsonObjDate+"*******数据库失败***********");
					    }
					}else{
						//log.info("*************"+jsonObjDate+"******************");
					}
				}
			}else{
				log.error("*****获取pt投注额失败！******"+startTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}

}
