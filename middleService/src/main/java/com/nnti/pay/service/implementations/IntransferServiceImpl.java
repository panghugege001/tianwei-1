package com.nnti.pay.service.implementations;

import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.utils.DateUtil;
import com.nnti.pay.controller.vo.IntransferVo;
import com.nnti.pay.dao.master.IMasterIntransferDao;
import com.nnti.pay.model.enums.LevelType;
import com.nnti.pay.model.enums.ProposalFlagType;
import com.nnti.pay.model.enums.TaskFlagType;
import com.nnti.pay.model.vo.Intransfer;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.Task;
import com.nnti.pay.service.interfaces.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class IntransferServiceImpl implements IIntransferService {

	private Logger log = Logger.getLogger(IntransferServiceImpl.class);

	@Autowired
	private IMasterIntransferDao masterIntransferDao;
	@Autowired
	private IBasicService basicService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private IBankInfoService bankInfoService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private IProposalService proposalService;

	public int create(Intransfer intransfer) {

		return masterIntransferDao.insert(intransfer);
	}

	public int createBatch(List<Intransfer> intransfers) {
		
		return masterIntransferDao.createBatch(intransfers);
	}

	@Override
	public void batchExcute(IntransferVo vo) throws Exception {
		String bankId, merchantId;
		BankInfo bankInfoFrom = null, bankInfoTo = null;
		MerchantPay mpFrom, mpTo;
		Proposal proposal;
		String pnos = "";

		List<Intransfer> intransfers = new ArrayList<>();
		List<Proposal> proposals = new ArrayList<>();
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < vo.getNumber(); i++) {
			String pno = basicService.generateTransferNo(516);
			pnos += pno + ",";
			Intransfer intransfer = new Intransfer();
			intransfer.setPno(pno);
			intransfer.setAmount(vo.getAmount());

			String output_bank[] = vo.getOutput().split("b_");
			String output_online[] = vo.getOutput().split("o_");
			if (output_bank != null && output_bank.length > 1) {
				bankId = output_bank[1];
				bankInfoFrom = bankInfoService.findById(Integer.valueOf(bankId));
				intransfer.setWherefrom(bankInfoFrom.getUserName());
			} else if (output_online != null && output_online.length > 1) {
				merchantId = output_online[1];
				mpFrom = merchantPayService.findById(Long.valueOf(merchantId));
				intransfer.setWherefrom(mpFrom.getPayName());
			}

			String input_bank[] = vo.getInput().split("b_");
			String input_online[] = vo.getInput().split("o_");
			if (input_bank != null && input_bank.length > 1) {
				bankId = input_bank[1];
				bankInfoTo = bankInfoService.findById(Integer.valueOf(bankId));
				intransfer.setWhereto(bankInfoTo.getUserName());
			} else if (input_online != null && input_online.length > 1) {
				merchantId = input_online[1];
				mpTo = merchantPayService.findById(Long.valueOf(merchantId));
				intransfer.setWhereto(mpTo.getPayName());
			}

			intransfer.setOperator(vo.getLoginname());
			intransfer.setRemark(vo.getRemark());
			intransfer.setFee(vo.getFee());
			intransfer.setCreateTime(DateUtil.getCurrentTimestamp());
			intransfer.setFromto(vo.getOutput() + "※" + vo.getInput());
			intransfer.setTransferflag(vo.getTransferflag());

			intransfers.add(intransfer);

			if ((bankInfoFrom != null && bankInfoFrom.getBankType() == 1) ||
					(bankInfoTo != null && bankInfoTo.getBankType() == 1)) {
				proposal = new Proposal(pno, vo.getLoginname(), DateUtil.getCurrentTimestamp(), 516, 0,
						intransfer.getWherefrom() + "->" + intransfer.getWhereto(), vo.getAmount(), "",
						ProposalFlagType.SUBMITED.getCode(), "后台", vo.getLoginname(), null, vo.getTransferflag());
			} else {
				proposal = new Proposal(pno, vo.getLoginname(), DateUtil.getCurrentTimestamp(), 516, 0,
						intransfer.getWherefrom() + "->" + intransfer.getWhereto(), vo.getAmount(), "",
						ProposalFlagType.AUDITED.getCode(), "后台", vo.getLoginname(), null, vo.getTransferflag());
			}
			proposals.add(proposal);

			Task audit = new Task(pno, LevelType.AUDIT.getCode(), TaskFlagType.SUBMITED.getCode(), DateUtil.getCurrentTimestamp(), null, vo.getLoginname());
			tasks.add(audit);
			Task excute = new Task(pno, LevelType.EXCUTE.getCode(), TaskFlagType.SUBMITED.getCode(), DateUtil.getCurrentTimestamp(), null, vo.getLoginname());
			tasks.add(excute);
		}
		createBatch(intransfers);
		proposalService.batchCreate(proposals);
		taskService.createBatch(tasks);
		log.info("提案成功：intransfers size =" + intransfers.size() + ",proposals size=" + proposals.size() + ",pnos=" + pnos);
	}
}