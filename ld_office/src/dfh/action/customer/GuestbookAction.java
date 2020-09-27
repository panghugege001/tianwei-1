package dfh.action.customer;

import java.util.Date;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.service.interfaces.GuestBookService;
import dfh.utils.Page;

/***
 * 
 * @author sun
 * 
 * 
 */
public class GuestbookAction extends SubActionSupport {
	
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

	private String errormsg;// 返回的消息

	private Integer pageIndex;//当前页
	private Integer size;// 每页显示的行数

	// 提交留言
	public String leaveWords() {
		try {
			queryGuestbook();
			System.out.println(title+" "+ content+" "+ username+" "+ email+" "+ phone+" "+ qq);
			String msg = guestBookService.leaveWords(title, content, username, email, phone, qq, getIp());
			if (msg == null)
				setErrormsg("恭喜您 您的留言信息已成功提交");
			else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return SUCCESS;
	}

	//查询所有可显示留言
	public String queryGuestbook() {
		try {
			Page page = guestBookService.queryWordsForFront(pageIndex,new Integer(10));
			this.getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	// 得到单条留言的详细
	public String queryReference() {
		try {
			if (id != null) {
				Page page = guestBookService.queryReference(id,new Integer(0), pageIndex, size);
				getRequest().setAttribute("page", page);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	//留言回复
	public String leaveReplyWords() {
		try {
			String msg = null;
			msg = guestBookService.leaveReplyWords(referenceId, content, username, email, phone, qq,getIp());
			if (msg == null)
				setErrormsg("恭喜您 您的留言回复信息已成功提交");
			else
				setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return SUCCESS;
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

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
