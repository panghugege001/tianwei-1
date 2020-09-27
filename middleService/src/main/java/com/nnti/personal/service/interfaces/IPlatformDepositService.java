package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.dto.SelfDepositDTO;

public interface IPlatformDepositService {

	// 首存优惠
	String firstDeposit(SelfDepositDTO dto) throws Exception;
	
	// 次存优惠
	String timesDeposit(SelfDepositDTO dto) throws Exception;
	
	// 限时优惠
	String limitedTime(SelfDepositDTO dto) throws Exception;
}