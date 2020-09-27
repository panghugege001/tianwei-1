package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Transfer;

public interface ISlaveTransferDao {

	List<Transfer> findList(Map<String, Object> paramsMap);
	
	List<Transfer> findUsedTransferList(Map<String, Object> paramsMap);
}