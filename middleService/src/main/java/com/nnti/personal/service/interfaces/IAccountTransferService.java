package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;

public interface IAccountTransferService {
	

	// 个人中心->个人钱包->户内转账->提交
	String submit(AccountTransferDTO dto) throws Exception;
	
	// 个人中心->个人钱包->户内转账->一键转账
	String oneKeyGameMoney(AccountTransferDTO dto) throws Exception;

	// 个人中心->自助优惠->优惠券专区->红包优惠券->提交
	String redEnvelopeCoupon(AccountTransferDTO dto) throws Exception;

	// 个人中心->自助优惠->优惠券专区->存送优惠券->提交
	String depositCoupon(AccountTransferDTO dto) throws Exception;

	// 个人中心->自助优惠->自助体验金->提交
	String experienceGold(SelfExperienceDTO dto) throws Exception;
	
	// 三存送活动
	String activities(AccountTransferDTO dto) throws Exception;
}