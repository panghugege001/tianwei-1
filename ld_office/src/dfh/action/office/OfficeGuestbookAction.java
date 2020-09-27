package dfh.action.office;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import dfh.action.SubActionSupport;
import dfh.action.customer.MemberAction;
import dfh.model.PtProfit;
import dfh.model.PtStatistical;
import dfh.model.Users;
import dfh.service.interfaces.GuestBookService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.DateUtil;
import dfh.utils.IPSeeker;
import dfh.utils.Page;
import dfh.utils.NTUtils;
import dfh.utils.SortList;

/**
 * 
 * @author sun
 * 
 */
public class OfficeGuestbookAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(MemberAction.class);

	private GuestBookService guestBookService;
	private ProposalService proposalService;
	List<PtStatistical> list = new ArrayList<PtStatistical>();
	List<PtProfit> listPtProfit = new ArrayList<PtProfit>();

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
	private String startTi;
	private String endTi;

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
	private String isdeposit;//是否存款

	String agent;
	String ip;
	String client_address;
	String agent_website;
	String order;
	String by;
	private String ids;//要删除的站内信id窜
	private IPSeeker getIPSeeker(HttpServletRequest request) {
		return (IPSeeker) request.getSession().getServletContext().getAttribute("ipSeeker");

	}

	// 查询所有数据包含不显示的
	public String queryWordsForBack() {
		try {
			Page page = guestBookService.queryWordsForBack(adminname, username, title, flag, adminUserStatus, isadmin, start, end, pageIndex, size);
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
			Page page = guestBookService.queryIplist(username, agent, ip, client_address, agent_website, start, end, pageIndex, size);
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
			String msg = guestBookService.deleteWords(id);
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

	//批量删除站内信
	public void batchDelete() {
		try {
			guestBookService.batchDelete(ids);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
	}
	
	// 查询单条留言详细
	public String queryReference() {
		try {
			Page page = guestBookService.queryReference(id, flag, pageIndex, size);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	// 审核留言通过
	public String auditing() {
		try {
			String msg = guestBookService.auditing(id);
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
			String msg = guestBookService.leaveReplyWordsAdmin(referenceId, content, "客服中心", email, phone, qq, ipaddress, createdate);
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
	public String saveLeaveWords() {
		try {
			if(type==null){
				setErrormsg("类型不能为空！");
				return SUCCESS;
			}
			if(type==0){
				if(username==null || username.equals("")){
					setErrormsg("账号不能为空！");
					return SUCCESS;
				}
			}else if(type==2){//客服代码群发或会员等级群发
				if(null == start || null == end){
					setErrormsg("时间选项不能为空");
					return SUCCESS;
				}
				if(end.getTime()<=start.getTime()){
					setErrormsg("结束时间需要大于开始时间");
					return SUCCESS;
				}
				if((end.getTime()-start.getTime()>3*24*60*60*1000)){  //时间跨度为3天
					setErrormsg("结束时间和开始时间相差不能超过3天");
					return SUCCESS;
				}
			}
			if(title==null || title.equals("")){
				setErrormsg("标题不能为空！");
				return SUCCESS;
			}
			if(content==null || content.equals("")){
				setErrormsg("内容不能为空！");
				return SUCCESS;
			}
			ipaddress = getIPSeeker(getRequest()).getRandomIp();
			String msg = guestBookService.oficeLeaveWords(type,username,usernameType,title,flag,createdate,content,ipaddress , start , end , csType,isdeposit);
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

	public String getPtList() {
		if (start == null || end == null) {
			setErrormsg("时间不允许为空!");
			return INPUT;
		}
		Session session = guestBookService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			StringBuffer buffer = new StringBuffer();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			buffer.append("SELECT userId,loginname,createtime,SUM(amount) as amount,SUM(betCredit) as betCredit,SUM(payOut) as payOut FROM ptprofit WHERE createtime>='" + formatter.format(start) + "' AND createtime<'" + formatter.format(end) + "' GROUP BY loginname ORDER BY " + by + " DESC LIMIT " + pageIndex + "," + size + "");
			System.out.println(buffer.toString());
			Query query = session.createSQLQuery(buffer.toString());
			List list = query.list();
			for (Object obj : list) {
				Object[] objarray = (Object[]) obj;
				PtProfit profit = new PtProfit();
				profit.setUserId((Integer) objarray[0]);
				profit.setLoginname((String) objarray[1]);
//			profit.setCreatetime(new Timestamp(((Date) objarray[2]).getTime()));
				profit.setAmount((Double) objarray[3]);
				profit.setBetCredit((Double) objarray[4]);
				profit.setPayOut((Double) objarray[5]);
				listPtProfit.add(profit);
			}
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}

	public String getPtListInfo() {
		try {
			List<PtStatistical> listPtStatistical = new ArrayList<PtStatistical>();
			Users ptUser = (Users) proposalService.get(Users.class, username);
			if(start==null || end ==null){
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
			String loginString = NTUtils.getEffectiveBets(ptUser.getLoginname(), startcal.getTime(), endcal.getTime());
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			// 判断是否有投注额 如果大于了1个小时没有投注 自动下线
			if (!jsonObj.containsKey("stat")) {
				 return INPUT;
			}
			String nt_name=jsonObj.getString("user_id");
			for (Object object : jsonObj.getJSONArray("stat")) {
				List listObject = (List) object;
				PtStatistical ptSt = new PtStatistical();
				ptSt.setUserId(ptUser.getId());
				Calendar c = Calendar.getInstance();
				c.setTime(DateUtil.parseDateForStandard((String) listObject.get(0)));
				c.add(Calendar.HOUR_OF_DAY, 7);
				ptSt.setPlaytime(c.getTime());
				ptSt.setAmount(Double.parseDouble((String) listObject.get(1)) / 100);
				ptSt.setType(Integer.parseInt((String) listObject.get(7)));
				if (ptSt.getType() == 0) {
					ptSt.setMultiplier(Double.parseDouble((String) listObject.get(2)));
					ptSt.setLine(Double.parseDouble((String) listObject.get(3)));
					ptSt.setBet(Double.parseDouble((String) listObject.get(4)));
				}
				ptSt.setGameCode(Integer.parseInt((String) listObject.get(5)));
				ptSt.setPayOut(Double.parseDouble((String) listObject.get(6)) / 100);
				ptSt.setLoginname(username);
				ptSt.setNtName(nt_name);
				listPtStatistical.add(ptSt);
			}
			Collections.sort(listPtStatistical, new Comparator<PtStatistical>() {
				@Override
				public int compare(PtStatistical o1, PtStatistical o2) {
					Date d1 = o1.getPlaytime();
					Date d2 = o2.getPlaytime();
					return d2.compareTo(d1); 
				}
			});
			list=listPtStatistical;
			return INPUT;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return INPUT;
		}
	}

	public String getPtListInfoTow() {
		try {
			List<PtStatistical> listPtStatistical = new ArrayList<PtStatistical>();
			Users ptUser = (Users) proposalService.get(Users.class, username);
			// 判断是更新还是保存 12点到12点为一个时间段
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date startTime = null;
			Date endTime = null;

			startTime = formatter.parse(df.format(start) + " 12:00:00");
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			cal.add(java.util.Calendar.HOUR_OF_DAY, -6);
			startTime = cal.getTime();

			endTime = formatter.parse(df.format(end) + " 12:00:00");
			Calendar calend = Calendar.getInstance();
			calend.setTime(endTime);
			calend.add(java.util.Calendar.HOUR_OF_DAY, -6);
			endTime = calend.getTime();
			// 获取数据
			String loginString = NTUtils.getEffectiveBets(ptUser.getLoginname(), startTime, endTime);
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			// 判断是否有投注额 如果大于了1个小时没有投注 自动下线
			if (!jsonObj.containsKey("stat")) {
				return null;
			}
			for (Object object : jsonObj.getJSONArray("stat")) {
				List listObject = (List) object;
				PtStatistical ptSt = new PtStatistical();
				ptSt.setUserId(ptUser.getId());
				ptSt.setPlaytime(new Timestamp(new Date((String) listObject.get(0)).getTime()));
				ptSt.setAmount(Double.parseDouble((String) listObject.get(1)) / 100);
				ptSt.setType(Integer.parseInt((String) listObject.get(7)));
				if (ptSt.getType() == 0) {
					ptSt.setMultiplier(Double.parseDouble((String) listObject.get(2)));
					ptSt.setLine(Double.parseDouble((String) listObject.get(3)));
					ptSt.setBet(Double.parseDouble((String) listObject.get(4)));
				} else {
					ptSt.setMultiplier(0.0);
					ptSt.setLine(0.0);
					ptSt.setBet(0.0);
				}
				ptSt.setGameCode(Integer.parseInt((String) listObject.get(5)));
				ptSt.setPayOut(Double.parseDouble((String) listObject.get(6)) / 100);
				ptSt.setCreatetime(new Timestamp(new Date().getTime()));
				ptSt.setLoginname(ptUser.getLoginname());
				listPtStatistical.add(ptSt);
			}
			SortList<PtStatistical> listPtStatisticals = new SortList<PtStatistical>();
			listPtStatisticals.Sort(listPtStatistical, "getPlaytime", "desc");
			for (PtStatistical ptStatistical : listPtStatistical) {
				list.add(ptStatistical);
			}
			Calendar calTwo = Calendar.getInstance();
			calTwo.setTime(startTime);
			calTwo.add(java.util.Calendar.HOUR_OF_DAY, 6);
			start = calTwo.getTime();

			Calendar calendTwo = Calendar.getInstance();
			calendTwo.setTime(endTime);
			calendTwo.add(java.util.Calendar.HOUR_OF_DAY, 6);
			end = calendTwo.getTime();

			return INPUT;
		} catch (ParseException e) {
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

	public GuestBookService getGuestBookService() {
		return guestBookService;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
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

	public String getStartTi() {
		return startTi;
	}

	public void setStartTi(String startTi) {
		this.startTi = startTi;
	}

	public String getEndTi() {
		return endTi;
	}

	public void setEndTi(String endTi) {
		this.endTi = endTi;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public List<PtStatistical> getList() {
		return list;
	}

	public void setList(List<PtStatistical> list) {
		this.list = list;
	}

	public List<PtProfit> getListPtProfit() {
		return listPtProfit;
	}

	public void setListPtProfit(List<PtProfit> listPtProfit) {
		this.listPtProfit = listPtProfit;
	}

	public String getCsType() {
		return csType;
	}

	public void setCsType(String csType) {
		this.csType = csType;
	}

	public String getIsdeposit() {
		return isdeposit;
	}

	public void setIsdeposit(String isdeposit) {
		this.isdeposit = isdeposit;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
