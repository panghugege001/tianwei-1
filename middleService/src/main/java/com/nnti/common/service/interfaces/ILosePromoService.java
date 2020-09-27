package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.LosePromo;
import com.nnti.common.page.Page;

public interface ILosePromoService {

	Page<LosePromo> findPageList(Map<String, Object> paramsMap) throws Exception;

	List<LosePromo> findList(Map<String, Object> paramsMap) throws Exception;

	int update(Map<String, Object> paramsMap) throws Exception;
}