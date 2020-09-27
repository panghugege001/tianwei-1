package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.CmbTransfer;

public interface ISlaveCmbTransferDao {

	List<CmbTransfer> findList(Map<String, Object> paramsMap);
}