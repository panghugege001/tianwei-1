package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.dto.SelfDepositDTO;

public interface ISelfDepositService {
	
	// 个人中心->自助优惠->自助存送->提交
	String submit(SelfDepositDTO dto) throws Exception;
}