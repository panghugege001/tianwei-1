package com.nnti.personal.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.personal.dao.master.IMasterPreferentialConfigDao;
import com.nnti.personal.model.vo.PreferentialConfig;
import com.nnti.personal.service.interfaces.IPreferentialConfigService;

@Service
public class PreferentialConfigServiceImpl implements IPreferentialConfigService {

	@Autowired
	private IMasterPreferentialConfigDao masterPreferentialConfigDao;

	public PreferentialConfig get(Long id) throws Exception {

		return masterPreferentialConfigDao.get(id);
	}

	public List<PreferentialConfig> findList(Map<String, Object> paramsMap) throws Exception {

		return masterPreferentialConfigDao.findList(paramsMap);
	}
}