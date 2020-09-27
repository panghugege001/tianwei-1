package com.nnti.personal.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.PreferentialConfig;

public interface IPreferentialConfigService {

	PreferentialConfig get(Long id) throws Exception;

	List<PreferentialConfig> findList(Map<String, Object> paramsMap) throws Exception;
}