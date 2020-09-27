package com.nnti.pay.dao.slave;

import com.nnti.pay.model.vo.TotalDeposit;
import java.util.List;

public interface ISlaveTotalDepositDao {

    List<TotalDeposit> findByLoginName(String loginname);
}