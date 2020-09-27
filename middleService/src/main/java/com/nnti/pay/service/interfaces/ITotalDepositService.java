package com.nnti.pay.service.interfaces;

import java.util.List;
import com.nnti.pay.model.vo.TotalDeposit;

public interface ITotalDepositService {

	List<TotalDeposit> findByLoginName(String loginname) throws Exception;

	Integer insert(TotalDeposit totalDeposit) throws Exception;

	Integer update(TotalDeposit totalDeposit) throws Exception;
}