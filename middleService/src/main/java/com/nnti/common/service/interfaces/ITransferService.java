package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Transfer;

public interface ITransferService {

	List<Transfer> findList(Map<String, Object> paramsMap) throws Exception;
	
	List<Transfer> findUsedTransferList(Map<String, Object> paramsMap) throws Exception;

	int create(Transfer transfer) throws Exception;
}