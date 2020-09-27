package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.SystemConfig;

public interface ISystemConfigService {

	SystemConfig get(SystemConfig systemConfig) throws Exception;
	
}