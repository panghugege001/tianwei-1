package com.nnti.common.dao.slave;

import java.util.List;
import com.nnti.common.model.vo.CreditLog;

public interface ISlaveCreditLogDao {

	List<CreditLog> findCreditLogList(CreditLog creditLog);

	Integer count(CreditLog creditLog);
}