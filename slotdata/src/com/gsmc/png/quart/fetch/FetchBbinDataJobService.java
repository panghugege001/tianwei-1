package com.gsmc.png.quart.fetch;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.gsmc.png.model.BbinData;
import com.gsmc.png.quart.thread.BBINThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.BBinUtils;

public class FetchBbinDataJobService {
	
	private static Logger log = Logger.getLogger(FetchBbinDataJobService.class);

	final CommonService commonService;

	public FetchBbinDataJobService(CommonService commonService) {
		this.commonService = commonService;
	}


	@SuppressWarnings("rawtypes")
	public void execute(){
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
		try {
			//最大10个并发的线程池，超出部分在队列中等待
			String sql = "select type, MAX(VendorId) vendorId from bbin_data group by type ";
			List list = commonService.excuteQuerySql(sql, new HashMap<String, Object>());
			BigInteger vendorid = new BigInteger("1");
			//gebrbv电子 gfbrbv捕鱼 gbrbv真人 gsbrbvbbin体育 glbrbv彩票
			String[] arr = {"gebrbv","gfbrbv","gbrbv","gsbrbvbbin","glbrbv"};
			for (int i = 0; i < arr.length; i++) {
				String method = arr[i];
				if (list != null && list.size() > 0 && list.get(0) != null) {
					for (int j = 0; j < list.size(); j++) {
						Object[] obj = (Object[]) list.get(j);
						String type = (String) obj[0];
						if(method.equals(type)){
							vendorid = (BigInteger) obj[1];
						}
					}
				}
				List<BbinData> datas = BBinUtils.GetBettingRecord(vendorid,method);
				if(datas != null && datas.size() > 0){
					fixedThreadPool.execute(new BBINThread(commonService, datas));
				}
			}
		} catch (Exception e) {
			log.error("获取bbin投注数据错误：" + e.getMessage(), e);
		}finally {
			fixedThreadPool.shutdown();
		}
	}

}
