package dfh.icbc.quart.fetch;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import dfh.model.PlatformData;
import dfh.remote.bean.NTwoClientBetBean;
import dfh.utils.NTwoUtil;

public class NTwoDataThread implements Callable<List<PlatformData>>{
	
	private static Logger log = Logger.getLogger(NTwoDataThread.class);
	private Calendar searchDate;
	
	public NTwoDataThread(Calendar searchDate) {
		this.searchDate = searchDate;
	}
	
	@Override
	public List<PlatformData> call() throws Exception {
		Map<String, NTwoClientBetBean> beanMap = NTwoUtil.gameInfoPendingRequest(searchDate);
		return NTwoUtil.convertToPlatformData(beanMap, searchDate);
	}

}
