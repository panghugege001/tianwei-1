package dfh.service.implementations;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;

import dfh.dao.BankinfoDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.model.Bankinfo;
import dfh.model.Intransfer;
import dfh.model.Proposal;
import dfh.model.enums.ProposalFlagType;
import dfh.service.interfaces.IIntransferService;
import dfh.utils.DateUtil;

public class IntransferServiceImpl extends UniversalServiceImpl implements IIntransferService {
	
	private static Logger log = Logger.getLogger(IntransferServiceImpl.class);
	private BankinfoDao bankinfoDao;
	private TaskDao taskDao;
	private SeqDao seqDao;

	@Override
	public String addIntransfer(String from, String to, String operator,
			Double amount,Double fee, String remark,Integer transferflag) throws Exception {

		log.info("add intransfer proposal");
		try {
			String pno = seqDao.generateTRANSFER(516);
			
			Bankinfo frombankinfo = (Bankinfo) get(Bankinfo.class, Integer.parseInt(from), LockMode.UPGRADE);
			Bankinfo tobankinfo = (Bankinfo) get(Bankinfo.class, Integer.parseInt(to), LockMode.UPGRADE);
			
			Intransfer intransfer = new Intransfer();
			intransfer.setPno(pno);
			intransfer.setAmount(amount);
			intransfer.setWherefrom(frombankinfo.getUsername());
			intransfer.setWhereto(tobankinfo.getUsername());
			intransfer.setOperator(operator);
			intransfer.setRemark(remark);
			intransfer.setFee(fee);
			intransfer.setTransferflag(transferflag);
			intransfer.setCreateTime(DateUtil.now());
			intransfer.setFromto(from+"※"+to);
			
			String[] fromto = intransfer.getFromto().split("※");
			String fromByTo = intransfer.getWherefrom()+"-->"+intransfer.getWhereto();
			//bankinfoDao.changeBankInAmount(fromto[0], 1, 0, -(intransfer.getAmount()+intransfer.getFee()),fromByTo);
			//bankinfoDao.changeBankInAmount(fromto[1], 0, 0, intransfer.getAmount(),fromByTo);
			if(frombankinfo.getBanktype()==1 || tobankinfo.getBanktype()==1){
				Proposal proposal = new Proposal(pno, operator, DateUtil.now(), 516, 0, frombankinfo.getUsername()+"->"+tobankinfo.getUsername(),
						amount, "", ProposalFlagType.SUBMITED.getCode(), "后台", null, null,transferflag);
				save(proposal);
			}else{
				Proposal proposal = new Proposal(pno, operator, DateUtil.now(), 516, 0, frombankinfo.getUsername()+"->"+tobankinfo.getUsername(),
						amount, "", ProposalFlagType.AUDITED.getCode(), "后台", null, null,transferflag);
				save(proposal);
			}
			save(intransfer);
			taskDao.generateTasks(pno, operator);
		} catch (Exception e) {
			e.printStackTrace();
			return "提交失败"+e.toString();
		}
		
		return null;
	}

	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}

	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	


	

}
