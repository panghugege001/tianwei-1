package com.nnti.common.service.interfaces;

import java.util.Map;
import com.nnti.common.model.vo.ProposalExtend;

public interface IProposalExtendService {

	int create(ProposalExtend proposalExtend) throws Exception;
	
	Integer count(Map<String, Object> params) throws Exception;
}