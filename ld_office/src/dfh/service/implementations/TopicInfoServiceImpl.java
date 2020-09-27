package dfh.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.util.PushUtil;
import dfh.dao.ReplyInfoDao;
import dfh.dao.TopicInfoDao;
import dfh.dao.TopicStatusDao;
import dfh.dao.UniversalDao;
import dfh.model.AgentVisit;
import dfh.model.Guestbook;
import dfh.model.ReplyInfo;
import dfh.model.TopicInfo;
import dfh.model.TopicStatus;
import dfh.model.Users;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.ITopicInfoService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

public class TopicInfoServiceImpl implements ITopicInfoService {

	private static Logger log = Logger.getLogger(TopicInfoServiceImpl.class);

	private TopicInfoDao topicInfoDao;

	private TopicStatusDao topicStatusDao;

	private ReplyInfoDao replyInfoDao;

	private UniversalDao universalDao;

	@Override
	public String deleteYouHuiTopicInfo(Integer id) {
		String msg = null;
		if (id == null || id <= 0)
			msg = "提供的参数不正确";
		else {
			// 删除状态
			topicStatusDao.deleteById(id);
		}
		return msg;
	}

	// 删除主贴，同时清空回复贴和状态表。
	public String deleteWords(Integer id) {
		log.info("start delete topicInfo data id is" + id);
		String msg = null;
		if (id == null || id <= 0)
			msg = "提供的参数不正确";
		else {
			TopicInfo topicInfo = (TopicInfo) topicInfoDao.get(TopicInfo.class, id);
			if (topicInfo == null) {
				msg = "未找到任何数据";
			} else {
				topicInfoDao.delete(TopicInfo.class, id);
				// 删除回复
				replyInfoDao.deleteByTopicId(id);
				// 删除状态
				topicStatusDao.deleteByTopicId(id);
				log.info("end delete topicInfo data success id is" + id);
			}
		}
		return msg;
	}

	public String leaveReplyWords(Integer referenceId, String content, String username, String email, String phone,
			String qq, String ip) {
		return leaveReplyWords(referenceId, 1, content, username, email, phone, qq, ip, null);
	}

	public String leaveReplyWordsAdmin(Integer referenceId, String content, String username, String email, String phone,
			String qq, String ip, Date createDate) {
		return leaveReplyWords(referenceId, 0, content, username, email, phone, qq, ip, createDate);
	}

	// 回复
	private String leaveReplyWords(Integer topicId, Integer flag, String content, String username, String email,
			String phone, String qq, String ip, Date createDate) {
		String msg = null;
		if (content == null || ip == null || content.length() <= 0 || ip.length() <= 0) {
			msg = "提供的参数不完整";
		} else {
			log.info("start leaveReplyWordsAdmin topicInfo");
			TopicInfo topicInfo = (TopicInfo) topicInfoDao.get(TopicInfo.class, topicId);
			if (topicInfo == null) {
				msg = "未找相任何数据";
			} else {
				if (topicInfo.getReCount() != null) {
					topicInfo.setReCount(topicInfo.getReCount() + 1);
				} else {
					topicInfo.setReCount(1);
				}
				// 更新回复数
				topicInfoDao.saveOrUpdate(topicInfo);
				// 设置用户未读
				topicStatusDao.updateUserUnRead(topicId);

				if (createDate == null) {
					createDate = DateUtil.now();
				}
				// 新增回复
				ReplyInfo replyInfo = new ReplyInfo();
				replyInfo.setTopicId(topicId);
				replyInfo.setContent(content);
				replyInfo.setCreateTime(new Timestamp(createDate.getTime()));
				replyInfo.setCreateUname(username);
				replyInfo.setReplyType(0);
				replyInfo.setIpAddress(ip);
				replyInfoDao.save(replyInfo);
				log.info("success end leaveReplyWordsAdmin topicInfo");
			}
		}
		return msg;
	}

