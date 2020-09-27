package dfh.service.interfaces;

import java.util.List;

import dfh.utils.Page;


public interface IQuerybetrankService {
	/**
	 * 根据传入的sql 返回list
	 * @return
	 */
	public List getListBysql(String sql);
	
	/**
	 * 
	 * @param sql
	 * @param a
	 * @return
	 */
	public Page getPage(String sql,int pageIndex,int size,String count);
}
