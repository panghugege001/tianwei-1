package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.CmbTransfer;

public interface ICmbTransferService {

	int createCmbTransferList(List<CmbTransfer> createList) throws Exception;

	List<CmbTransfer> findList(Map<String, Object> paramsMap) throws Exception;
}