package com.nnti.personal.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.ExperienceGoldConfig;

public interface IExperienceConfigService {

	ExperienceGoldConfig get(Integer id) throws Exception;

	List<ExperienceGoldConfig> findList(Map<String, Object> paramsMap) throws Exception;

	Map<String, Object> haveSameInfo(String loginName, String type, String sid, String channel,String product) throws Exception;
	
	
}