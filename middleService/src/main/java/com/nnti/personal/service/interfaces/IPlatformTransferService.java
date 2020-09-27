package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;

public interface IPlatformTransferService {

	// 转入
	String transferIn(AccountTransferDTO dto) throws Exception;

	// 转出
	String transferOut(AccountTransferDTO dto) throws Exception;

	// 红包优惠券
	String redEnvelopeCoupon(AccountTransferDTO dto) throws Exception;

	// 存送优惠券
	String depositCoupon(AccountTransferDTO dto) throws Exception;

	// 自助体验金
	String experienceGold(SelfExperienceDTO dto) throws Exception;
}