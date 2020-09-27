package com.nnti.common.service.implementations;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.dao.master.IMasterProposalExtendDao;
import com.nnti.common.dao.slave.ISlaveProposalExtendDao;
import com.nnti.common.model.vo.ProposalExtend;
import com.nnti.common.service.interfaces.IProposalExtendService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProposalExtendServiceImpl implements IProposalExtendService {

	@Autowired
	private IMasterProposalExtendDao masterProposalExtendDao;
	@Autowired
	private ISlaveProposalExtendDao slaveProposalExtendDao;
	
	public int create(ProposalExtend proposalExtend) throws Exception {
		
		return masterProposalExtendDao.create(proposalExtend);
	}
	
	public Integer count(Map<String, Object> params) throws Exception {
	
		return slaveProposalExtendDao.count(params);
	}
}