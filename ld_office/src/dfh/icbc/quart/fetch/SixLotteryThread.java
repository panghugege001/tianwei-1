package dfh.icbc.quart.fetch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import dfh.model.PlatformData;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.DateUtil;
import dfh.utils.SixLotteryUtil;

public class SixLotteryThread extends Thread {
	
	private static Logger log = Logger.getLogger(SixLotteryThread.class);

	private IGetdateService getdateService;
	public SixLotteryThread(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

	@Override
	public void run() {
		try {
			Date date = new Date();
			SimpleDateFormat sdfHH = new SimpleDateFormat("HH");
			SimpleDateFormat sdfmm = new SimpleDateFormat("mm");
			Integer mm = Integer.parseInt(sdfmm.format(date));
			Integer hh = Integer.parseInt(sdfHH.format(new Date()));
			
			Date startT = null ;
			Date endT = null ;
			if (hh == 0 && mm <= 5) {
				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.add(Calendar.DAY_OF_MONTH, -1);
				cals.set(Calendar.HOUR_OF_DAY,0 );
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime();
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(new Date());
				endcals.add(Calendar.DAY_OF_MONTH, -1);
				endcals.set(Calendar.HOUR_OF_DAY,23 );
				endcals.set(Calendar.MINUTE, 59);
				endcals.set(Calendar.SECOND, 59);
				endT = endcals.getTime();
			} else {
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.set(Calendar.HOUR_OF_DAY,0 );
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime() ;
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(new Date());
				endcals.set(Calendar.HOUR_OF_DAY,23 );
				endcals.set(Calendar.MINUTE, 59);
				endcals.set(Calendar.SECOND, 59);
				endT = endcals.getTime();
			}
			
			
			String xml = SixLotteryUtil.getValidateBet(null, DateUtil.formatDateForStandard(startT), DateUtil.formatDateForStandard(endT)) ;
			if(xml.contains("無注單")){
				return ;
			}
			List<PlatformData> list = SixLotteryUtil.parseBetXml(xml) ;
			if(null != list && list.size()>0 ){
				for (PlatformData platformData : list) {
					platformData.setStarttime(startT);
					platformData.setEndtime(endT) ;
					platformData.setUuid(UUID.randomUUID().toString());
					platformData.setPlatform("sixlottery");
					getdateService.updateSixLotteryPlatForm(platformData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

}
