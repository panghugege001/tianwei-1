package com.nnti.common.service.implementations;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.slave.ISlaveCommonDao;
import com.nnti.common.service.interfaces.ICommonService;

@Service
public class CommonServiceImpl implements ICommonService {

	@Autowired
	private ISlaveCommonDao slaveCommonDao;

	/**
	 * @description：获取指定时间段内的玩家存款额
	 */
	public Double getDeposit(Map<String, Object> paramsMap) throws Exception {

		Double value = slaveCommonDao.getDeposit(paramsMap);

		return null == value ? 0.00 : value;
	}

	/**
	 * @description：获取指定时间段内的玩家输赢值
	 */
	public Double getWinLose(Map<String, Object> paramsMap) throws Exception {

		Double value = slaveCommonDao.getWinLose(paramsMap);

		return null == value ? 0.00 : value;
	}

	/**
	 * @description：获取指定时间段内的玩家输赢值
	 */
	public Double sumProfitAmount(Map<String, Object> paramsMap) throws Exception {

		Double value = slaveCommonDao.sumProfitAmount(paramsMap);

		return null == value ? 0.00 : value;
	}

	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	public Double sumProfitBetTotal(Map<String, Object> paramsMap) throws Exception {

		Double value = slaveCommonDao.sumProfitBetTotal(paramsMap);

		return null == value ? 0.00 : value;
	}

	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	public Double sumPlatformBet(Map<String, Object> paramsMap) throws Exception {

		Double value = slaveCommonDao.sumPlatformBet(paramsMap);

		return null == value ? 0.00 : value;
	}
	
	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	public Double sumMgBet(Map<String, Object> paramsMap) throws Exception {
		
		Double value = slaveCommonDao.sumMgBet(paramsMap);
		
		return null == value ? 0.00 : value;
	}
	
	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	public Double sumPngBet(Map<String, Object> paramsMap) throws Exception {
		
		Double value = slaveCommonDao.sumPngBet(paramsMap);
		
		return null == value ? 0.00 : value;
	}
	
	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	public Double sumSwBet(Map<String, Object> paramsMap) throws Exception {
		
		Double value = slaveCommonDao.sumSwBet(paramsMap);
		
		return null == value ? 0.00 : value;
	}
	
	/**
	 * @description：获取指定时间段内的玩家投注额
	 */
	public Double sumAginBet(Map<String, Object> paramsMap) throws Exception {
		
		Double value = slaveCommonDao.sumAginBet(paramsMap);
		
		return null == value ? 0.00 : value;
	}
}