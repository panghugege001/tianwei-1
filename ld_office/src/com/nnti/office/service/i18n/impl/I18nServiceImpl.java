package com.nnti.office.service.i18n.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.office.dao.i18n.I18nDao;
import com.nnti.office.model.i18n.I18n;
import com.nnti.office.service.i18n.I18nService;

@Service
public class I18nServiceImpl implements I18nService{
	
	@Autowired
	I18nDao i18nDao;
	
	@Override
	public List<I18n> batchGetI18n(List<String> keyList,String language) {
		if(language.equals("cn")) {
			return i18nDao.batchGetI18nCn(keyList);
		}else if(language.equals("en")) {
			return i18nDao.batchGetI18nEn(keyList);
		}
		return null;
	}

	@Override
	public List<I18n> getAllI18n() {
		return i18nDao.getAllI18n();
	}

	@Override
	public List<I18n> searchI18n(I18n i18n) {
		return i18nDao.searchI18n(i18n);
	}

	@Override
	public void insertI18n(I18n i18n) {
		i18n.setCreateTime(new Date());
		i18nDao.insertI18n(i18n);
	}

	@Override
	public void updateI18n(I18n i18n) {
		i18nDao.updateI18n(i18n);
	}

	@Override
	public I18n getI18nById(Integer id) {
		return i18nDao.getI18nById(id);
	}

	@Override
	public void deleteI18n(Integer id) {
		i18nDao.deleteI18n(id);
	}

	@Override
	public boolean isDuplicated(String key) {
		int count = i18nDao.isDuplicated(key);
		if(count > 0) {
			return true;
		}
		return false;
	}
	
}
