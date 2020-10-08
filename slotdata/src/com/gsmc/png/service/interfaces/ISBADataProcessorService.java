package com.gsmc.png.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ISBADataProcessorService {

	/**
	 * 查询沙巴返回的last_version_key
	 * @return
	 */
	public String getVersionKey();
	/**
	 * 将流水数据Map存入数据库。
	 * @param
	 * @return
	 * */
	@SuppressWarnings("rawtypes")
	public String processData(String version_key,Map map);
	
	/**
	 * 根据时间查询产品流水。
	 * @param startTime
	 * @param endTime
	 * @param productdb
	 * @return
	 * */
	public List getSbaXimaData(String startTime, String endTime,String productdb);
	
	public void reacquireSBAData();
/*	*//**
	 * 获取更新时间
	 * @return
	 *//*
	public Date getUpdateTime();*/
	
}
