package com.nnti.personal.dao.master;

import java.util.List;
import java.util.Map;

import com.nnti.personal.model.vo.AppPreferential;

public interface IMasterAppPreferentialDao {

	public List<AppPreferential> findAppPreferentialList(Map<String, Object> params);
}