package com.nnti.office.dao.i18n;

import java.util.List;

import com.nnti.office.model.i18n.I18n;

public interface I18nDao {
	
	public List<I18n> batchGetI18nCn(List<String> keyList);
	
	public List<I18n> batchGetI18nEn(List<String> keyList);
	
	public List<I18n> getAllI18n();
	
	public List<I18n> searchI18n(I18n i18n);
	
	public void insertI18n(I18n i18n);
	
	public void updateI18n(I18n i18n);
	
	public I18n getI18nById(Integer id);
	
	public void deleteI18n(Integer id);
	
	public int isDuplicated(String key);

}
