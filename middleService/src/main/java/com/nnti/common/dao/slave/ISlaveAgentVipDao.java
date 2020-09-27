package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;

import com.nnti.common.model.vo.AgentVip;

public interface ISlaveAgentVipDao {

	AgentVip get(String id);
	
	List<AgentVip> findAgentVipList(Map<String, Object> params);
}