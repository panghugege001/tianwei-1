package com.nnti.common.dao.slave;

import java.util.Map;
import com.nnti.common.model.vo.PTProfit;

public interface ISlavePTProfitDao {

	Double queryBetCredit(PTProfit profit);

	Double sumBetCredit(Map<String, Object> paramsMap);
}