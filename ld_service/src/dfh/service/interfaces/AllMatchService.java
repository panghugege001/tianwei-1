package dfh.service.interfaces;

import dfh.utils.Page;

public interface AllMatchService extends UniversalService{
	/**
	 * 获取老虎机周体验金比赛列表
	 * @param sql
	 * @param pageIndex
	 * @param size
	 * @param count
	 * @return
	 */
	public Page getSlotsMatchPage(String sql,int pageIndex,int size,String count);
	
	
	/**
	 * 演唱会活动流水列表
	 * @param sql
	 * @param pageIndex
	 * @param size
	 * @param count
	 * @return
	 */
	public Page getConcertPage(String sql, int pageIndex, int size,String count) throws Exception;
	
	
}
