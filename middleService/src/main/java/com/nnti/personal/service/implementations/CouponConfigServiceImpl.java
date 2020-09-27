package com.nnti.personal.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.personal.dao.master.IMasterCouponConfigDao;
import com.nnti.personal.model.vo.CouponConfig;
import com.nnti.personal.service.interfaces.ICouponConfigService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CouponConfigServiceImpl implements ICouponConfigService {

	@Autowired
	private IMasterCouponConfigDao masterCouponConfigDao;

	public List<CouponConfig> findList(Map<String, Object> paramsMap) {

		return masterCouponConfigDao.findList(paramsMap);
	}

	public int update(Map<String, Object> paramsMap) {

		return masterCouponConfigDao.update(paramsMap);
	}
}
