package dfh.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.GuestbookDao;
import dfh.dao.UniversalDao;
import dfh.model.AgentVisit;
import dfh.model.Guestbook;
import dfh.model.PtProfit;
import dfh.model.PtStatistical;
import dfh.model.Users;
import dfh.service.interfaces.GuestBookService;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

public class GuestBookServiceImpl implements GuestBookService {

	private static Logger log = Logger.getLogger(GuestBookServiceImpl.class);

	private GuestbookDao guestbookDao;

	private UniversalDao universalDao;

	public String deleteWords(Integer id) {
		log.info("start delete guestbook data id is" + id);
		String msg = null;
		if (id == null || id <= 0)
			msg = "提供的参数不正确";
		else {
			Guestbook guestbook = (Guestbook) guestbookDao.get(Guestbook.class, id);
			if (guestbook == null) {
				msg = "未找到任何数据";
			} else {
				if (guestbook.getReferenceId() != null) {
					Guestbook updateguestbook = (Guestbook) guestbookDao.get(Guestbook.class, guestbook.getReferenceId());
					if (updateguestbook != null) {
						updateguestbook.setRcount(updateguestbook.getRcount() - 1);
						guestbookDao.update(updateguestbook);
					}
				}
				guestbookDao.delete(Guestbook.class, id);
				log.info("end delete guestbook data success id is" + id);
			}
		}
		return msg;
	}

	public String leaveReplyWords(Integer referenceId, String content, String username, String email, String phone, String qq, String ip) {
		return leaveReplyWords(referenceId, 1, content, username, email, phone, qq, ip, null);
	}

	public String leaveReplyWordsAdmin(Integer referenceId, String content, String username, String email, String phone, String qq, String ip, Date createDate) {
		return leaveReplyWords(referenceId, 0, content, username, email, phone, qq, ip, createDate);
	}

	private String leaveReplyWords(Integer referenceId, Integer flag, String content, String username, String email, String phone, String qq, String ip, Date createDate) {
		String msg = null;
		if (content == null || ip == null || content.length() <= 0 || ip.length() <= 0)
			msg = "提供的参数不完整";
		else {
			log.info("start leaveReplyWordsAdmin guestbook");
			Guestbook guestBook = (Guestbook) guestbookDao.get(Guestbook.class, referenceId);
			if (guestBook == null)
				msg = "未找相任何数据";
			else {
				if (guestBook.getRcount() != null)
					guestBook.setRcount(guestBook.getRcount() + 1);
				else
					guestBook.setRcount(1);
				guestbookDao.saveOrUpdate(guestBook);

				if (createDate == null)
					createDate = DateUtil.now();

				Guestbook book = new Guestbook();
				book.setUsername(username);
				book.setReferenceId(guestBook.getId());
				book.setFlag(0);
				book.setIpaddress(ip);
				book.setCreatedate(new Timestamp(createDate.getTime()));
				book.setAdminname("客服管理员");
				book.setIsadmin(1);
				book.setTitle(guestBook.getTitle());
				book.setContent(content);
				book.setAdminstatus(1);
				book.setUserstatus(0);
				book.setUpdateid(guestBook.getUpdateid());
				guestbookDao.save(book);
				guestBook.setAdminstatus(1);
				guestBook.setUserstatus(0);
				guestbookDao.update(guestBook);
				log.info("success end leaveReplyWordsAdmin guestbook");
			}
		}
		return msg;
	}

