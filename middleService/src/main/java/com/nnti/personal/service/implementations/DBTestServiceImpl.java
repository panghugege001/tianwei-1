package com.nnti.personal.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterDBTestDao;

@Service("dbTestService")
public class DBTestServiceImpl implements IDBTestService {


	@Autowired
	private IMasterDBTestDao masterDBTestDao;

	@Override
	public int masterDBTest() {
		return masterDBTestDao.queryTest();
	}

	
}