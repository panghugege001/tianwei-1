package dfh.service.implementations;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Xima;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.utils.DateUtil;

public class SystemBatchAutoExecPrize extends AbstractGameinfoServiceImpl {

	private Logger log=Logger.getLogger(SystemBatchAutoExecPrize.class);

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
	//	System.out.println("开始执行");
		sf=new StringBuffer();
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(7*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		this.setMsg("【执行提案】开始执行任务...",true);
		List<Proposal> notExecProposal = this.getProposalService().getNotExecPrizeProposal(starttime, new Date()); 
		
		if (notExecProposal==null||notExecProposal.size()<=0) {
			this.setMsg("提案记录为空，不需要执行该任务",true);
		}else{
			for (Proposal proposal : notExecProposal) {
				Prize prize = (Prize) this.getGameinfoDao().get(Prize.class, proposal.getPno());
				if (prize==null) {
					this.setErrorMsg("提案号："+proposal.getPno()+"存在问题，请核实.",true);
					continue;
				}
				this.setMsg("正在处理提案号："+proposal.getPno()+"...");
				this.getTradeDao().changeCredit(prize.getLoginname(), prize.getTryCredit(), CreditChangeType.PRIZE.getCode(), proposal.getPno(), "ok");
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setRemark(proposal.getRemark());
				this.getProposalService().save(proposal);
				this.getTaskDao().excuteTask(proposal.getPno(), "system", "127.0.0.1");
				this.getLogDao().insertOperationLog("system", OperationLogType.EXCUTE, "ip:127.0.0.1;pno:" + proposal.getPno());
				this.setMsg("成功", true);
			}
		}
		this.setMsg("【执行提案】任务完成",true);
	}

}