	public String leaveWords(String title, String content, String username, String email, String phone, String qq, String ip) {
		log.info("start leaveWords guestbook");
		String msg = null;
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(ip))
			msg = "提供的参数不完整";
		else {
			Guestbook guestBook = new Guestbook(null, 1, null, ip, username, email, phone, qq, DateUtil.now(), title, content);
			guestbookDao.save(guestBook);
			log.info("end leaveWords guestbook");
		}
		return msg;
	}

	public Page queryWordsForBack(String adminname, String username, String title, Integer flag, Integer adminUserStatus, Integer isadmin, Date start, Date end, Integer beginIndex, Integer size) {
		log.info("start queryWordsForBack");
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		if (start != null)
			dc = dc.add(Restrictions.ge("createdate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createdate", end));
		if (StringUtils.isNotEmpty(title)) {
			dc = dc.add(Restrictions.or(Restrictions.like("title", "%" + title + "%"), Restrictions.like("content", "%" + title + "%")));
		}
		if (flag != null) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		if (adminUserStatus != null) {
			if (adminUserStatus == 0) {
				dc = dc.add(Restrictions.or(Restrictions.eq("adminstatus", 1), Restrictions.eq("userstatus", 0)));
			} else {
				dc = dc.add(Restrictions.or(Restrictions.eq("adminstatus", 0), Restrictions.eq("userstatus", 1)));
			}
		}
		if (isadmin != null) {
			dc = dc.add(Restrictions.eq("isadmin", isadmin));
		}
		if (adminname != null && !adminname.equals("")) {
			dc = dc.add(Restrictions.or(Restrictions.eq("adminname", adminname), Restrictions.eq("username", adminname)));
		}
		if (username != null && !username.equals("")) {
			dc = dc.add(Restrictions.or(Restrictions.eq("username", username), Restrictions.eq("adminname", adminname)));
		}
		dc = dc.add(Restrictions.isNull("referenceId"));
//		dc = dc.addOrder(Order.desc("createdate"));
		Order o = Order.desc("createdate");
		Page page = PageQuery.queryForPagenation(guestbookDao.getHibernateTemplate(), dc, beginIndex, size, o);
		log.info("end queryWordsForBack");
		return page;
	}

	public Page queryIplist(String username,String agent,String ip,String client_address,String agent_website,Date start, Date end,Integer beginIndex, Integer size){
		log.info("start queryWordsForBack");
		DetachedCriteria dc = DetachedCriteria.forClass(AgentVisit.class);
		if (username != null && !username.equals("")) {
			dc = dc.add(Restrictions.eq("loginname", username));
		}
		if (agent != null && !agent.equals("")) {
			dc = dc.add(Restrictions.eq("agent", agent));
		}
		if (ip != null && !ip.equals("")) {
			dc = dc.add(Restrictions.eq("client_ip", ip));
		}
		if (client_address != null && !client_address.equals("")) {
			dc = dc.add(Restrictions.like("source_come_url", "%" + client_address + "%"));
		}
		if (agent_website != null && !agent_website.equals("")) {
			dc = dc.add(Restrictions.eq("agent_website", agent_website));
		}
		if (start != null) {
			dc = dc.add(Restrictions.ge("createtime", start));
		}
		if (end != null) {
			dc = dc.add(Restrictions.lt("createtime", end));
		}
//		dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(guestbookDao.getHibernateTemplate(), dc, beginIndex, size, o);
		return page;
	}

	public Page queryWordsForFront(Integer beginIndex, Integer size) {
		log.info("start queryWordsForFront beginIndex:" + beginIndex + " and size" + size + "");
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.eq("flag", new Integer(0)));
		dc = dc.add(Restrictions.isNull("referenceId"));
