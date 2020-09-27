package dfh.action.office;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.customer.MemberAction;
import dfh.service.interfaces.GuestBookService;
import dfh.utils.IPSeeker;
import dfh.utils.Page;

/**
 * 
 * @author sun
 * 
 */
public class OfficeGuestbookAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(MemberAction.class);

	private GuestBookService guestBookService;

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

	private String keywords;// 关键词

	private String errormsg;// 返回的消息
	private Date endDate;// 结束时间

	private Integer pageIndex;// 当前页
	private Integer size;// 每页显示的行数
	
	private IPSeeker getIPSeeker(HttpServletRequest request) {
		return (IPSeeker) request.getSession().getServletContext().getAttribute("ipSeeker");

	}

//	//查询所有数据包含不显示的
//	public String queryWordsForBack() {
//		try {
//			Page page = guestBookService.queryWordsForBack(keywords, start, end, pageIndex, size);
//			getRequest().setAttribute("page", page);
//		} catch (Exception e) {
//			e.printStackTrace();
//			setErrormsg("返回消息:" + e.getMessage());
//
//		}
//		return INPUT;
//	}
//
//	// 删除单条记录
//	public String deleteWords() {
//		try {
//			String msg = guestBookService.deleteWords(id);
//			if (msg == null)
//				setErrormsg("返回消息:删除成功");
//			else
//				setErrormsg("返回消息:" + msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//			setErrormsg("返回消息:" + e.getMessage());
//		}
//		return INPUT;
//	}
//
//	// 查询单条留言详细
//	public String queryReference() {
//		try {
//			Page page = guestBookService.queryReference(id, flag, pageIndex, size);
//			getRequest().setAttribute("page", page);
//		} catch (Exception e) {
//			e.printStackTrace();
//			setErrormsg("返回消息:" + e.getMessage());
//		}
//		return INPUT;
//	}
//
//	// 审核留言通过
//	public String auditing() {
//		try {
//			String msg = guestBookService.auditing(id);
//			if (msg == null)
//				setErrormsg("成功审核通过");
//			else
//				setErrormsg("返回消息:" + msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//			setErrormsg(e.getMessage());
//		}
//
//		return INPUT;
//	}
//
//	// 留言回复
//	public String leaveReplyWords() {
//		try {
//			if (StringUtils.isEmpty(ipaddress))
//				ipaddress =getIPSeeker(getRequest()).getRandomIp();
//			String msg = guestBookService.leaveReplyWordsAdmin(referenceId, content, "客服中心", email, phone, qq, ipaddress,createdate);
//			if (msg == null)
//				setErrormsg("成功回复信息");
//			else
//				setErrormsg("返回消息:" + msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//			setErrormsg(e.getMessage());
//		}
//		return INPUT;
//	}
//	
//	
//
//	//管理员发贴
//	public String saveLeaveWords() {
//		if (StringUtils.isEmpty(ipaddress))
//			ipaddress = getIPSeeker(getRequest()).getRandomIp();
//		try {
//			String msg = guestBookService.oficeLeaveWords(title, content, username, email, phone, qq, ipaddress,createdate);
//			if (msg == null) {
//				setErrormsg("返回消息:发贴成功");
//				return SUCCESS;
//			} else
//				setErrormsg("返回消息:" + msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//			setErrormsg(e.getMessage());
//		}
//		return INPUT;
//	}

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

}
