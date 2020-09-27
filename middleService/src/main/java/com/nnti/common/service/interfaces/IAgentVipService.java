package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;

import com.nnti.common.model.vo.AgentVip;

public interface IAgentVipService {

	AgentVip get(String id) throws Exception;
	
	List<AgentVip> findAgentVipList(Map<String, Object>params) throws Exception;
}