package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;

import dfh.action.vo.XimaVO;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class SystemAutoAddProposal extends AbstractGameinfoServiceImpl {
	
	private Logger log=Logger.getLogger(SystemAutoAddProposal.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			autoAddXimaProposal();
			this.destory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.setErrorMsg(e.getMessage(), true);
		}
	}
	
	@Override
	public void autoAddXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		sf=new StringBuffer();
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
//		System.out.println(starttime);
//		System.out.println(endtime);
		
		
		this.setMsg("【提交提案】开始执行任务...",true);
		// 获取一天之内未执行的洗码提案；主要是为了避免程序在执行中途出现问题终止后，在启动反水功能时重复提交洗码数据的情况
		List<Proposal> notExecProposal = this.getProposalService().getNotExecProposal(starttime, new Date()); 
		
		// 得到一天内的所有会员的有效投注记录
		List<XimaVO> ximaObjectList = this.getXimaObject(starttime,endtime);
		if (ximaObjectList==null||ximaObjectList.size()<=0) {
			this.setMsg("洗码记录为空，任务终止",true);
			return;
		}
		
		List<String> proposalUsers=new ArrayList<String>();
		if (notExecProposal!=null&&notExecProposal.size()>0) {
			for (Proposal proposal : notExecProposal) {
				proposalUsers.add(proposal.getLoginname());
			}
		}
		int proposalSize=proposalUsers.size();
		this.setMsg("一天的投注记录数为："+ximaObjectList.size(),true);
		
		for (XimaVO ximaObject : ximaObjectList) {
			
			if (proposalSize>0&&proposalUsers.contains(ximaObject.getLoginname())) {
				proposalUsers.remove(ximaObject.getLoginname()); // 减小集合长度
				continue;
			}
			
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			this.setMsg("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Users user = (Users) this.getUserDao().get(Users.class, ximaObject.getLoginname(),LockMode.UPGRADE);
			if (user==null) {
				this.setErrorMsg("用户："+ximaObject.getLoginname()+"，不存在", true);
				continue;
			}
			String remark = "系统洗码";
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.getGameinfoDao().save(xima);
			this.getProposalService().save(proposal);
			this.setMsg("成功", true);
		}
		this.setMsg("【提交提案】任务完成",true);
	}


}
