package com.nnti.office.service.i18n;

import java.util.List;

import com.nnti.office.model.i18n.I18n;

public interface I18nService {
	
	public List<I18n> batchGetI18n(List<String> keyList,String language);
	
	public List<I18n> getAllI18n();
	
	public List<I18n> searchI18n(I18n i18n);
	
	public void insertI18n(I18n i18n);
	
	public void updateI18n(I18n i18n);
	
	public I18n getI18nById(Integer id);
	
	public void deleteI18n(Integer id);
	
	public boolean isDuplicated(String key);
	
}
