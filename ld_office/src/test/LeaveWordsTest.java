package test;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SynRecordsService;
import dfh.service.interfaces.TransferService;
import junit.framework.TestCase;
import dfh.action.customer.*;


public class LeaveWordsTest extends TestCase{

	private static FileSystemXmlApplicationContext ctx;
	static ProposalService proposalService;
	static NetpayService netpayService;
	static SeqService seqService;
	static TransferService transferService;
	static SynRecordsService synRecordsService;
	static NotifyService notifyService;
	static CustomerService customerService;
	static TransferDao transferDao;
	static UserDao userDao;
	static TradeDao tradeDao;
	static OperatorService operatorService;
	private String loginname;
	private String billno;
	private Double amount;
	private String aliasName;
	private String email;
	private String phone;
	private String newAccount;
	private String password;
	private String confirm_password;
	private String netpayName;
	private String partner;
	private Integer isSucc;
	private String errormsg;
	
	public  LeaveWordsTest()
	{
		
	}
	
	public static FileSystemXmlApplicationContext getContext() {
		return ctx;
	}

	public static SynRecordsService getSynRecordsService() {
		return synRecordsService;
	}
	
	public static void initSpring() {

		ctx = new FileSystemXmlApplicationContext("/WebRoot/WEB-INF/applicationContext.xml");
		proposalService = (ProposalService) getContext().getBean("proposalService");
		netpayService = (NetpayService) getContext().getBean("netpayService");
		seqService = (SeqService) getContext().getBean("seqService");
		transferService = (TransferService) getContext().getBean("transferService");
		synRecordsService = (SynRecordsService) getContext().getBean("synRecordsService");
		notifyService = (NotifyService) getContext().getBean("notifyService");
		customerService = (CustomerService) getContext().getBean("customerService");
		transferDao = (TransferDao) getContext().getBean("transferDao");
		userDao = (UserDao) getContext().getBean("userDao");
		operatorService = (OperatorService) getContext().getBean("operatorService");
		tradeDao = (TradeDao) getContext().getBean("tradeDao");
	}
	
	public String getIp() {
		String forwaredFor = getRequest().getHeader("X-Forwarded-For");
		String remoteAddr = getRequest().getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)){
			String[] ipArray=forwaredFor.split(",");
			return ipArray[0];
		}else
			return remoteAddr;
	}
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	public void testExecute()
	{
		initSpring();
		//ServletActionContext.getRequest().setAttribute(Constants.SESSION_CUSTOMERID, user);
		GuestbookAction action = new GuestbookAction();
		action.setTitle("title");
		action.setContent("hahahahahahaha");
		action.setUsername("stest999");
		action.setEmail("email");
		action.setQq("AA");
		action.setPhone("121212");

			assertEquals(3, 2+1);
			assertEquals("success" , action.leaveWords());		
		

	}
}
