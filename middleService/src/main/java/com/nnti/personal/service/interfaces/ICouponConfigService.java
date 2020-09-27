package com.nnti.personal.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.CouponConfig;

public interface ICouponConfigService {

	List<CouponConfig> findList(Map<String, Object> paramsMap);

	int update(Map<String, Object> paramsMap);
}
