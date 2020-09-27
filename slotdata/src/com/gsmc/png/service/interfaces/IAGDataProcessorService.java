package com.gsmc.png.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gsmc.png.model.ag.AGData4OracleVO;


public interface IAGDataProcessorService {

	/**
	 * 根据JSON字符串将流水数据存入数据库。
	 * @param
	 * @return
	 * */
	public String processData(String xml,Date createtime,Date fromTime,Date toTime,List<AGData4OracleVO> listData);
	
	
	
	
	/**
	 * 获取抓取平台的最后更新时间
	 * @param
	 * @return
	 * */
	public String getAgLastTime(String platform);
	
	/**
	 * 
	 * @param
	 * @return
	 * */
	public String processInsertAgData(List<AGData4OracleVO>data,Date lastTime,String platformType);
	
	

	/**
	 * 根据时间查询玩家流水。
	 * @param timeStart
	 * @param timeEnd
	 * @param loginname
	 * @return
	 * */
	public Map<String, Object> getPlayerBetsByDate(String timeStart, String timeEnd, String loginname);
	
	/**
	 * 根据时间查询玩家流水。
	 * @param timeStart
	 * @param timeEnd
	 * @param loginname
	 * @return
	 * */
	public String repairAgData(List<AGData4OracleVO>list);
	
	
	public List<AGData4OracleVO> organizedata(String date);


	
}
