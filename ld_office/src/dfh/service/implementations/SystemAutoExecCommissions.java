package dfh.service.implementations;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.Commissions;
import dfh.model.Proposal;
import dfh.model.Xima;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.utils.DateUtil;

public class SystemAutoExecCommissions extends AbstractGameinfoServiceImpl {

	private Logger log=Logger.getLogger(SystemAutoExecCommissions.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			excuteAutoXimaProposal();
			this.destory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.setErrorMsg(e.getMessage(),true);
		}
	}
	
	@Override
	public void excuteAutoXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		sf=new StringBuffer();
		Calendar calendar=Calendar.getInstance();
		DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class);
		dc.add(Restrictions.eq("id.year", calendar.get(Calendar.YEAR)));
		dc.add(Restrictions.eq("id.month", calendar.get(Calendar.MONTH)));
		dc.add(Restrictions.eq("flag", 1));
		//System.out.println(dc.toString());
		List<Commissions> commissions=this.getTaskDao().findByCriteria(dc);
		if(commissions==null||commissions.size()<=0){
			this.setMsg("佣金记录为空，不需要执行该任务",true);
		}else{
			for (Commissions commissions2 : commissions) {
				Integer subCount= commissions2.getSubCount();
				Integer activeuser= commissions2.getActiveuser();//活跃会员数超过5个或者佣金为负数才执行
				if(activeuser>=5||commissions2.getAmount()<0){
					this.getTradeDao().changeCredit(commissions2.getId().getLoginname(),commissions2.getAmount(), CreditChangeType.COMMISSION.getCode(),
							commissions2.getId().getLoginname()+commissions2.getId().getYear()+commissions2.getId().getMonth(), "ok");
					commissions2.setFlag(0);
					this.getProposalService().save(commissions2);
					this.setMsg("成功", true);
				}
				
			}
		}
//		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(7*24-12));
//		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
//		this.setMsg("【执行提案】开始执行任务...",true);
		//List<Proposal> notExecProposal = this.getProposalService().getNotExecProposal(starttime, new Date()); 
		
//		if (notExecProposal==null||notExecProposal.size()<=0) {
//			this.setMsg("提案记录为空，不需要执行该任务",true);
//		}else{
//			for (Proposal proposal : notExecProposal) {
//				Xima xima = (Xima) this.getGameinfoDao().get(Xima.class, proposal.getPno());
//				if (xima==null) {
//					this.setErrorMsg("提案号："+proposal.getPno()+"存在问题，请核实.",true);
//					continue;
//				}
//				this.setMsg("正在处理提案号："+proposal.getPno()+"...");
//				this.getTradeDao().changeCredit(xima.getLoginname(), xima.getTryCredit(), CreditChangeType.XIMA_CONS.getCode(), proposal.getPno(), "ok");
//				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
//				proposal.setRemark(proposal.getRemark() + ";执行:");
//				this.getProposalService().save(proposal);
//				this.getTaskDao().excuteTask(proposal.getPno(), "system", "127.0.0.1");
//				this.getLogDao().insertOperationLog("system", OperationLogType.EXCUTE, "ip:127.0.0.1;pno:" + proposal.getPno());
//				this.setMsg("成功", true);
//			}
//		}
		this.setMsg("【执行提案】任务完成",true);
	}

}
