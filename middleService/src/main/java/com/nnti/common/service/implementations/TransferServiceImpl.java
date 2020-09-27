package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterTransferDao;
import com.nnti.common.dao.slave.ISlaveTransferDao;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.service.interfaces.ITransferService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransferServiceImpl implements ITransferService {

	@Autowired
	private IMasterTransferDao masterTransferDao;
	@Autowired
	private ISlaveTransferDao slaveTransferDao;

	public List<Transfer> findList(Map<String, Object> paramsMap) throws Exception {

		return slaveTransferDao.findList(paramsMap);
	}
	
	public List<Transfer> findUsedTransferList(Map<String, Object> paramsMap) throws Exception {
		
		return slaveTransferDao.findUsedTransferList(paramsMap);
	}

	public int create(Transfer transfer) throws Exception {

		return masterTransferDao.create(transfer);
	}
}