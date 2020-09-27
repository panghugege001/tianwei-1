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


public class ModifyBankInfoTest extends TestCase{

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
	
	public ModifyBankInfoTest()
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
		String loginname = "stest999";
		String aliasname = "kevin";
		String phone = "69696";
		String email = "kevin";
		String bank = "bpi";
		String bankAddress="999here";
		String accountName = "noname";
		String accountType = "rich";
		String accountCity = "qc";
		String accountNo = "11111";
		try
		{
		assertEquals(3, 2+1);
		assertEquals(null , customerService.modifyBankInfo(loginname, StringUtils.trim(bank), StringUtils.trim(bankAddress), StringUtils.trim(accountType), StringUtils.trim(accountName),
				StringUtils.trim(accountNo), StringUtils.trim(accountCity), getIp()));
		}
		catch(Exception ex)
		{
			System.out.println("JEE");
		}
	}
}
