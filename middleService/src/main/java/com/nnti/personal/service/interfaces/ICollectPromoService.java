package com.nnti.personal.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.CollectPromo;

public interface ICollectPromoService {

	List<CollectPromo> findList(Map<String, Object> paramsMap) throws Exception;

	int update(Map<String, Object> paramsMap) throws Exception;
}