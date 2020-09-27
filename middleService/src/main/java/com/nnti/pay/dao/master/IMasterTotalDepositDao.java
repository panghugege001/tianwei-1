package com.nnti.pay.dao.master;

import com.nnti.pay.model.vo.TotalDeposit;

public interface IMasterTotalDepositDao {

	Integer insert(TotalDeposit totalDeposit);

	Integer update(TotalDeposit totalDeposit);
}