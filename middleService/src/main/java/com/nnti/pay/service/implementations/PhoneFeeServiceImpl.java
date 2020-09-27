package com.nnti.pay.service.implementations;

import com.nnti.common.constants.CreditType;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.dao.master.IMasterProposalDao;
import com.nnti.common.dao.master.IMasterUserDao;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.common.utils.PhoneFeeUtil;
import com.nnti.pay.controller.vo.PhoneFeeVo;
import com.nnti.pay.dao.master.IMasterPhoneFeeDao;
import com.nnti.pay.model.enums.ProposalFlagType;
import com.nnti.pay.model.vo.PhoneFeeRecord;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IPhoneFeeService;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PhoneFeeServiceImpl implements IPhoneFeeService {

	@Autowired
	private IMasterPhoneFeeDao masterPhoneFeeDao;
	@Autowired
	private IMasterProposalDao masterProposalDao;
	@Autowired
	private IMasterUserDao masterUserDao;
	@Autowired
	private IBasicService basicService;
	@Autowired
    private ICreditLogService creditLogService;
	
	@Override
	public String phoneFeeCallBack(PhoneFeeVo vo) throws Exception {
		
		if(!PhoneFeeUtil.valiedMD5BySign(vo.getSign(), vo.getSporder_id(), vo.getOrderid())){
			return "验签失败！";
		}
		
		PhoneFeeRecord phoneFeeRecord = masterPhoneFeeDao.findPhoneFeeRecord(vo.getOrderid());
		if(phoneFeeRecord == null){
			return "未找到该订单！";
		}
		if(phoneFeeRecord.getStatus() == 2){
			return "success";
		}
		
		User user = masterUserDao.get(phoneFeeRecord.getLoginname());
		if(user == null){
			return "未查找到该用户！";
		}
		
		Timestamp now = DateUtil.getCurrentTimestamp();
		phoneFeeRecord.setSporder_id(vo.getSporder_id());
		phoneFeeRecord.setCallback_time(now);
		if(vo.getSta() == 1){
			
			phoneFeeRecord.setStatus(2);
			String pno = basicService.generateTransferNo(ProposalType.SELF_888.getCode());
			
			Proposal proposal = new Proposal(pno, phoneFeeRecord.getLoginname(), now, ProposalType.SELF_888.getCode(), user.getLevel(),
					phoneFeeRecord.getLoginname(), null, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), "后台",
					"referenceNo:" + phoneFeeRecord.getOrder_no(), null, null);
            
			proposal.setGiftAmount(Double.parseDouble(phoneFeeRecord.getAmount().toString()));
			proposal.setExecuteTime(now);
			/*** 记录交易日志 */
            CreditLog creditLog = new CreditLog();
            creditLog.setLoginName(user.getLoginName());
            creditLog.setType(CreditType.PHONEFEE_CHARGE.getCode());
            creditLog.setCredit(user.getCredit());
            creditLog.setRemit(0.0);
            creditLog.setCreateTime(now);
            creditLog.setNewCredit(user.getCredit());
            creditLog.setRemark("referenceNo:" + pno + ";抢话费活动充值,金额:" + proposal.getGiftAmount());
            this.creditLogService.create(creditLog);
			this.masterProposalDao.create(proposal);
			
		} else if(vo.getSta() == 9){
			phoneFeeRecord.setStatus(3);
		}
		
		this.masterPhoneFeeDao.update(phoneFeeRecord);
		return "success";
	}
	
}