package dfh.icbc.quart.fetch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import dfh.model.CreditWarnlogs;
import dfh.model.Creditlogs;
import dfh.model.Userstatus;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SlaveService;
import dfh.utils.DateUtil;
public class FetchCreditService {
	private static Logger log = Logger.getLogger(FetchCreditService.class);
	
	private ProposalService proposalService ;
	private SlaveService slaveService ;
	
	public void analyzeUserCreditLog() {
		log.info("额度审核处理...");
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date());
		c2.add(Calendar.MINUTE, -5);
		String sql = "select * from creditlogs where createtime>'"+DateUtil.formatDateForStandard(c2.getTime())+"' and type!='CASHOUT_RETURN'  and type!='CASHIN' and type!='XIMA_CONS' and type!='CHANGE_COMMISSIONSDAY' and type!='NETPAY' group by loginname,createtime,credit having count(*) >= 2; ";
		
		Session session = slaveService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			List<Creditlogs> historyLogs = (List<Creditlogs>) session.createSQLQuery(sql).addEntity(Creditlogs.class).list() ;
			if(null != historyLogs && historyLogs.size()>0){
				for (Creditlogs creditlogs : historyLogs) {
					try {
						CreditWarnlogs warnlog = new CreditWarnlogs(creditlogs.getLoginname(), creditlogs.getCreatetime(), creditlogs.getType(), creditlogs.getCredit(), creditlogs.getNewCredit(), creditlogs.getRemit(), "额度出现问题，已限制秒提。");
						warnlog.setId(creditlogs.getId());
						proposalService.save(warnlog);
						//禁用秒提
						Userstatus userstatus =(Userstatus) proposalService.get(Userstatus.class, creditlogs.getLoginname());
						userstatus.setTouzhuflag(1);
						userstatus.setRemark(userstatus.getRemark()+";额度出现问题，请核对。");
						proposalService.update(userstatus);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	
	public SlaveService getSlaveService() {
	
		return slaveService;
	}

	
	public void setSlaveService(SlaveService slaveService) {
	
		this.slaveService = slaveService;
	}

}
