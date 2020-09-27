package com.nnti.personal.service.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.personal.dao.master.IMasterAppPreferentialDao;
import com.nnti.personal.model.vo.AppPreferential;
import com.nnti.personal.service.interfaces.IAppPreferentialService;

@Service
public class AppPreferentialserviceImpl implements IAppPreferentialService {

	@Autowired
	private IMasterAppPreferentialDao appPreferentialDao;

	public List<AppPreferential> findAppPreferentialList(Map<String, Object> params) throws Exception {
		return appPreferentialDao.findAppPreferentialList(params);
	}

}
