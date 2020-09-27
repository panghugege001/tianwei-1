package com.nnti.common.service.interfaces;

import java.util.Map;
import com.nnti.common.model.vo.PTProfit;

public interface IPTProfitService {

	Double queryBetCredit(PTProfit profit) throws Exception;

	Double sumBetCredit(Map<String, Object> paramsMap) throws Exception;
}