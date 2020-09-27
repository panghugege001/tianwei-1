package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterBigBangDao;
import com.nnti.common.dao.slave.ISlaveBigBangDao;
import com.nnti.common.model.vo.BigBang;
import com.nnti.common.service.interfaces.IBigBangService;

@Service
@Transactional(rollbackFor = Exception.class)
public class BigBangServiceImpl implements IBigBangService {

	@Autowired
	private IMasterBigBangDao masterBigBangDao;
	@Autowired
	private ISlaveBigBangDao slaveBigBangDao;

	public Integer count(Map<String, Object> paramsMap) throws Exception {

		return slaveBigBangDao.count(paramsMap);
	}

	public int updateList(Map<String, Object> paramsMap) throws Exception {

		return masterBigBangDao.updateList(paramsMap);
	}
	
	public int updateList4Slot(Map<String, Object> paramsMap) throws Exception {

		return masterBigBangDao.updateList4Slot(paramsMap);
	}
	
	public List<BigBang> findList(Map<String, Object> paramsMap) throws Exception {

		return slaveBigBangDao.findList(paramsMap);
	}
	
	public List<BigBang> findListBySlot(Map<String, Object> paramsMap) throws Exception {

		return slaveBigBangDao.findListBySlot(paramsMap);
	}
}