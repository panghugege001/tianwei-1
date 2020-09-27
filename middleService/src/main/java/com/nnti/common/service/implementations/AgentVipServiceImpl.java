package com.nnti.common.service.implementations;

import com.nnti.common.dao.slave.ISlaveAgentVipDao;
import com.nnti.common.model.vo.AgentVip;
import com.nnti.common.service.interfaces.IAgentVipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AgentVipServiceImpl implements IAgentVipService {
	
	@Autowired
	private ISlaveAgentVipDao slaveAgentVipDao;
	

	@Override
	public AgentVip get(String id) throws Exception {
		return null;
	}

	@Override
	public List<AgentVip> findAgentVipList(Map<String, Object> params) throws Exception {
		return slaveAgentVipDao.findAgentVipList(params);
	}
}