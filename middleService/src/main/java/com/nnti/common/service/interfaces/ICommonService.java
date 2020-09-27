package com.nnti.common.service.interfaces;

import java.util.Map;

public interface ICommonService {

	/**
	 * @description：获取指定时间段内的玩家存款额
	 */
	Double getDeposit(Map<String, Object> paramsMap) throws Exception;

	/**
	 * @description：获取指定时间段内的玩家输赢值
	 */
	Double getWinLose(Map<String, Object> paramsMap) throws Exception;

	/**
	 * @description：获取指定时间段内的玩家输赢值
	 */
	Double sumProfitAmount(Map<String, Object> paramsMap) throws Exception;

	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	Double sumProfitBetTotal(Map<String, Object> paramsMap) throws Exception;

	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	Double sumPlatformBet(Map<String, Object> paramsMap) throws Exception;
	
	/**
	 * @description：获取指定时间段内的玩家mg投注额
	 */
	Double sumMgBet(Map<String, Object> paramsMap) throws Exception;

	/**
	 * @description：获取指定时间段内的玩家png投注额
	 */
	Double sumPngBet(Map<String, Object> params) throws Exception;

	/**
	 * @description：获取指定时间段内的玩家sw投注额
	 */
	Double sumSwBet(Map<String, Object> params) throws Exception;
	
	/**
	 * @description：获取指定时间段内的玩家agin投注额
	 */
	Double sumAginBet(Map<String, Object> params) throws Exception;
}