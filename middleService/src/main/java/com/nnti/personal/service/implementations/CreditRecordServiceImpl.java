package com.nnti.personal.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.personal.dao.master.IMasterCreditRecordDao;
import com.nnti.personal.model.vo.CreditRecord;
import com.nnti.personal.service.interfaces.ICreditRecordService;

@Service
public class CreditRecordServiceImpl implements ICreditRecordService {

	@Autowired
	private IMasterCreditRecordDao masterCreditRecordDao;

	public int insert(CreditRecord creditRecord) throws Exception {

		return masterCreditRecordDao.insert(creditRecord);
	}
}