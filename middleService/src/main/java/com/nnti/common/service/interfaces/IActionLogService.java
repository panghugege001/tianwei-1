package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.ActionLog;

public interface IActionLogService {

	int create(ActionLog actionLog) throws Exception;
}