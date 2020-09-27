package com.nnti.personal.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.personal.dao.slave.ISlaveRedEnvelopeActivityDao;
import com.nnti.personal.model.vo.RedEnvelopeActivity;
import com.nnti.personal.service.interfaces.IRedEnvelopeActivityService;

@Service
public class RedEnvelopeActivityServiceImpl implements IRedEnvelopeActivityService {

	@Autowired
	private ISlaveRedEnvelopeActivityDao slaveRedEnvelopeActivityDao;

	public List<RedEnvelopeActivity> findList(Map<String, Object> paramsMap) throws Exception {

		return slaveRedEnvelopeActivityDao.findList(paramsMap);
	}
}