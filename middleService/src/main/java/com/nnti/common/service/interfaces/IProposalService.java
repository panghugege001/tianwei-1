package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Proposal;

public interface IProposalService {
	
	Proposal get(String pno) throws Exception;
	
	int update(Proposal proposal) throws Exception;

	Integer preferentialTimes(Map<String, Object> paramsMap) throws Exception;
	
	Integer experienceGoldTimes(Map<String, Object> paramsMap) throws Exception;
	
	List<Proposal> findProposalList(Map<String, Object> params) throws Exception;
	
	int create(Proposal proposal) throws Exception;

	int batchCreate(List<Proposal> createList) throws Exception;
    
	Double sumAmount(Proposal proposal) throws Exception;
	
	Double getDepositAmount(Map<String, Object> params) throws Exception;
    
    Double getWithdrawAmount(Map<String, Object> params) throws Exception;
    
    Double getYouHuiAmount(Map<String, Object> params) throws Exception;
    
    List<Proposal> existNotMsbankAuditedProposal(Map<String, Object> params) throws Exception;
    
    Proposal single(Map<String, Object> paramsMap) throws Exception;
    
	int updateProposal(Map<String, Object> paramsMap) throws Exception;
	
	List<Proposal> findList(Map<String, Object> paramsMap) throws Exception;
	
	Integer countFirstDeposit(Map<String, Object> paramsMap) throws Exception;
	
	Integer count(Map<String, Object> paramsMap) throws Exception;
}