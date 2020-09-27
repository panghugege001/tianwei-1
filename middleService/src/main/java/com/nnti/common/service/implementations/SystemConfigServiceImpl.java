package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.dao.master.IMasterSystemConfigDao;
import com.nnti.common.dao.master.IMasterUserStatusDao;
import com.nnti.common.model.vo.SystemConfig;
import com.nnti.common.service.interfaces.ISystemConfigService;
import com.nnti.common.service.interfaces.IUserStatusService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemConfigServiceImpl implements ISystemConfigService {

	@Autowired
	private IMasterSystemConfigDao masterSystemConfigDao;
	
	public SystemConfig get(SystemConfig systemConfig) throws Exception {

		return masterSystemConfigDao.get(systemConfig);
	}
}