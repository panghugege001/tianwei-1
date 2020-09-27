package dfh.icbc.quart.fetch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import dfh.model.PlatformData;
import dfh.model.bean.QtBetVo;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.DateUtil;
import dfh.utils.QtUtil;

public class QtThread {
	
	private static Logger log = Logger.getLogger(QtThread.class);


	public static void execute(IGetdateService getdateService, String fromApi) {
		try {
			Date date = new Date();
			SimpleDateFormat sdfHH = new SimpleDateFormat("HH");
			SimpleDateFormat sdfmm = new SimpleDateFormat("mm");
			Integer mm = Integer.parseInt(sdfmm.format(date));
			Integer hh = Integer.parseInt(sdfHH.format(date));
			
			Date startT = null ;
			Date endT = null ;
			if (hh == 0 && mm <= 35) {
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.add(Calendar.DAY_OF_MONTH, -1);
				cals.set(Calendar.HOUR_OF_DAY,0 );
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime();
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(date);
				endcals.add(Calendar.DAY_OF_MONTH, 0);
				endcals.set(Calendar.HOUR_OF_DAY, 0);
				endcals.set(Calendar.MINUTE, 0);
				endcals.set(Calendar.SECOND, 0);
				endT = endcals.getTime();
			} else {
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.set(Calendar.HOUR_OF_DAY, 0);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime() ;
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(date);
				endcals.add(Calendar.DAY_OF_MONTH, 1);
				endcals.set(Calendar.HOUR_OF_DAY, 0);
				endcals.set(Calendar.MINUTE, 0);
				endcals.set(Calendar.SECOND, 0);
				endT = endcals.getTime();
			}
			List<PlatformData> list;
			if("yesOld".equals(fromApi)){
				//查询玩家交易记录的API接口
				Map<String, QtBetVo> map = new HashMap<String, QtBetVo>();
				String backStr = QtUtil.getBetTotal("", DateUtil.formatDateForQt(startT), DateUtil.formatDateForQt(endT), 100, 0, map);
				if("FAIL".equals(backStr)){
					return ;
				}
				list = QtUtil.parseBetString(backStr);
			}else if ("yes".equals(fromApi)){
				//查询玩家NGR接口
				Map<String, QtBetVo> map = new HashMap<String, QtBetVo>();
				String backStr = QtUtil.getBetTotalByNGR(DateUtil.formatDateForQt(startT), DateUtil.formatDateForQt(endT), map);
				if("FAIL".equals(backStr)){
					return ;
				}
				list = QtUtil.parseBetString(backStr);
			}else{
				//统计qtdata表的数据
				list = getdateService.selectQtData(startT, endT);
			}
			
			if(null != list && list.size()>0 ){
				for (PlatformData platformData : list) {
					platformData.setStarttime(startT);
					platformData.setEndtime(endT) ;
					platformData.setUuid(UUID.randomUUID().toString());
					platformData.setPlatform("qt");
					getdateService.updateQtPlatForm(platformData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}

}
