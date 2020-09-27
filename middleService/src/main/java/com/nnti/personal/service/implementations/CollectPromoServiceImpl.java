package com.nnti.personal.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.personal.dao.master.IMasterCollectPromoDao;
import com.nnti.personal.model.vo.CollectPromo;
import com.nnti.personal.service.interfaces.ICollectPromoService;

@Service
public class CollectPromoServiceImpl implements ICollectPromoService {

	@Autowired
	private IMasterCollectPromoDao masterCollectPromoDao;

	public List<CollectPromo> findList(Map<String, Object> paramsMap) throws Exception {

		return masterCollectPromoDao.findList(paramsMap);
	}

	public int update(Map<String, Object> paramsMap) throws Exception {

		return masterCollectPromoDao.update(paramsMap);
	}
}