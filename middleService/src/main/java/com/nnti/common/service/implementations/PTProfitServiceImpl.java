package com.nnti.common.service.implementations;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.slave.ISlavePTProfitDao;
import com.nnti.common.model.vo.PTProfit;
import com.nnti.common.service.interfaces.IPTProfitService;

@Service
public class PTProfitServiceImpl implements IPTProfitService {

	@Autowired
	private ISlavePTProfitDao slavePTProfitDao;

	public Double queryBetCredit(PTProfit profit) throws Exception {

		return slavePTProfitDao.queryBetCredit(profit);
	}

	public Double sumBetCredit(Map<String, Object> paramsMap) throws Exception {

		return slavePTProfitDao.sumBetCredit(paramsMap);
	}
}