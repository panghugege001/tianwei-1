package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterCmbTransferDao;
import com.nnti.common.dao.slave.ISlaveCmbTransferDao;
import com.nnti.common.model.vo.CmbTransfer;
import com.nnti.common.service.interfaces.ICmbTransferService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CmbTransferServiceImpl implements ICmbTransferService {

	@Autowired
	private IMasterCmbTransferDao masterCmbTransferDao;
	@Autowired
	private ISlaveCmbTransferDao slaveCmbTransferDao;

	public int createCmbTransferList(List<CmbTransfer> createList) throws Exception {

		return masterCmbTransferDao.createCmbTransferList(createList);
	}

	public List<CmbTransfer> findList(Map<String, Object> paramsMap) throws Exception {

		return slaveCmbTransferDao.findList(paramsMap);
	}
}