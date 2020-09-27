package com.nnti.personal.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.personal.model.vo.RedEnvelopeActivity;

public interface IRedEnvelopeActivityService {

	List<RedEnvelopeActivity> findList(Map<String, Object> paramsMap) throws Exception;
}