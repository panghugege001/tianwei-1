package com.nnti.personal.dao.master;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.PreferentialConfig;

public interface IMasterPreferentialConfigDao {

	PreferentialConfig get(Long id);

	List<PreferentialConfig> findList(Map<String, Object> paramsMap);
}