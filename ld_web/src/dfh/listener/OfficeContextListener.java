package dfh.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import dfh.email.template.EmailTemplateHelp;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.AnnouncementType;
import dfh.model.enums.CardType;
import dfh.model.enums.CommisionType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.DateIntervalType;
import dfh.model.enums.GameType;
import dfh.model.enums.IssuingBankEnum;
import dfh.model.enums.LevelType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.PlayCodeType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.QuestionEnum;
import dfh.model.enums.TaskFlagType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.security.Ipconfine;
import dfh.utils.Constants;
import dfh.utils.IPSeeker;
import dfh.utils.SeoKeyUtil;
import dfh.utils.WebServiceConfig;

public class OfficeContextListener implements ServletContextListener {

	private Logger log;

	public OfficeContextListener() {
		log = Logger.getLogger(OfficeContextListener.class);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("LoadConstantListener Destroyed");
	}

	public void contextInitialized(ServletContextEvent ctx) {
		log.debug("contextInitialized");
		
		// reader seokeys to map
		try {
			Constants.titles=SeoKeyUtil.getTitles();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		// reader email template
		try {
			EmailTemplateHelp.readerEmailTemplate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		//设置更新时间
		//ctx.getServletContext().setAttribute("updateTime", DateUtil.formatDateForStandard(DateUtil.now()));
		WebServiceConfig.axis2XmlPath = ctx.getServletContext().getRealPath("/");
		//部署时开启
		/**
		WebServiceConfig.webServiceUrl=WebProperties.getValue("webservice.publicWSurl");
		if(WebServiceConfig.webServiceUrl!=null){
			 try {
				 String filepath = OfficeContextListener.class.getResource("webservice.properties").toURI().getPath().substring(1);
				 if(null!=filepath){ 
					new File(filepath).delete();
				 }else{
					 log.info("获取删除文件路径失败！");
					 return ;
				 }		
			 } catch (Exception e) {
				e.printStackTrace();
				log.info("获取删除文件失败");
				return ;
			}
		}else{
			log.info("数据写入内存失败！");
			return ;
		}**/
		
		log.info("load enums...");
		ctx.getServletContext().setAttribute("UserRole", Arrays.asList(UserRole.values()));
		ctx.getServletContext().setAttribute("UserRoleExceptFree", getUsersExceptFree());
		ctx.getServletContext().setAttribute("CreditChangeType", Arrays.asList(CreditChangeType.values()));
		ctx.getServletContext().setAttribute("IssuingBankEnum", Arrays.asList(IssuingBankEnum.values()));
		ctx.getServletContext().setAttribute("LevelType", Arrays.asList(LevelType.values()));
		ctx.getServletContext().setAttribute("OperationLogType", Arrays.asList(OperationLogType.values()));
		ctx.getServletContext().setAttribute("PayOrderFlagType", Arrays.asList(PayOrderFlagType.values()));
		ctx.getServletContext().setAttribute("ProposalFlagType", Arrays.asList(ProposalFlagType.values()));
		ctx.getServletContext().setAttribute("ProposalType", Arrays.asList(ProposalType.values()));
		ctx.getServletContext().setAttribute("TaskFlagType", Arrays.asList(TaskFlagType.values()));
		ctx.getServletContext().setAttribute("ActionLogType", Arrays.asList(ActionLogType.values()));
		ctx.getServletContext().setAttribute("PageSizes", Arrays.asList(new Integer[] { 20, 50, 100, 250, 500, 1000 }));
		ctx.getServletContext().setAttribute("AnnouncementType", Arrays.asList(AnnouncementType.values()));
		ctx.getServletContext().setAttribute("VipLevel", Arrays.asList(VipLevel.values()));
		ctx.getServletContext().setAttribute("DateIntervalType", Arrays.asList(DateIntervalType.values()));
		ctx.getServletContext().setAttribute("CommisionType", Arrays.asList(CommisionType.values()));

		ctx.getServletContext().setAttribute("GameType", Arrays.asList(GameType.values()));
		ctx.getServletContext().setAttribute("PlayCodeType", Arrays.asList(PlayCodeType.values()));
		ctx.getServletContext().setAttribute("QuestionEnum", Arrays.asList(QuestionEnum.values()));
		ctx.getServletContext().setAttribute("CardType", Arrays.asList(CardType.values()));
		try {
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
		IPSeeker ipSeeker = new IPSeeker(ctx.getServletContext().getRealPath("/commons/QQWry.Dat"));
		ctx.getServletContext().setAttribute("ipSeeker", ipSeeker);
	}

	private List getUsersExceptFree() {
		List list = new ArrayList();
		list.add(UserRole.MONEY_CUSTOMER);
		list.add(UserRole.AGENT);
		// list.add(UserRole.PARTNER);
		return list;
	}
}
