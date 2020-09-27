package dfh.service.implementations;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.action.vo.Messages;
import dfh.dao.GuestbookDao;
import dfh.model.Guestbook;
import dfh.service.interfaces.GuestBookService;

public class GuestBookServiceImpl implements GuestBookService {

	private static Logger log = Logger.getLogger(GuestBookServiceImpl.class);

	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private GuestbookDao guestbookDao;

	public GuestBookServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#delete(java.lang.Class,
	 *      java.io.Serializable)
	 */
	public void delete(Class clazz, Serializable id) {
		guestbookDao.delete(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#findByCriteria(org.hibernate
	 *      .criterion.DetachedCriteria)
	 */
	public List findByCriteria(DetachedCriteria criteria) {
		return guestbookDao.getHibernateTemplate().findByCriteria(criteria);
	}

	public List findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults) {
		return guestbookDao.getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#findByNamedQuery(java.lang
	 *      .String, java.util.Map)
	 */
	public List findByNamedQuery(String sql, Map params) {
		return guestbookDao.findByNamedQuery(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#get(java.lang.Class,
	 *      java.io.Serializable)
	 */
	public Object get(Class clazz, Serializable id) {
		return guestbookDao.get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#get(java.lang.Class,
	 *      java.io.Serializable, org.hibernate.LockMode)
	 */
	public Object get(Class clazz, Serializable id, LockMode mode) {
		return guestbookDao.get(clazz, id, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#getHibernateTemplate()
	 */
	public HibernateTemplate getHibernateTemplate() {
		return guestbookDao.getHibernateTemplate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#getHibernateTemplate()
	 */
	public Integer getGuestbookCount(String loginname) {
		return guestbookDao.getGuestbookCount(loginname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#getHibernateTemplate()
	 */
	public Integer getGuestbookCountAll(String loginname) {
		return guestbookDao.getGuestbookCountAll(loginname);
	}
	
	
	public Integer getGuestbookCountMessage(Integer agent) {
		return guestbookDao.getGuestbookCountMessage(agent);
	}
	
	public List<Guestbook> getGuestbookMessageList(String loginname, Integer page, Integer count, Integer userstatus, Integer agent) {
		return guestbookDao.getGuestbookMessageList(loginname, page, count, userstatus, agent);

	}

	public List<Guestbook> getGuestbookList(String loginname, Integer page, Integer count, Integer userstatus, Integer referenceId) {
		return guestbookDao.getGuestbookList(loginname, page, count, userstatus, referenceId);

	}

	public Integer getGuestbookListCount(String loginname, Integer userstatus) {
		return guestbookDao.getGuestbookListCount(loginname, userstatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#loadAll(java.lang.Class)
	 */
	public List loadAll(Class clazz) {
		return guestbookDao.loadAll(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#save(java.lang.Object)
	 */
	public Object save(Object o) {
		return guestbookDao.save(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#update(java.lang.Object)
	 */
	public void update(Object o) {
		guestbookDao.update(o);
	}

	public GuestbookDao getGuestbookDao() {
		return guestbookDao;
	}

	public void setGuestbookDao(GuestbookDao guestbookDao) {
		this.guestbookDao = guestbookDao;
	}

	/**
	 * 获取邮件
	 * 
	 * @param loginname
	 * @return
	 */
	public Guestbook getGuestbook(Integer id) {
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.eq("id", id));
		Guestbook guestbook = (Guestbook) guestbookDao.findByCriteria(dc).get(0);
		if (guestbook != null) {
			guestbook.setUserstatus(1);
			guestbookDao.update(guestbook);
		}
		return guestbook;
	}

	public Guestbook saveGuestbook(String loginname, Integer id, String content, String ip) {
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.eq("id", id));
		Guestbook guestbook = (Guestbook) guestbookDao.findByCriteria(dc).get(0);
		if (guestbook != null) {
			Guestbook book = new Guestbook();
			book.setUsername(loginname);
			book.setReferenceId(guestbook.getId());
			book.setFlag(0);
			book.setIpaddress(ip);
			book.setCreatedate(new Timestamp(new Date().getTime()));
			book.setAdminname("客服管理员");
			book.setIsadmin(1);
			book.setTitle(guestbook.getTitle());
			book.setContent(content);
			book.setAdminstatus(0);
			book.setUserstatus(1);
			book.setUpdateid(guestbook.getUpdateid());
			guestbookDao.save(book);
			guestbook.setAdminstatus(0);
			guestbook.setUserstatus(1);
			if (guestbook.getRcount() != null) {
				guestbook.setRcount(guestbook.getRcount() + 1);
			} else {
				guestbook.setRcount(1);
			}
			guestbookDao.update(guestbook);
		}
		return guestbook;
	}

	public Guestbook saveBookDate(String loginname, Integer id, String title, String content, String ip) {
		Guestbook book = new Guestbook();
		book.setUsername(loginname);
		book.setReferenceId(null);
		book.setFlag(0);
		book.setIpaddress(ip);
		book.setCreatedate(new Timestamp(new Date().getTime()));
		book.setAdminname("客服管理员");
		book.setIsadmin(1);
		book.setTitle(title);
		book.setContent(content);
		book.setAdminstatus(0);
		book.setUserstatus(1);
		guestbookDao.save(book);
		return book;
	}

	@Override
	public Integer getUnReadMessageCount(String loginname, Integer agent) {
		return guestbookDao.getUnReadMessageCount(loginname, agent);
	}

	@Override
	public void getMessagesByUser(String loginName, Integer agent, Messages msg) {
		guestbookDao.getMessagesByUser(loginName, agent, msg);
	}

	@Override
	public boolean isRead4PublicMsg(Integer msgID, String loginName) {
		return guestbookDao.isRead4PublicMsg(msgID, loginName);
	}

	@Override
	public Integer getSubMessage(Integer id) {
		return guestbookDao.getSubMessage(id);
	}
}
