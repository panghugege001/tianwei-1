package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Proposal;

public interface ISlaveProposalDao {

	Proposal get(String pno);

	Double sumAmount(Proposal proposal);

	Integer preferentialTimes(Map<String, Object> paramsMap);
	
	Integer experienceGoldTimes(Map<String, Object> paramsMap);

	List<Proposal> findProposalList(Map<String, Object> params);

	Double getDepositAmount(Map<String, Object> params);

	Double getWithdrawAmount(Map<String, Object> params);

	Double getYouHuiAmount(Map<String, Object> params);
	
	List<Proposal> existNotMsbankAuditedProposal(Map<String, Object> params);
	
	Proposal single(Map<String, Object> paramsMap);
	
	List<Proposal> findList(Map<String, Object> paramsMap);

	Integer countFirstDeposit(Map<String, Object> paramsMap);
	
	Integer count(Map<String, Object> paramsMap);
}