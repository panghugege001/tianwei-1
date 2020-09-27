package dfh.service.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.model.Guestbook;

import dfh.action.vo.Messages;

public interface GuestBookService {
	void delete(Class clazz, Serializable id);

	List findByCriteria(DetachedCriteria criteria);

	List findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults);

	List findByNamedQuery(String sql, Map params);

	Object get(Class clazz, Serializable id);

	Object get(Class clazz, Serializable id, LockMode mode);

	HibernateTemplate getHibernateTemplate();

	List loadAll(Class clazz);

	Object save(Object o);

	void update(Object o);
	
	Integer getGuestbookCount(String loginname);
	
	Integer getGuestbookCountAll(String loginname);
	
	Integer getGuestbookCountMessage(Integer agent);
	
	public List<Guestbook> getGuestbookMessageList(String loginname, Integer page, Integer count, Integer userstatus, Integer agent);
	
	List<Guestbook> getGuestbookList(String loginname,Integer page,Integer count,Integer userstatus,Integer referenceId);
	
	public Integer getGuestbookListCount(String loginname,Integer userstatus);
	
	public Guestbook getGuestbook(Integer id);
	
	public Guestbook saveGuestbook(String loginname, Integer id, String content, String ip);
	
	public Guestbook saveBookDate(String loginname, Integer id,String title,String content, String ip);
	
	/**
	 * 未读站内信数量
	 * @param loginname
	 * @param agent
	 * @return
	 */
	Integer getUnReadMessageCount(String loginname, Integer agent);
	
	/**
	 * 查询会员站内信
	 * @param loginname
	 * @param agent
	 * @return
	 */
	public void getMessagesByUser(String loginName, Integer agent, Messages msg);
	
	/**
	 * 判断玩家是否已经阅读群发站内信
	 * @param loginname
	 * @param agent
	 * @return
	 */
	public boolean isRead4PublicMsg(Integer msgID, String loginName);
	
	/**
	 * 判断是否有回信
	 * @param id
	 * @return
	 */
	public Integer getSubMessage(Integer id);
}
