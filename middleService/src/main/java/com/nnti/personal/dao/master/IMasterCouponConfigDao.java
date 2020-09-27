package com.nnti.personal.dao.master;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.CouponConfig;

public interface IMasterCouponConfigDao {

	List<CouponConfig> findList(Map<String, Object> paramsMap);

	int update(Map<String, Object> paramsMap);
}
