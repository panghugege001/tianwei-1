package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterSelfRecordDao;
import com.nnti.common.dao.slave.ISlaveSelfRecordDao;
import com.nnti.common.model.vo.SelfRecord;
import com.nnti.common.service.interfaces.ISelfRecordService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SelfRecordServiceImpl implements ISelfRecordService {

	@Autowired
	private IMasterSelfRecordDao masterSelfRecordDao;
	@Autowired
	private ISlaveSelfRecordDao slaveSelfRecordDao;

	public int updateList(Map<String, Object> paramsMap) throws Exception {

		return masterSelfRecordDao.updateList(paramsMap);
	}

	public int create(SelfRecord selfRecord) throws Exception {

		return masterSelfRecordDao.create(selfRecord);
	}

	public int update(SelfRecord selfRecord) throws Exception {

		return masterSelfRecordDao.update(selfRecord);
	}

	public List<SelfRecord> findList(Map<String, Object> paramsMap) throws Exception {

		return slaveSelfRecordDao.findList(paramsMap);
	}
}