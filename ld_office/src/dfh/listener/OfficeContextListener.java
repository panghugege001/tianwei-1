package dfh.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import dfh.email.template.EmailTemplateHelp;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.AnnouncementType;
import dfh.model.enums.BankCreditChangeType;
import dfh.model.enums.BankInfoType;
import dfh.model.enums.Banktype;
import dfh.model.enums.BusinessProposalType;
import dfh.model.enums.CardType;
import dfh.model.enums.CommisionType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.DateIntervalType;
import dfh.model.enums.DaysNumber;
import dfh.model.enums.GamePlatform;
import dfh.model.enums.GameType;
import dfh.model.enums.IsCashinType;
import dfh.model.enums.IssuingBankEnum;
import dfh.model.enums.LevelType;
import dfh.model.enums.NetPayBusinessList;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.PayType;
import dfh.model.enums.PayTypem;
import dfh.model.enums.PhoneStatus;
import dfh.model.enums.PlayCodeType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.PtCouponUseType;
import dfh.model.enums.QuotalType;
import dfh.model.enums.TaskFlagType;
import dfh.model.enums.UserDepositStatus;
import dfh.model.enums.UserLimitType;
import dfh.model.enums.UserRegisterStatus;
import dfh.model.enums.UserRole;
import dfh.model.enums.UserStatus;
import dfh.model.enums.VipLevel;
import dfh.model.enums.WarnLevel;
import dfh.security.Authorities;
import dfh.security.Ipconfine;
import dfh.service.interfaces.EaOfficeFetchService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.IPSeeker;
import dfh.model.CallDaysNumber;

public class OfficeContextListener implements ServletContextListener {

	private Logger log;
	private static EaOfficeFetchService eaOfficeFetchService = null;

	public OfficeContextListener() {
		log = Logger.getLogger(OfficeContextListener.class);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("LoadConstantListener Destroyed");
	}

