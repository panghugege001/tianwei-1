package com.nnti.personal.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.RedEnvelopeActivity;

public interface ISlaveRedEnvelopeActivityDao {

	List<RedEnvelopeActivity> findList(Map<String, Object> paramsMap);
}