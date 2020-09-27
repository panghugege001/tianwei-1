package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.dto.ActivityDTO;

public interface IActivityService {

	// 红包雨活动->领取
	String receiveRedEnvelope(ActivityDTO dto) throws Exception;
}