package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterPreferentialRecordDao;
import com.nnti.common.dao.slave.ISlavePreferentialRecordDao;
import com.nnti.common.model.vo.PreferentialRecord;
import com.nnti.common.service.interfaces.IPreferentialRecordService;

@Service
public class PreferentialRecordServiceImpl implements IPreferentialRecordService {

	@Autowired
	private IMasterPreferentialRecordDao masterPreferentialRecordDao;
	@Autowired
	private ISlavePreferentialRecordDao slavePreferentialRecordDao;
	
	public PreferentialRecord get(String pno) throws Exception {

		return slavePreferentialRecordDao.get(pno);
	}
	
	public int create(PreferentialRecord preferentialRecord) throws Exception {
		
		return masterPreferentialRecordDao.create(preferentialRecord);
	}
}