//		dc = dc.addOrder(Order.desc("createdate"));
		Order o = Order.desc("createdate");
		Page page = PageQuery.queryForPagenation(guestbookDao.getHibernateTemplate(), dc, beginIndex, size, o);
		log.info("end queryWordsForFront");
		return page;
	}

	public Page queryReference(Integer id, Integer flag, Integer beginIndex, Integer size) {
		if (id == null)
			return null;
		Guestbook guestBook = (Guestbook) guestbookDao.get(Guestbook.class, id);
		if (guestBook != null) {
			guestBook.setAdminstatus(1);
			guestbookDao.update(guestBook);
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.or(Restrictions.eq("id", id), Restrictions.eq("referenceId", id)));
		if (flag != null) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
//		dc = dc.addOrder(Order.asc("createdate"));
		Order o = Order.asc("createdate");
		Page page = PageQuery.queryForPagenation(guestbookDao.getHibernateTemplate(), dc, beginIndex, size, o);
		return page;
	}

	public String auditing(Integer id) {
		String msg = null;
		if (id == null)
			msg = "未找到任何数据";
		else {
			Guestbook guestBook = (Guestbook) guestbookDao.get(Guestbook.class, id);
			if (guestBook == null)
				msg = "未找相任何数据";
			else {
				guestBook.setFlag(new Integer(0));
				guestbookDao.update(guestBook);
			}
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public String oficeLeaveWords(Integer type, String username,Integer usernameType, String title, Integer flag, Date createDate,String content, String ip , Date start , Date end ,String csType,String isdeposit) {
		log.info("start oficeLeaveWords guestbook");
		String msg = null;
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(ip)) {
			msg = "提供的参数不完整";
		} else {
			if (createDate == null) {
				createDate = DateUtil.now();
			}
			// 0 个人 1 群发
			if (type == 0) {
				if(username.contains(",")){
					String name[]= username.split(",");
					for (String stringName : name) {
						Guestbook guestBook = new Guestbook();
						guestBook.setUsername(stringName);
						guestBook.setFlag(flag);
						guestBook.setIpaddress(ip);
						guestBook.setCreatedate(new Timestamp(createDate.getTime()));
						guestBook.setTitle(title);
						guestBook.setContent(content);
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setIsadmin(0);
						guestBook.setMessage(9);
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
						log.info("end oficeLeaveWords guestbook");
					}
				}else{
						DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
						dc = dc.add(Restrictions.eq("flag", 0));
						dc = dc.add(Restrictions.eq("loginname", username));
						List<Users> list = universalDao.findByCriteria(dc);
						if (list == null || list.size() <= 0) {
							return username+"账号不存在或者已经被禁用！全部不成功！";
						}
						Guestbook guestBook = new Guestbook();
						guestBook.setUsername(username);
						guestBook.setFlag(flag);
						guestBook.setIpaddress(ip);
						guestBook.setCreatedate(new Timestamp(createDate.getTime()));
						guestBook.setTitle(title);
						guestBook.setContent(content);
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setIsadmin(0);
						guestBook.setMessage(9);//9代表是单个的 发送
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
						log.info("end oficeLeaveWords guestbook");
				}
			} else if(type == 1){
				if (usernameType == 7) {
					Guestbook guestBook = new Guestbook();
					//guestBook.setUsername("全部会员");
					guestBook.setFlag(flag);
					guestBook.setIpaddress(ip);
					guestBook.setCreatedate(new Timestamp(createDate.getTime()));
					guestBook.setTitle(title);
					guestBook.setContent(content);
					guestBook.setAdminstatus(1);
					guestBook.setUserstatus(0);
					guestBook.setIsadmin(0);
					guestBook.setMessage(7);//7 全部会员 8全部代理    9个人群发 10代表是按照客服推荐码群发
					guestBook.setAdminname("客服管理员");
					guestbookDao.save(guestBook);
					log.info("end oficeLeaveWords guestbook");
				} else if (usernameType == 8) {
					/*DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
					dc = dc.add(Restrictions.eq("flag", 0));
					dc = dc.add(Restrictions.eq("role", UserRole.AGENT.getCode()));
					List<Users> list = universalDao.findByCriteria(dc);
					if (list == null || list.size() <= 0) {
						return "获取代理账号列表失败！";
					}
					for (Users users : list) {*/
						Guestbook guestBook = new Guestbook();
						//guestBook.setUsername("全部代理");
						guestBook.setFlag(flag);
						guestBook.setIpaddress(ip);
						guestBook.setCreatedate(new Timestamp(createDate.getTime()));
						guestBook.setTitle(title);
						guestBook.setContent(content);
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setIsadmin(0);
						guestBook.setMessage(8);//7 全部会员 8全部代理    9个人群发 10代表是按照客服推荐码群发
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
						log.info("end oficeLeaveWords guestbook");
					//}
				} else {// 这里是按照玩家等级来放
				//	DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				//	dc = dc.add(Restrictions.eq("flag", 0));
				//	dc = dc.add(Restrictions.eq("level", usernameType));
				//	dc = dc.add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
				//	List<Users> list = universalDao.findByCriteria(dc);
				//	if (list == null || list.size() <= 0) {
				//		return "获取账号列表失败！";
				//	}
				//	for (Users users : list) {
						Guestbook guestBook = new Guestbook();
				//		guestBook.setUsername(users.getLoginname());
						guestBook.setFlag(flag);
						guestBook.setIpaddress(ip);
						guestBook.setCreatedate(new Timestamp(createDate.getTime()));
						guestBook.setTitle(title);
						guestBook.setContent(content);
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setIsadmin(0);
						guestBook.setAdminname("客服管理员");
						guestBook.setMessage(usernameType);//8代表全部代理 7代表全部会员 玩家等级  这里 usernameType代表该等级下对应的玩家0--6
						guestbookDao.save(guestBook);
						log.info("end oficeLeaveWords guestbook");
				//	}
				}
			}else if(type == 2){//按照客服推荐码群发
				
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc = dc.add(Restrictions.eq("flag", 0));
				dc = dc.add(Restrictions.eq("intro", csType));
				dc = dc.add(Restrictions.ge("createtime", start));
				dc = dc.add(Restrictions.le("createtime", end));
				List<Users> list = universalDao.findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "账号不存在或者已经被禁用！全部不成功！";
				}
				for (Users users : list) {
					/*Guestbook guestBook = new Guestbook();
					guestBook.setUsername(csType);//--------如果是按照客服推荐码群发 则用户名字里保存推荐码
					guestBook.setFlag(flag);
					guestBook.setIpaddress(ip);
					guestBook.setCreatedate(new Timestamp(createDate.getTime()));
					guestBook.setTitle(title);
					guestBook.setContent(content);
					guestBook.setAdminstatus(1);
					guestBook.setUserstatus(0);
					guestBook.setMessage(10);//-------------10代表是按照客服推荐码群发
					guestBook.setIsadmin(0);*/
					Guestbook guestBook = new Guestbook();
					guestBook.setUsername(users.getLoginname());
					guestBook.setFlag(flag);
					guestBook.setIpaddress(ip);
					guestBook.setCreatedate(new Timestamp(createDate.getTime()));
					guestBook.setTitle(title);
					guestBook.setContent(content);
					guestBook.setAdminstatus(1);
					guestBook.setUserstatus(0);
					guestBook.setIsadmin(0);
					guestBook.setMessage(10);
					guestBook.setAdminname("客服管理员");
					guestbookDao.save(guestBook);
					log.info("end oficeLeaveWords guestbook");
				}
			}else if(type == 3){
				//根据代理账号批量发站内信，此时的username存放的是代理账号
				StringBuffer agents = new StringBuffer();
				if(username.contains(",")){
					String[] agentArr = username.split(",");
					for (String agent : agentArr) {
						agents.append("'"+agent+"',");
					}
					agents = agents.delete(agents.length()-1, agents.length());
				}else{
					agents.append("'"+username+"'");
				}
				
				List<String> usernames = universalDao.queryUserByCondition(agents.toString(),isdeposit,usernameType);
				if (usernames == null || usernames.size() <= 0) {
					return "账号不存在！全部不成功！";
				}
				List<Guestbook> guestBookList = new ArrayList<Guestbook>();
				for (String uName : usernames) {
					Guestbook guestBook = new Guestbook();
					guestBook.setUsername(uName);
					guestBook.setFlag(flag);
					guestBook.setIpaddress(ip);
					guestBook.setCreatedate(new Timestamp(createDate.getTime()));
					guestBook.setTitle(title);
					guestBook.setContent(content);
					guestBook.setAdminstatus(1);
					guestBook.setUserstatus(0);
					guestBook.setIsadmin(0);
					guestBook.setMessage(11); //11代表是按照代理账号群发
					guestBook.setAdminname("代理管理员");
					guestBookList.add(guestBook);
				}
				guestbookDao.saveOrUpdateAll(guestBookList);
				log.info("end oficeLeaveWords guestbook");
			}
		}
		return msg;

	}
	
	public Page getPtList(String loginname,Date start, Date end, Integer pageIndex, Integer size,String order,String by){
		DetachedCriteria dc = DetachedCriteria.forClass(PtProfit.class);
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		return PageQuery.queryForPagenationWithStatistics(guestbookDao.getHibernateTemplate(), dc, pageIndex, size, "amount", "betCredit", null, o);
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return guestbookDao.getHibernateTemplate();
	}
	
	public Page getPtListInfo(String loginname,Date start, Date end, Integer pageIndex, Integer size,String order,String by){
		DetachedCriteria dc = DetachedCriteria.forClass(PtStatistical.class);
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (start != null)
			dc = dc.add(Restrictions.ge("playtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("playtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		return PageQuery.queryForPagenationWithStatistics(guestbookDao.getHibernateTemplate(), dc, pageIndex, size, "payOut", null, null, o);
	}
	
	@Override
	public void batchDelete(String ids) throws Exception {
		guestbookDao.batchDelete(ids);
	}

	public GuestbookDao getGuestbookDao() {
		return guestbookDao;
	}

	public void setGuestbookDao(GuestbookDao guestbookDao) {
		this.guestbookDao = guestbookDao;
	}

	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	public List<PtProfit> getPtProfitList(Date start, Date end) {
		// TODO Auto-generated method stub
		return guestbookDao.getPtProfitList(start, end);
	}

}