	public void contextInitialized(ServletContextEvent ctx) {
		log.debug("contextInitialized");
		
		// reader email template
		try {
			EmailTemplateHelp.readerEmailTemplate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		System.setProperty("org.apache.catalina.SESSION_COOKIE_NAME", "e68Office");
		//设置更新时间
		ctx.getServletContext().setAttribute("updateTime", DateUtil.formatDateForStandard(DateUtil.now()));

		log.info("load enums...");
		ctx.getServletContext().setAttribute("NetPayBusinessList", Arrays.asList(NetPayBusinessList.values()));
		ctx.getServletContext().setAttribute("UserRole", Arrays.asList(UserRole.values()));
		ctx.getServletContext().setAttribute("UserRoleExceptFree", getUsersExceptFree());	
		ctx.getServletContext().setAttribute("CreditChangeType", Arrays.asList(CreditChangeType.values()));	
		ctx.getServletContext().setAttribute("BankCreditChangeType", Arrays.asList(BankCreditChangeType.values()));
		ctx.getServletContext().setAttribute("IssuingBankEnum", Arrays.asList(IssuingBankEnum.values()));
		ctx.getServletContext().setAttribute("PayTypem", Arrays.asList(PayTypem.values()));
		ctx.getServletContext().setAttribute("LevelType", Arrays.asList(LevelType.values()));				
		ctx.getServletContext().setAttribute("OperationLogType", Arrays.asList(OperationLogType.values()));	
		ctx.getServletContext().setAttribute("PayType", Arrays.asList(PayType.values())); 
		ctx.getServletContext().setAttribute("QuotalType", Arrays.asList(QuotalType.values())); 
		ctx.getServletContext().setAttribute("PayOrderFlagType", Arrays.asList(PayOrderFlagType.values())); 
		ctx.getServletContext().setAttribute("ProposalFlagType", Arrays.asList(ProposalFlagType.values()));	
		ctx.getServletContext().setAttribute("ProposalType", Arrays.asList(ProposalType.values()));
		ctx.getServletContext().setAttribute("BusinessProposalType", Arrays.asList(BusinessProposalType.values()));	
		ctx.getServletContext().setAttribute("TaskFlagType", Arrays.asList(TaskFlagType.values()));			
		ctx.getServletContext().setAttribute("ActionLogType", Arrays.asList(ActionLogType.values()));		
		ctx.getServletContext().setAttribute("PageSizes", Arrays.asList(new Integer[] { 20, 50, 100, 250, 500, 1000,2000,3000,4000,5000,5,10 })); 
		ctx.getServletContext().setAttribute("PageSizeSms", Arrays.asList(new Integer[] {20, 50, 100, 200})); 
		ctx.getServletContext().setAttribute("AnnouncementType", Arrays.asList(AnnouncementType.values()));	
		ctx.getServletContext().setAttribute("VipLevel", Arrays.asList(VipLevel.values()));			
		ctx.getServletContext().setAttribute("WarnLevel", Arrays.asList(WarnLevel.values()));
		ctx.getServletContext().setAttribute("DateIntervalType", Arrays.asList(DateIntervalType.values()));	
		ctx.getServletContext().setAttribute("CommisionType", Arrays.asList(CommisionType.values()));		
		ctx.getServletContext().setAttribute("IsCashinType", Arrays.asList(IsCashinType.values()));	


		ctx.getServletContext().setAttribute("GameType", Arrays.asList(GameType.values()));
		ctx.getServletContext().setAttribute("PlayCodeType", Arrays.asList(PlayCodeType.values()));	
		ctx.getServletContext().setAttribute("Banktype", Arrays.asList(Banktype.values()));	
		
		ctx.getServletContext().setAttribute("PhoneStatus", Arrays.asList(PhoneStatus.values()));	
		ctx.getServletContext().setAttribute("UserDepositStatus", Arrays.asList(UserDepositStatus.values()));	
		ctx.getServletContext().setAttribute("UserRegisterStatus", Arrays.asList(UserRegisterStatus.values()));	
		ctx.getServletContext().setAttribute("UserStatus", Arrays.asList(UserStatus.values()));	
		ctx.getServletContext().setAttribute("UserLimitType", Arrays.asList(UserLimitType.values()));	
		ctx.getServletContext().setAttribute("BankInfoType", Arrays.asList(BankInfoType.values()));	
		ctx.getServletContext().setAttribute("PtCouponUseType", Arrays.asList(PtCouponUseType.values()));
		ctx.getServletContext().setAttribute("DaysNumber", Arrays.asList(DaysNumber.values()));	    
		ctx.getServletContext().setAttribute("CardType", Arrays.asList(CardType.values()));  
		
		ctx.getServletContext().setAttribute("GamePlatform", Arrays.asList(GamePlatform.values()));  
		ctx.getServletContext().setAttribute("CallDaysNumber", Arrays.asList(CallDaysNumber.values()));	
		log.info("load authorities...");
		try {
			String xml = IOUtils.toString(ctx.getServletContext().getResourceAsStream("/WEB-INF/authorities.xml"), Constants.DEFAULT_ENCODING);
//			log.info(xml);
			ctx.getServletContext().setAttribute("authoritiesXml", xml);
			ctx.getServletContext().setAttribute("authoritiesMap", Authorities.getAuthorites(xml));

			// insert sun ip限制
			String ipxml = IOUtils.toString(ctx.getServletContext().getResourceAsStream("/WEB-INF/ipconfine.xml"), Constants.DEFAULT_ENCODING);
			log.info(ipxml);
			ctx.getServletContext().setAttribute("ipconfineXml", ipxml);
			ctx.getServletContext().setAttribute("ipconfineMap", Ipconfine.getAuthorites(ipxml));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("load IPseeker...");
		IPSeeker ipSeeker = new IPSeeker(ctx.getServletContext().getRealPath("/commons/qqwry.dat"));
		ctx.getServletContext().setAttribute("ipSeeker", ipSeeker);

		/*
		log.info("time task to fetch the ea login logs");
		ApplicationContext springCtx = WebApplicationContextUtils.getWebApplicationContext(ctx.getServletContext());
		eaOfficeFetchService = (EaOfficeFetchService) springCtx.getBean("eaOfficeFetchService");
		TimerTask taskFetchLoginLogs = new TimerTask() {
			@Override
			public void run() {
				eaOfficeFetchService.fetchLoginLogs();
			}
		};
		Timer timer = new Timer();
		timer.schedule(taskFetchLoginLogs, 10 * 1000, 30 * 1000);
		*/
	}

	private List getUsersExceptFree() {
		List list = new ArrayList();
		list.add(UserRole.MONEY_CUSTOMER);
		list.add(UserRole.AGENT);
		// list.add(UserRole.PARTNER);
		return list;
	}

}
