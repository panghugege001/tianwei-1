package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.LosePromo;
import com.nnti.common.page.Page;

public interface ISlaveLosePromoDao {

	Page<LosePromo> findPageList(Map<String, Object> paramsMap);

	List<LosePromo> findList(Map<String, Object> paramsMap);
}