package com.gsmc.png.service.implementations;

import java.util.List;
import java.util.Map;

import com.gsmc.png.service.interfaces.CommonService;


public class CommonServiceImpl extends UniversalServiceImpl implements CommonService {

    @Override
    public int executeSql(String sql, Map<String, Object> params) {
        return universalDao.excuteSql(sql, params);
    }

    @Override
    public Object getOneValue(String sql, Map<String, Object> params) {
        return universalDao.getOneValue(sql, params);
    }

	@Override
	public List excuteQuerySql(String sql, Map<String, Object> params){
		return universalDao.excuteQuerySql(sql, params);
	}

}
