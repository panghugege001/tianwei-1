package com.nnti.common.dao.master;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Proposal;

public interface IMasterProposalDao {
	
	Proposal get(String pno);

	int create(Proposal proposal);

	int update(Proposal proposal);

	int delete(Proposal proposal);

	int batchCreate(List<Proposal> createList);
	
	List<Proposal> findProposalList(Map<String, Object> params);
	
	List<Proposal> existNotMsbankAuditedProposal(Map<String, Object> params);
	
	int updateProposal(Map<String, Object> paramsMap);
}