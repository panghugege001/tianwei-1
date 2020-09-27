package com.gsmc.png.service.interfaces;

import java.util.Map;

public interface IPNGDataProcessorService {

	/**
	 * 根据JSON字符串将流水数据存入数据库。
	 * @param
	 * @return
	 * */
	public String processData(String msg);

	/**
	 * 根据时间查询玩家流水。
	 * @param timeStart
	 * @param timeEnd
	 * @param loginname
	 * @return
	 * */
	public Map<String, Object> getPlayerBetsByDate(String timeStart, String timeEnd, String loginname);
}
