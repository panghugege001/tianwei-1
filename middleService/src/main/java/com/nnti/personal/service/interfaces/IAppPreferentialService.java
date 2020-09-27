package com.nnti.personal.service.interfaces;

import java.util.List;
import java.util.Map;

import com.nnti.personal.model.vo.AppPreferential;

public interface IAppPreferentialService {

	public List<AppPreferential> findAppPreferentialList(Map<String, Object> params) throws Exception;

}