	public String leaveWords(String title, String content, String username, String email, String phone, String qq,
			String ip) {
		log.info("start leaveWords topicInfo");
		String msg = null;
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(ip))
			msg = "提供的参数不完整";
		else {
			// 100 全部会员 101全部代理 102个人群发 103代表是按照客服推荐码群发104代表是按照代理账号群发 105代表管理员
			// 大于0对应玩家等级
			TopicInfo topicInfo = new TopicInfo(1, 0, ip, username, DateUtil.now(), title, content, 0, 103);
			topicInfoDao.save(topicInfo);
			log.info("end leaveWords topicInfo");
		}
		return msg;
	}

	public Page queryWordsForBack(String createUname, String receiveUname, String title, Integer flag, Integer isRead,
			Integer topicType, Date start, Date end, Integer beginIndex, Integer size) {
		log.info("start queryTopicInfoList");
		// DetachedCriteria dc = DetachedCriteria.forClass(TopicInfo.class);

		StringBuilder totalSql = new StringBuilder("select count(1) from  (");
		StringBuilder recordSql = new StringBuilder(
				" select t.id,	title,	content,	t.create_time,	create_uname,	t.ip_address,	flag,	re_count,	topic_type,	user_name_type,	topic_status, is_admin_read from topicinfo t where 1=1 ");

		if (start != null)
			recordSql.append(" and create_time >= date(" + DateUtil.fmtyyMMddHHmmss(start) + ")");
		if (end != null)
			recordSql.append(" and create_time <= date(" + DateUtil.fmtyyMMddHHmmss(end) + ")");
		if (StringUtils.isNotEmpty(title)) {
			recordSql.append(" and (title like '%" + title + "%'  or content like '%" + title + "%')");
		}
		if (flag != null) {
			recordSql.append(" and flag = " + flag + "");
		}
		if (topicType != null) {
			recordSql.append(" and topic_type = " + topicType + "");
		}
//		if (createUname != null && !createUname.equals("")) {
//			recordSql.append(" and create_uname = '" + createUname + "'");
//		}
		if (createUname != null && !createUname.equals("")) {
			recordSql.append(" and (create_uname = '" + createUname + "' or t.id in(select s.topic_id from topicstatus s where s.receive_uname='" + createUname + "' ))");
		}
		
		/*
		 * recordSql.
		 * append(" union all (select t.id,title,content,t.create_time,create_uname,t.ip_address,flag,re_count,topic_type,user_name_type,topic_status,receive_uname,is_user_read,is_admin_read"
		 * )
		 * .append(" from topicinfo t left join topicstatus s on t.id = s.topic_id where  t.topic_status = 0  and is_valid = 0 "
		 * ); if (start != null) recordSql.append(" and t.create_time >= date("
		 * + DateUtil.fmtyyMMddHHmmss(start) + ")"); if (end != null)
		 * recordSql.append(" and t.create_time <= date(" +
		 * DateUtil.fmtyyMMddHHmmss(end) + ")"); if
		 * (StringUtils.isNotEmpty(title)) {
		 * recordSql.append(" and (t.title like '%" + title +
		 * "%'  or content like '%" + title + "%')"); } if (flag != null) {
		 * recordSql.append(" and flag = " + flag + ""); } if (isRead != null) {
		 * if (isRead == 0) { recordSql.append(" and is_admin_read = 0 "); }
		 * else { recordSql.append(" and is_admin_read = 1 "); } } if (topicType
		 * != null) { recordSql.append(" and topic_type = " + topicType + ""); }
		 * if (createUname != null && !createUname.equals("")) {
		 * recordSql.append(" and (create_uname = '" + createUname +
		 * "' or receive_uname = '" + createUname + "')"); } if (receiveUname !=
		 * null && !receiveUname.equals("")) {
		 * recordSql.append(" and (create_uname = '" + receiveUname +
		 * "' or receive_uname = '" + receiveUname + "')"); }
		 * 
		 * recordSql.append(")) a where 1=1 ");
		 */

		recordSql.append(" order by create_time desc");
		totalSql.append(recordSql.toString()).append(") a ");
		
		if (beginIndex == null || beginIndex.intValue() == 0) {
			beginIndex = Page.PAGE_BEGIN_INDEX;
		}
		if (size == null || size.intValue() == 0) {

			size = Page.PAGE_DEFAULT_SIZE;
		}
		recordSql.append(" LIMIT " + ((beginIndex - 1) * size) + ", " + size);
		Page page = PageQuery.queryForPagenation(topicInfoDao.getHibernateTemplate(), recordSql.toString(),
				totalSql.toString(), beginIndex, size);
		log.info("end queryTopicInfoList");
		return page;
	}
	
	
	
	public Page queryYouHuiTopic(String createUname, String receiveUname, String title, Integer flag, Integer isRead,
			Integer topicType, Date start, Date end, Integer beginIndex, Integer size) {
		log.info("start queryYouHuiTopic");
		// DetachedCriteria dc = DetachedCriteria.forClass(TopicInfo.class);

		StringBuilder totalSql = new StringBuilder("select count(1) from  (");
		StringBuilder recordSql = new StringBuilder(
				" select s.id,s.topic_id,s.receive_uname,s.ip_address,s.is_user_read,s.create_time,s.is_valid,s.title,s.content from topicstatus s where topic_id = -1 ");
		if (start != null)
			recordSql.append(" and create_time >= date(" + DateUtil.fmtyyMMddHHmmss(start) + ")");
		if (end != null)
			recordSql.append(" and create_time <= date(" + DateUtil.fmtyyMMddHHmmss(end) + ")");
		if (StringUtils.isNotEmpty(title)) {
			recordSql.append(" and (title like '%" + title + "%'  or content like '%" + title + "%')");
		}
		if (createUname != null && !createUname.equals("")) {
			recordSql.append(" and receive_uname = '" + createUname + "'");
		}
		if (isRead != null && !isRead.equals("")) {
			recordSql.append(" and is_user_read = "+isRead+" ");
		}
		recordSql.append(" order by create_time desc");
		totalSql.append(recordSql.toString()).append(") a ");
		
		if (beginIndex == null || beginIndex.intValue() == 0) {
			beginIndex = Page.PAGE_BEGIN_INDEX;
		}
		if (size == null || size.intValue() == 0) {

			size = Page.PAGE_DEFAULT_SIZE;
		}
		recordSql.append(" LIMIT " + ((beginIndex - 1) * size) + ", " + size);
		Page page = PageQuery.queryForPagenation(topicInfoDao.getHibernateTemplate(), recordSql.toString(),
				totalSql.toString(), beginIndex, size);
		log.info("end queryYouHuiTopic");
		return page;
	}
	
	public Page queryTopicUserList(Integer topicId,String createUname,  Integer isRead, Integer beginIndex, Integer size) {
		log.info("start queryTopicUserList");
		StringBuilder totalSql = new StringBuilder("select count(1) from  (");
		StringBuilder recordSql = new StringBuilder(
				" select t.id,t.create_uname,s.receive_uname,s.is_user_read,t.title from topicinfo t left join  topicstatus s on  t.id = s.topic_id where 1=1 and t.id = "+topicId+"");
		if (createUname != null && !createUname.equals("")) {
			recordSql.append(" and receive_uname = '" + createUname + "'");
		}
		if (isRead != null && !isRead.equals("")) {
			recordSql.append(" and is_user_read = "+isRead+" ");
		}
		totalSql.append(recordSql.toString()).append(") a ");
		
		if (beginIndex == null || beginIndex.intValue() == 0) {
			beginIndex = Page.PAGE_BEGIN_INDEX;
		}
		if (size == null || size.intValue() == 0) {
			
			size = Page.PAGE_DEFAULT_SIZE;
		}
		recordSql.append(" LIMIT " + ((beginIndex - 1) * size) + ", " + size);
		Page page = PageQuery.queryForPagenation(topicInfoDao.getHibernateTemplate(), recordSql.toString(),
				totalSql.toString(), beginIndex, size);
		log.info("end queryTopicUserList");
		return page;
	}
	

	public Page queryWordsForFront(Integer beginIndex, Integer size) {
		log.info("start queryWordsForFront beginIndex:" + beginIndex + " and size" + size + "");
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.eq("flag", new Integer(0)));
		dc = dc.add(Restrictions.isNull("referenceId"));
		Order o = Order.desc("createdate");
		Page page = PageQuery.queryForPagenation(topicInfoDao.getHibernateTemplate(), dc, beginIndex, size, o);
		log.info("end queryWordsForFront");
		return page;
	}

	public TopicInfo queryTopicInfo(Integer id) {
		if (id == null)
			return null;
		TopicInfo topicInfo = (TopicInfo) topicInfoDao.get(TopicInfo.class, id);
		return topicInfo;
	}
	
	public Page queryReference(Integer id, Integer flag, Integer beginIndex, Integer size) {
		if (id == null)
			return null;
		// 修改管理员已读状态
		topicInfoDao.updateAdminRead(id, "客服管理员");

		StringBuilder recordSql = new StringBuilder();
//		recordSql.append("select id,content,title,create_time,create_uname,ip_address,flag from( ");
		recordSql
				.append(" select t.id,t.content,t.title,t.create_time,t.create_uname,t.ip_address,t.flag  from topicinfo t where t.id ="
						+ id + " ");
//		recordSql
//				.append(" (select r.topic_id,r.content,'' as title ,r.create_time,r.create_uname,r.ip_address,0 as flag from replyinfo r where r.topic_id ="
//						+ id + ")) a ");
		recordSql.append("  order by create_time asc ");
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(1) from (" + recordSql.toString() + ") a");
		Page page = PageQuery.queryForPagenation(topicInfoDao.getHibernateTemplate(), recordSql.toString(),
				totalSql.toString(), beginIndex, size);
		return page;
	}

	/**
	 * 审核
	 */
	public String auditing(Integer id) {
		String msg = null;
		if (id == null)
			msg = "未找到任何数据";
		else {
			TopicInfo topicInfo = (TopicInfo) topicInfoDao.get(TopicInfo.class, id);
			if (topicInfo == null)
				msg = "未找相任何数据";
			else {
				topicInfo.setFlag(new Integer(0));
				topicInfoDao.update(topicInfo);
			}
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public String saveLeaveWords(Integer type, String username, Integer usernameType, String title, Integer flag,
			Date createDate, String content, String ip, Date start, Date end, String csType, String isdeposit,
			String operateName) {

		log.info("start oficeLeaveWords guestbook");
		String msg = null;
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(ip)) {
			msg = "提供的参数不完整";
		} else {
			if (createDate == null) {
				createDate = DateUtil.now();
			}
			// 0 个人 1 群发
			// UserNameType 100 全部会员 101全部代理 102个人群发
			// 103代表是按照客服推荐码群发104代表是按照代理账号群发 105代表管理员 大于0对应玩家等级
			if (type == 0) {
				if (username.contains(",")) {
					String name[] = username.split(",");
					// for (String stringName : name) {
					// DetachedCriteria dc =
					// DetachedCriteria.forClass(Users.class);
					// dc = dc.add(Restrictions.eq("flag", 0));
					// dc = dc.add(Restrictions.eq("loginname", stringName));
					// List<Users> list = universalDao.findByCriteria(dc);
					// if (list == null || list.size() <= 0) {
					// return stringName+"账号不存在或者已经被禁用！全部不成功！";
					// }
					// }
					TopicInfo topicInfo = new TopicInfo();
					topicInfo.setFlag(flag);
					topicInfo.setIpAddress(ip);
					topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
					topicInfo.setTitle(title);
					topicInfo.setContent(content);
					topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
					topicInfo.setUserNameType(Constants.TOPIC_SEND_TYPE_PERSONAL_GROUP);// 102个人群发
					topicInfo.setTopicStatus(Constants.TOPIC_STATUS_PERSONAL);
					topicInfo.setCreateUname("(" + operateName + ")客服管理员");
					topicInfo.setIsAdminRead(1);
					Integer topicId = (Integer) topicInfoDao.save(topicInfo);
					Map<String, String> hashMap = new HashMap<String, String>();
					for (String stringName : name) {
						if (null != topicId) {
							if (null == hashMap.get(stringName)) {
								hashMap.put(stringName, stringName);
								TopicStatus topicStatus = new TopicStatus();
								topicStatus.setCreateTime(new Timestamp(createDate.getTime()));
								topicStatus.setTopicId(topicId);
								topicStatus.setIpAddress(ip);// 发送者ip地址
								topicStatus.setIsUserRead(0);
								topicStatus.setReceiveUname(stringName);
								topicStatusDao.save(topicStatus);
							}
						} else {
							return "发帖失败";
						}
					}
					log.info("end saveTopicInfo topicInfo");
				} else {
					DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
					dc = dc.add(Restrictions.eq("flag", 0));
					dc = dc.add(Restrictions.eq("loginname", username));
					List<Users> list = universalDao.findByCriteria(dc);
					if (list == null || list.size() <= 0) {
						return username + "账号不存在或者已经被禁用！全部不成功！";
					}
					TopicInfo topicInfo = new TopicInfo();
					topicInfo.setFlag(flag);
					topicInfo.setIpAddress(ip);
					topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
					topicInfo.setTitle(title);
					topicInfo.setContent(content);
					topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
					topicInfo.setUserNameType(Constants.TOPIC_SEND_TYPE_PERSONAL_GROUP);// 102代表单个发送
					topicInfo.setTopicStatus(Constants.TOPIC_STATUS_PERSONAL);
					topicInfo.setCreateUname("(" + operateName + ")客服管理员");
					topicInfo.setIsAdminRead(1);
					Integer topicId = (Integer) topicInfoDao.save(topicInfo);

					if (null != topicId) {
						TopicStatus topicStatus = new TopicStatus();
						topicStatus.setCreateTime(new Timestamp(createDate.getTime()));
						topicStatus.setTopicId(topicId);
						topicStatus.setIpAddress(ip);// 发送者ip地址
						topicStatus.setIsUserRead(0);
						topicStatus.setReceiveUname(username);
						topicStatusDao.save(topicStatus);
					} else {
						return "发帖失败";
					}
					log.info("end saveTopicInfo topicInfo");
				}
			} else if (type == 1) {
				if (usernameType == Constants.TOPIC_SEND_TYPE_ALL_MEMBER) {
					TopicInfo topicInfo = new TopicInfo();
					topicInfo.setFlag(flag);
					topicInfo.setIpAddress(ip);
					topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
					topicInfo.setTitle(title);
					topicInfo.setContent(content);
					topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
					topicInfo.setUserNameType(Constants.TOPIC_SEND_TYPE_ALL_MEMBER);// 100
																			// 全部会员
					topicInfo.setTopicStatus(Constants.TOPIC_STATUS_GROUP);
					topicInfo.setCreateUname("(" + operateName + ")客服管理员");
					topicInfo.setIsAdminRead(1);
					topicInfoDao.save(topicInfo);
					log.info("end saveTopicInfo topicInfo");
				} else if (usernameType == Constants.TOPIC_SEND_TYPE_ALL_AGENT) {
					TopicInfo topicInfo = new TopicInfo();
					topicInfo.setFlag(flag);
					topicInfo.setIpAddress(ip);
					topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
					topicInfo.setTitle(title);
					topicInfo.setContent(content);
					topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
					topicInfo.setUserNameType(Constants.TOPIC_SEND_TYPE_ALL_AGENT);// 101全部代理
					topicInfo.setTopicStatus(Constants.TOPIC_STATUS_GROUP);
					topicInfo.setCreateUname("(" + operateName + ")客服管理员");
					topicInfo.setIsAdminRead(1);
					topicInfoDao.save(topicInfo);
					log.info("end saveTopicInfo topicInfo");
				} else {// 这里是按照玩家等级来放
					TopicInfo topicInfo = new TopicInfo();
					topicInfo.setFlag(flag);
					topicInfo.setIpAddress(ip);
					topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
					topicInfo.setTitle(title);
					topicInfo.setContent(content);
					topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
					topicInfo.setUserNameType(usernameType);// 玩家等级
					topicInfo.setTopicStatus(Constants.TOPIC_STATUS_GROUP);
					topicInfo.setCreateUname("(" + operateName + ")客服管理员");
					topicInfo.setIsAdminRead(1);
					topicInfoDao.save(topicInfo);
					log.info("end saveTopicInfo topicInfo");
				}
			} else if (type == 2) {// 按照客服推荐码群发
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc = dc.add(Restrictions.eq("flag", 0));
				dc = dc.add(Restrictions.eq("intro", csType));
				dc = dc.add(Restrictions.ge("createtime", start));
				dc = dc.add(Restrictions.le("createtime", end));
				List<Users> list = universalDao.findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "账号不存在或者已经被禁用！全部不成功！";
				}
				TopicInfo topicInfo = new TopicInfo();
				topicInfo.setFlag(flag);
				topicInfo.setIpAddress(ip);
				topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
				topicInfo.setTitle(title);
				topicInfo.setContent(content);
				topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
				topicInfo.setUserNameType(Constants.TOPIC_SEND_TYPE_CODE_GROUP);// 103代表是按照客服推荐码群发
				topicInfo.setTopicStatus(Constants.TOPIC_STATUS_PERSONAL);
				topicInfo.setCreateUname("(" + operateName + ")客服管理员");
				topicInfo.setIsAdminRead(1);
				Integer topicId = (Integer) topicInfoDao.save(topicInfo);

				for (Users users : list) {
					if (null != topicId) {
						TopicStatus topicStatus = new TopicStatus();
						topicStatus.setCreateTime(new Timestamp(createDate.getTime()));
						topicStatus.setTopicId(topicId);
						topicStatus.setIpAddress(ip);// 发送者ip地址
						topicStatus.setIsUserRead(0);
						topicStatus.setReceiveUname(users.getLoginname());
						topicStatusDao.save(topicStatus);
					} else {
						return "发帖失败";
					}
				}

				log.info("end saveTopicInfo topicInfo");
			} else if (type == 3) {
				Date insertDate = new Date();
				// 根据代理账号批量发站内信，此时的username存放的是代理账号
				StringBuffer agents = new StringBuffer();
				if (username.contains(",")) {
					String[] agentArr = username.split(",");
					for (String agent : agentArr) {
						agents.append("'" + agent + "',");
					}
					agents = agents.delete(agents.length() - 1, agents.length());
				} else {
					agents.append("'" + username + "'");
				}

				List<String> usernames = universalDao.queryUserByCondition(agents.toString(), isdeposit, usernameType);
				if (usernames == null || usernames.size() <= 0) {
					return "账号不存在！全部不成功！";
				}
				TopicInfo topicInfo = new TopicInfo();
				topicInfo.setFlag(flag);
				topicInfo.setIpAddress(ip);
				topicInfo.setCreateTime(new Timestamp(createDate.getTime()));
				topicInfo.setTitle(title);
				topicInfo.setContent(content);
				topicInfo.setTopicType(Constants.TOPIC_TOPIC_TYPE_USER);
				topicInfo.setUserNameType(Constants.TOPIC_SEND_TYPE_AGENT_GROUP);// 104代表是按照客服推荐码群发
				topicInfo.setTopicStatus(Constants.TOPIC_STATUS_PERSONAL);
				topicInfo.setCreateUname("(" + operateName + ")客服管理员");
				topicInfo.setIsAdminRead(1);
				Integer topicId = (Integer) topicInfoDao.save(topicInfo);

				for (String userName : usernames) {

					if (null != topicId) {
						TopicStatus topicStatus = new TopicStatus();
						topicStatus.setCreateTime(new Timestamp(createDate.getTime()));
						topicStatus.setTopicId(topicId);
						topicStatus.setIpAddress(ip);// 发送者ip地址

						topicStatus.setIsUserRead(0);
						topicStatus.setReceiveUname(userName);
						topicStatusDao.save(topicStatus);
					} else {
						return "发帖失败";
					}
				}

			}
		}
		return msg;

	}

	public Page queryIplist(String username, String agent, String ip, String client_address, String agent_website,
			Date start, Date end, Integer beginIndex, Integer size) {
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
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(topicInfoDao.getHibernateTemplate(), dc, beginIndex, size, o);
		return page;
	}

	@Override
	public void batchDeleteTopic() throws Exception {
		// TODO Auto-generated method stub
		Calendar cd = Calendar.getInstance();
		String deleteIds = "";
		cd.add(Calendar.DAY_OF_MONTH, -15);
		String lastDate = DateUtil.formatDateForStandard(cd.getTime());
		List list = topicInfoDao.findTopicHistory(lastDate);
		if (null != list && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				Object obj[] = (Object[]) list.get(j);

				Integer topicType = new Integer(obj[8].toString());
				Integer id = new Integer(obj[0].toString());
				deleteIds = deleteIds + id.intValue() + ",";
			}
			if (!"".equals(deleteIds)) {
				deleteIds = deleteIds.substring(0, deleteIds.length() - 1);
				topicInfoDao.batchDelete(deleteIds);
				topicStatusDao.batchDelete(deleteIds);
				replyInfoDao.batchDelete(deleteIds);
			}
		}
	}

	@Override
	public void batchDelete(String ids) throws Exception {

		// 区分是管理发的还是玩家发的，管理员发的可以进行物理删除，玩家发的只能进行逻辑删除。
		List list = topicInfoDao.findTopicDetail(ids);
		String deleteIds = "";
		String disabledIds = "";
		if (null != list && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				Object obj[] = (Object[]) list.get(j);

				Integer topicType = new Integer(obj[8].toString());
				Integer id = new Integer(obj[0].toString());
				// 发帖给玩家
				if (topicType.equals(Constants.TOPIC_TOPIC_TYPE_USER)) {
					deleteIds = deleteIds + id.intValue() + ",";
				} else {
					disabledIds = disabledIds + id.intValue() + ",";
				}

			}
			if (!"".equals(deleteIds)) {
				deleteIds = deleteIds.substring(0, deleteIds.length() - 1);
				topicInfoDao.batchDelete(deleteIds);
				topicStatusDao.batchDelete(deleteIds);
				replyInfoDao.batchDelete(deleteIds);
			}
			if (!"".equals(disabledIds)) {
				disabledIds = disabledIds.substring(0, disabledIds.length() - 1);
				topicStatusDao.disabledTopic(disabledIds);
			}
		}
	}

	@Override
	public String batchPushToApp(String ids) throws Exception {

		List list = topicInfoDao.batchTopicApp(ids);

		String content = "";
		List<String> sourceUsernameList = null;

		if (list != null && list.size() > 0) {

			sourceUsernameList = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {

				Object[] obj = (Object[]) list.get(i);

				content = obj[2].toString();
				Integer status = new Integer(obj[10].toString());
				Integer userNameType = new Integer(obj[9].toString());

				String title = obj[1].toString();

				if (status.equals(Constants.TOPIC_STATUS_GROUP)) {// 群发

					if (list.size() > 1) {

						return "为了推送效率达到最佳，不同内容，不同群发方式的站内信请分开推送！";

					}

					// 按会员等级的
					if (userNameType < 99) {
						DetachedCriteria userDc = DetachedCriteria.forClass(Users.class);
						userDc = userDc.add(Restrictions.eq("flag", 0));
						userDc = userDc.add(Restrictions.eq("level", userNameType));
						userDc = userDc.add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
						List<Users> userList = universalDao.findByCriteria(userDc);

						for (Users users : userList) {

							sourceUsernameList.add(users.getLoginname());
						}
					}
					// 全体会员
					if (userNameType == Constants.TOPIC_SEND_TYPE_ALL_MEMBER) {

						return PushUtil.pushStationLetter(content, "all", sourceUsernameList);
					}

					// 全体代理
					if (userNameType == Constants.TOPIC_SEND_TYPE_ALL_AGENT) {
						DetachedCriteria agentDc = DetachedCriteria.forClass(Users.class);
						agentDc = agentDc.add(Restrictions.eq("flag", 0));
						agentDc = agentDc.add(Restrictions.eq("role", UserRole.AGENT.getCode()));
						List<Users> agentList = universalDao.findByCriteria(agentDc);

						for (Users users : agentList) {

							sourceUsernameList.add(users.getLoginname());
						}
					}

				} else {// 指定用户

					if (i > 0) {
						Object[] oldObj = (Object[]) list.get(i - 1);
						String oldTitle = oldObj[1].toString();
						if (!StringUtils.equals(title, oldTitle)) {// 按理应该比较发送内容，如果内容过长，效率比较低。

							return "为了推送效率达到最佳，不同内容，不同群发方式的站内信请分开推送！";
						}

					}
					String receiveUname = obj[11].toString();
					sourceUsernameList.add(receiveUname);

				}

			}

		}

		return PushUtil.pushStationLetter(content, "list", sourceUsernameList);

	}

	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	public TopicInfoDao getTopicInfoDao() {
		return topicInfoDao;
	}

	public void setTopicInfoDao(TopicInfoDao topicInfoDao) {
		this.topicInfoDao = topicInfoDao;
	}

	public TopicStatusDao getTopicStatusDao() {
		return topicStatusDao;
	}

	public void setTopicStatusDao(TopicStatusDao topicStatusDao) {
		this.topicStatusDao = topicStatusDao;
	}

	public ReplyInfoDao getReplyInfoDao() {
		return replyInfoDao;
	}

	public void setReplyInfoDao(ReplyInfoDao replyInfoDao) {
		this.replyInfoDao = replyInfoDao;
	}
	
	@Override
	public void batchDeleteTopicInfoYouHui(String ids) throws Exception {
		topicStatusDao.batchDeleteTopicStatus(ids);
	}

}
