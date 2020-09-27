package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.model.PtProfit;
import dfh.utils.Page;

public interface GuestBookService {
	
	HibernateTemplate getHibernateTemplate();

	/**
	 * 默认为不显示状态
	 * 
	 * @param title
	 * @param content
	 * @param username
	 * @param email
	 * @param phone
	 * @param qq
	 * @param ip
	 * @return
	 */
	String leaveWords(String title, String content, String username, String email, String phone, String qq, String ip);

	/**
	 * 标题默认为 "回复:"+title
	 * 前台使用
	 * @param referenceId
	 * @param content
	 * @param username
	 * @param email
	 * @param phone
	 * @param qq
	 * @param ip
	 * @return
	 */
	String leaveReplyWords(Integer referenceId, String content, String username, String email, String phone, String qq, String ip);
	
	/**
	 * 标题默认为 "回复:"+title
	 * 后台使用
	 * @param referenceId
	 * @param content
	 * @param username
	 * @param email
	 * @param phone
	 * @param qq
	 * @param ip
	 * @return
	 */
	String leaveReplyWordsAdmin(Integer referenceId, String content, String username, String email, String phone, String qq, String ip,Date createDate);
	
	
	String deleteWords(Integer id);

	/**
	 * 过滤隐藏的,flag=1
	 * 
	 * @return
	 */
	Page queryWordsForFront(Integer beginIndex, Integer size);

	/**
	 * 显示所有的
	 * 
	 * @param keywords
	 *            对应 标题和内容
	 * @param start
	 * @param end
	 * @return
	 */
	Page queryWordsForBack(String adminname,String username,String keywords, Integer flag,Integer adminUserStatus,Integer isadmin,Date start, Date end, Integer beginIndex, Integer size);
	/**
	 *
	 * 
	 * @param keywords
	 *           查询单个留言的所有回复记录
	 * @param start
	 * @param end
	 * @return
	 */
	Page queryReference(Integer id, Integer flag,Integer beginIndex,Integer size);
	/**
	 *
	 * 
	 * @param keywords
	 *        审核
	 * @param start
	 * @param end
	 * @return
	 */
	String auditing(Integer id);
	/**
	 * 管理员使用
	 * 默认为显示状态
	 * @param title
	 * @param content
	 * @param username
	 * @param email
	 * @param phone
	 * @param qq
	 * @param ip
	 * @return
	 */
	String oficeLeaveWords(Integer type,String username,Integer usernameType,String title,Integer flag,Date createDate,String content,String ip, Date start , Date end , String csType,String isdeposit);
	
	public Page queryIplist(String username,String agent,String ip,String client_address,String agent_website,Date start, Date end,Integer beginIndex, Integer size);
	
	Page getPtList(String loginname,Date start, Date end, Integer pageIndex, Integer size,String order,String by);
	
	Page getPtListInfo(String loginname,Date start, Date end, Integer pageIndex, Integer size,String order,String by);
	
	public List<PtProfit> getPtProfitList(Date start, Date end);

	public void batchDelete(String ids) throws Exception;
	
}
