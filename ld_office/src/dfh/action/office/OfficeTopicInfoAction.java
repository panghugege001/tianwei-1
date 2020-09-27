package dfh.action.office;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.nnti.office.model.auth.Operator;

import dfh.action.SubActionSupport;
import dfh.action.customer.MemberAction;
import dfh.model.PtStatistical;
import dfh.model.TopicInfo;
import dfh.model.Users;
import dfh.service.interfaces.GuestBookService;
import dfh.service.interfaces.ITopicInfoService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.IPSeeker;
import dfh.utils.NTUtils;
import dfh.utils.Page;

/**
 * 
 * @author sun
 * 
 */
public class OfficeTopicInfoAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(MemberAction.class);

	private ITopicInfoService topicInfoService;
	private ProposalService proposalService;

	private Integer id;
	private Integer referenceId;
	private Integer flag;
	private Integer rcount;
	private String ipaddress;
	private String username;
	private String email;
	private String phone;
	private String qq;
	private Date createdate;
	private String title;
	private String content;

	private Date start;
	private Date end;
	private Integer isRead;

	private String keywords;// 关键词

	private String errormsg;// 返回的消息
	private Date endDate;// 结束时间

	private Integer pageIndex;// 当前页
	private Integer size;// 每页显示的行数

	private Integer type;
	private Integer usernameType;
	private Long status;
	private Integer adminUserStatus;
	private Integer isadmin;
	private String adminname;
	private String csType;
	private String isdeposit;// 是否存款
	List<PtStatistical> list = new ArrayList<PtStatistical>();

	String agent;
	String ip;
	String client_address;
	String agent_website;

	private String ids;// 要删除的站内信id窜

	private IPSeeker getIPSeeker(HttpServletRequest request) {
		return (IPSeeker) request.getSession().getServletContext()
				.getAttribute("ipSeeker");

	}

	// 查询所有数据包含不显示的
	public String queryTopicInfoList() {
		try {
			Page page = topicInfoService.queryWordsForBack(adminname, username,
					title, flag, isRead, isadmin, start, end,
					pageIndex, size);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());

		}
		return INPUT;
	}

	// 查询所有数据包含不显示的
	public String queryTopicStatusList() {
		try {
			Page page = topicInfoService.queryYouHuiTopic(adminname, username, title, flag, isRead, isadmin, start, end,
					pageIndex, size);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());

		}
		return INPUT;
	}

	// 查询所有数据包含不显示的
	public String queryTopicUserList() {
		try {
			Page page = topicInfoService.queryTopicUserList(id,adminname, isRead, 
					pageIndex, size);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());

		}
		return INPUT;
	}
		
	public String queryIplist() {
		try {
			if (pageIndex == null) {
				pageIndex = 1;
			}
			if (size == null) {
				size = 20;
			}
			System.out.println(agent_website);
			Page page = topicInfoService.queryIplist(username, agent, ip,
					client_address, agent_website, start, end, pageIndex, size);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());

		}
		return INPUT;
	}

	// 删除单条记录
	public String deleteWords() {
		try {
			String msg = topicInfoService.deleteWords(id);
			if (msg == null)
				setErrormsg("返回消息:删除成功");
			else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}
	
	// 删除单条记录
	public String deleteYouHuiTopicInfo() {
		try {
			String msg = topicInfoService.deleteYouHuiTopicInfo(id);
			if (msg == null)
				setErrormsg("返回消息:删除成功");
			else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}
		

	// 批量删除站内信
	public void batchDelete() {
		try {
			topicInfoService.batchDelete(ids);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
	}

	// 批量删除站内信
	public void batchDeleteTopicInfoYouHui() {
		
		try {
			Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
			if(operator == null){//弱控制
				setErrormsg("请先登录");
				return;
			}
			String operateName = operator.getUsername();
			log.info(operateName + "删除优惠站内信：" + this.ids);
			
			topicInfoService.batchDeleteTopicInfoYouHui(this.ids);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
	}
	
	// 批量推送站内信
	public void batchPushToApp() {

		try {

			String result = topicInfoService.batchPushToApp(ids);

			GsonUtil.GsonObject(result);

		} catch (Exception e) {

			e.printStackTrace();
			// setErrormsg("返回消息:" + e.getMessage());
			GsonUtil.GsonObject("返回消息:" + e.getMessage());

		}
	}

	// 查询单条留言详细
	public String queryReference() {
		try {
			Page page = topicInfoService.queryReference(id, flag, pageIndex,
					size);
			
			TopicInfo topicInfo = topicInfoService.queryTopicInfo(id);
			getRequest().setAttribute("page", page);
			getRequest().setAttribute("topicInfo", topicInfo);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	// 审核留言通过
	public String auditing() {
		try {
			String msg = topicInfoService.auditing(id);
			if (msg == null)
				setErrormsg("成功审核通过");
			else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}

		return INPUT;
	}

	// 留言回复
	public String leaveReplyWords() {
		try {
			if (StringUtils.isEmpty(ipaddress))
				ipaddress = getIPSeeker(getRequest()).getRandomIp();
			String msg = topicInfoService.leaveReplyWordsAdmin(referenceId,
					content, "客服中心", email, phone, qq, ipaddress, createdate);
			if (msg == null)
				setErrormsg("成功回复信息");
			else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	// 管理员发贴
	public String saveTopicInfo() {
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		String operateName = operator.getUsername();
		try {
			if (type == null) {
				setErrormsg("类型不能为空！");
				return SUCCESS;
			}
			if (type == 0) {
				if (username == null || username.equals("")) {
					setErrormsg("账号不能为空！");
					return SUCCESS;
				}
			} else if (type == 2) {// 客服代码群发或会员等级群发
				if (null == start || null == end) {
					setErrormsg("时间选项不能为空");
					return SUCCESS;
				}
				if (end.getTime() <= start.getTime()) {
					setErrormsg("结束时间需要大于开始时间");
					return SUCCESS;
				}
				if ((end.getTime() - start.getTime() > 3 * 24 * 60 * 60 * 1000)) { // 时间跨度为3天
					setErrormsg("结束时间和开始时间相差不能超过3天");
					return SUCCESS;
				}
			}
			if (title == null || title.equals("")) {
				setErrormsg("标题不能为空！");
				return SUCCESS;
			}
			if (content == null || content.equals("")) {
				setErrormsg("内容不能为空！");
				return SUCCESS;
			}
			ipaddress = getIPSeeker(getRequest()).getRandomIp();
			String msg = topicInfoService.saveLeaveWords(type, username,
					usernameType, title, flag, createdate, content, ipaddress,
					start, end, csType, isdeposit,operateName);
			if (msg == null) {
				setErrormsg("返回消息:发贴成功");
				return SUCCESS;
			} else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return SUCCESS;
	}

	public String getPtListInfo() {
		try {
			List<PtStatistical> listPtStatistical = new ArrayList<PtStatistical>();
			Users ptUser = (Users) proposalService.get(Users.class, username);
			if (start == null || end == null) {
				this.setErrormsg("时间不允许为空");
				return INPUT;
			}
			Calendar startcal = Calendar.getInstance();
			startcal.setTime(start);
			startcal.add(Calendar.HOUR_OF_DAY, -8);

			Calendar endcal = Calendar.getInstance();
			endcal.setTime(end);
			endcal.add(Calendar.HOUR_OF_DAY, -8);

			// 获取数据
			String loginString = NTUtils
					.getEffectiveBets(ptUser.getLoginname(),
							startcal.getTime(), endcal.getTime());
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			// 判断是否有投注额 如果大于了1个小时没有投注 自动下线
			if (!jsonObj.containsKey("stat")) {
				return INPUT;
			}
			String nt_name = jsonObj.getString("user_id");
			for (Object object : jsonObj.getJSONArray("stat")) {
				List listObject = (List) object;
				PtStatistical ptSt = new PtStatistical();
				ptSt.setUserId(ptUser.getId());
				Calendar c = Calendar.getInstance();
				c.setTime(DateUtil.parseDateForStandard((String) listObject
						.get(0)));
				c.add(Calendar.HOUR_OF_DAY, 7);
				ptSt.setPlaytime(c.getTime());
				ptSt.setAmount(Double.parseDouble((String) listObject.get(1)) / 100);
				ptSt.setType(Integer.parseInt((String) listObject.get(7)));
				if (ptSt.getType() == 0) {
					ptSt.setMultiplier(Double.parseDouble((String) listObject
							.get(2)));
					ptSt.setLine(Double.parseDouble((String) listObject.get(3)));
					ptSt.setBet(Double.parseDouble((String) listObject.get(4)));
				}
				ptSt.setGameCode(Integer.parseInt((String) listObject.get(5)));
				ptSt.setPayOut(Double.parseDouble((String) listObject.get(6)) / 100);
				ptSt.setLoginname(username);
				ptSt.setNtName(nt_name);
				listPtStatistical.add(ptSt);
			}
			Collections.sort(listPtStatistical,
					new Comparator<PtStatistical>() {
						@Override
						public int compare(PtStatistical o1, PtStatistical o2) {
							Date d1 = o1.getPlaytime();
							Date d2 = o2.getPlaytime();
							return d2.compareTo(d1);
						}
					});
			list = listPtStatistical;
			return INPUT;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return INPUT;
		}
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public ITopicInfoService getTopicInfoService() {
		return topicInfoService;
	}

	public void setTopicInfoService(ITopicInfoService topicInfoService) {
		this.topicInfoService = topicInfoService;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getRcount() {
		return rcount;
	}

	public void setRcount(Integer rcount) {
		this.rcount = rcount;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getUsernameType() {
		return usernameType;
	}

	public void setUsernameType(Integer usernameType) {
		this.usernameType = usernameType;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Integer getAdminUserStatus() {
		return adminUserStatus;
	}

	public void setAdminUserStatus(Integer adminUserStatus) {
		this.adminUserStatus = adminUserStatus;
	}

	public Integer getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(Integer isadmin) {
		this.isadmin = isadmin;
	}

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getClient_address() {
		return client_address;
	}

	public void setClient_address(String client_address) {
		this.client_address = client_address;
	}

	public String getAgent_website() {
		return agent_website;
	}

	public void setAgent_website(String agent_website) {
		this.agent_website = agent_website;
	}

	public List<PtStatistical> getList() {
		return list;
	}

	public void setList(List<PtStatistical> list) {
		this.list = list;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public String getCsType() {
		return csType;
	}

	public void setCsType(String csType) {
		this.csType = csType;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getIsdeposit() {
		return isdeposit;
	}

	public void setIsdeposit(String isdeposit) {
		this.isdeposit = isdeposit;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	
}
