package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterPreferTransferRecordDao;
import com.nnti.common.model.vo.PreferTransferRecord;
import com.nnti.common.service.interfaces.IPreferTransferRecordService;

@Service
public class PreferTransferRecordServiceImpl implements IPreferTransferRecordService {

	@Autowired
	private IMasterPreferTransferRecordDao masterPreferTransferRecordDao;
	
	public int create(PreferTransferRecord preferTransferRecord) throws Exception {
		
		return masterPreferTransferRecordDao.create(preferTransferRecord);
	}
}