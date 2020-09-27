package dfh.service.implementations;

import java.util.Date;

import org.apache.log4j.Logger;

import dfh.utils.DateUtil;

public class SystemAutoUpdateXimaStatus extends AbstractGameinfoServiceImpl {

	private Logger log=Logger.getLogger(SystemAutoUpdateXimaStatus.class);
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			updateXimaStatus();
			this.destory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.setErrorMsg(e.getMessage());
		}
	}
	
	public void updateXimaStatus() throws Exception {
		// TODO Auto-generated method stub
		sf=new StringBuffer();
		this.setMsg("正在更新洗码数据状态...");
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		this.getGameinfoDao().updateXimaStatus(starttime, endtime);
		this.setMsg("完成", true);
	}

}
