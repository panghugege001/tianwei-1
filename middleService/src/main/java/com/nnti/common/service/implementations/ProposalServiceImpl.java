package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterProposalDao;
import com.nnti.common.dao.slave.ISlaveProposalDao;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.service.interfaces.IProposalService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProposalServiceImpl implements IProposalService {
	
	@Autowired
	private IMasterProposalDao masterProposalDao;
	@Autowired
	private ISlaveProposalDao slaveProposalDao;
	
	public Proposal get(String pno) throws Exception {

		return masterProposalDao.get(pno);
	}
	
	public int update(Proposal proposal) throws Exception {
		
		return masterProposalDao.update(proposal);
	}
	
	public Integer preferentialTimes(Map<String, Object> paramsMap) throws Exception {
		
		return slaveProposalDao.preferentialTimes(paramsMap);
	}
	
	public Integer experienceGoldTimes(Map<String, Object> paramsMap) throws Exception {
		
		return slaveProposalDao.experienceGoldTimes(paramsMap);
	}
	
	public List<Proposal> findProposalList(Map<String, Object> params) throws Exception {

		return masterProposalDao.findProposalList(params);
	}
	
	public int create(Proposal proposal) throws Exception {
		
		return masterProposalDao.create(proposal);
	}
	
	public int batchCreate(List<Proposal> createList) throws Exception {
		
		return masterProposalDao.batchCreate(createList);
	}
	
	public Double sumAmount(Proposal proposal) throws Exception {
	
		return slaveProposalDao.sumAmount(proposal);
	}
	
	public Double getDepositAmount(Map<String, Object> params) {
		return slaveProposalDao.getDepositAmount(params);
	}
	
	public Double getWithdrawAmount(Map<String, Object> params) {
		return slaveProposalDao.getWithdrawAmount(params);
	}
	
	public Double getYouHuiAmount(Map<String, Object> params) {
		return slaveProposalDao.getYouHuiAmount(params);
	}

	@Override
	public List<Proposal> existNotMsbankAuditedProposal(Map<String, Object> params) throws Exception {
		return masterProposalDao.existNotMsbankAuditedProposal(params);
	}

	public Proposal single(Map<String, Object> paramsMap) throws Exception {

		return slaveProposalDao.single(paramsMap);
	}
	
	public int updateProposal(Map<String, Object> paramsMap) throws Exception {
		
		return masterProposalDao.updateProposal(paramsMap);
	}
	
	public List<Proposal> findList(Map<String, Object> paramsMap) throws Exception {
		
		return slaveProposalDao.findList(paramsMap);
	}
	
	public Integer countFirstDeposit(Map<String, Object> paramsMap) throws Exception {
		
		return slaveProposalDao.countFirstDeposit(paramsMap);
	}
	
	public Integer count(Map<String, Object> paramsMap) throws Exception {
		
		return slaveProposalDao.count(paramsMap);
	}
}