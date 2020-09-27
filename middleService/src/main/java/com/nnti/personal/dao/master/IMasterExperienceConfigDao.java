package com.nnti.personal.dao.master;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.ExperienceGoldConfig;

public interface IMasterExperienceConfigDao {

	ExperienceGoldConfig get(Integer id);

	List<ExperienceGoldConfig> findList(Map<String, Object> paramsMap);
}