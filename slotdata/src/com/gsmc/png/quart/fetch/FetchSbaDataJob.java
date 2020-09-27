package com.gsmc.png.quart.fetch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gsmc.png.service.interfaces.ISBADataProcessorService;
import com.gsmc.png.utils.ShaBaUtils;

public class FetchSbaDataJob {

	private static Logger log = Logger.getLogger(FetchSbaDataJob.class);

	private ISBADataProcessorService sbaService;

	public void execute() {
		try {
			Long version_key = sbaService.getVersionKey();
			Map map = null;
			if (version_key != null) {
				if(version_key.longValue() != 0){
					map = ShaBaUtils.GetBetDetail(version_key, "");
					if(map != null && map.size() > 0){
						sbaService.processData(version_key,map);
					}
				}else{
					//如果抓到最后一笔，记录调用次数
					map = new HashMap();
					map.put("last_version_key", version_key);
					map.put("BetDetails", new ArrayList());
					sbaService.processData(version_key,map);
				}
			}else{
				map = ShaBaUtils.GetBetDetail(0L, "");
				if(map != null){
					sbaService.processData(version_key,map);
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	public ISBADataProcessorService getSbaService() {
		return sbaService;
	}

	public void setSbaService(ISBADataProcessorService sbaService) {
		this.sbaService = sbaService;
	}
}
