package com.nnti.office.action.i18n;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.nnti.office.model.auth.Permission;
import com.nnti.office.model.auth.Role;
import com.nnti.office.model.common.ResponseData;
import com.nnti.office.model.common.ResponseEnum;
import com.nnti.office.model.i18n.I18n;
import com.nnti.office.service.i18n.I18nService;
import com.nnti.office.util.StringUtil;
import com.nnti.office.util.StrutsPrintUtil;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/i18n")
@ResultPath(value="/")
public class I18nAction extends ActionSupport implements ServletResponseAware{
	
	private static final long serialVersionUID = 1815707322033915004L;
	private Logger logger = Logger.getLogger(I18nAction.class);
	
	private String i18nKeyArray;
	private String language;
	private HttpServletResponse response;
	private I18n i18n;
	private Integer id;
	
	@Autowired
	I18nService i18nService;
	
	public String getI18nKeyArray() {
		return i18nKeyArray;
	}

	public void setI18nKeyArray(String i18nKeyArray) {
		this.i18nKeyArray = i18nKeyArray;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public I18n getI18n() {
		return i18n;
	}

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Action(value="getI18nMap" )
	public void getI18nMap() {
		ResponseData responseData = new ResponseData();
		//validation
		if(StringUtil.isEmpty(language)) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		List<I18n> i18nList = null;
		try {
			List<String> keyList = new ArrayList<String>();
			if(StringUtil.isNotEmpty(i18nKeyArray)) {
				String[] StringKeyArray = i18nKeyArray.split(",");
				for(String key : StringKeyArray) {
					keyList.add(key);
				}
			}
			i18nList = i18nService.batchGetI18n(keyList,language);
		}catch(Exception e) {
			logger.error("获取i18n数据异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		} 
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(i18nList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getAllI18n")
	public void getAllI18n() {
		ResponseData responseData = new ResponseData();
		List<I18n> i18nList = null;
		try {
			i18nList = i18nService.getAllI18n();
		}catch(Exception e) {
			logger.error("获取所有的国际化翻译异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(i18nList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="searchI18n" )
	public void searchI18n() {
		ResponseData responseData = new ResponseData();
		List<I18n> i18nList = null;
		//validation
		if(i18n == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			i18nList = i18nService.searchI18n(i18n);
		}catch(Exception e) {
			logger.error("搜索权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(i18nList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="addI18n" )
	public void addI18n() {
		ResponseData responseData = new ResponseData();
		//validation
		if(i18n == null || StringUtil.isEmpty(i18n.getKey()) || StringUtil.isEmpty(i18n.getCnVal()) || StringUtil.isEmpty(i18n.getEnVal()) ) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		if(i18nService.isDuplicated(i18n.getKey())) {
			responseData.setResponseEnum(ResponseEnum.DUPLICATED_VALUE);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			i18nService.insertI18n(i18n);
		}catch(Exception e) {
			logger.error("创建国际化翻译异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updateI18n" )
	public void updateI18n() {
		ResponseData responseData = new ResponseData();
		//validation
		if(i18n == null || StringUtil.isEmpty(i18n.getKey()) || StringUtil.isEmpty(i18n.getCnVal()) || StringUtil.isEmpty(i18n.getEnVal()) ) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			i18nService.updateI18n(i18n);
		}catch(Exception e) {
			logger.error("更新国际化翻译异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getI18nById")
	public void getI18nById() {
		ResponseData responseData = new ResponseData();
		I18n i18n = null;
		try {
			i18n = i18nService.getI18nById(id);
		}catch(Exception e) {
			logger.error("按照ID获取国际化翻译异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(i18n);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="deleteI18n" )
	public void deleteI18n() {
		ResponseData responseData = new ResponseData();
		//validation
		if(id == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			i18nService.deleteI18n(id);
		}catch(Exception e) {
			logger.error("删除国际化翻译异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
    public String changeLineUserRecord(){
    	return "changeLineUserRecord";
    }
	
}
