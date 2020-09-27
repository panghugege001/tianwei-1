package com.nnti.common.extend;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyConfigurer extends PropertyPlaceholderConfigurer {
	
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		
		try {
			
			String type = props.getProperty("datasource.type");

			
			if (StringUtils.isNotBlank(type)) {
				
				props.setProperty("datasource.type", SpecialEnvironmentConfig.getPlainText(type));
			}
			
			String driverClassName = props.getProperty("datasource.driverClassName");
			
			if (StringUtils.isNotBlank(driverClassName)) {
				
				props.setProperty("datasource.driverClassName", SpecialEnvironmentConfig.getPlainText(driverClassName));
			}
			
			String masterUrl = props.getProperty("master.datasource.url");
			
			if (StringUtils.isNotBlank(masterUrl)) {
				
				props.setProperty("master.datasource.url",SpecialEnvironmentConfig.getPlainText(masterUrl));
			}
			
			String masterUserName = props.getProperty("master.datasource.username");
			
			if (StringUtils.isNotBlank(masterUserName)) {
				
				props.setProperty("master.datasource.username", SpecialEnvironmentConfig.getPlainText(masterUserName));
			}
			
			String masterPassword = props.getProperty("master.datasource.password");
			
			if (StringUtils.isNotBlank(masterPassword)) {
				
				props.setProperty("master.datasource.password", SpecialEnvironmentConfig.getPlainText(masterPassword));
			}
			
			String queryUrl = props.getProperty("query.datasource.url");
			
			if (StringUtils.isNotBlank(queryUrl)) {
				
				props.setProperty("query.datasource.url", SpecialEnvironmentConfig.getPlainText(queryUrl));
			}
			
			String queryUserName = props.getProperty("query.datasource.username");
			
			if (StringUtils.isNotBlank(queryUserName)) {
				
				props.setProperty("query.datasource.username", SpecialEnvironmentConfig.getPlainText(queryUserName));
			}
			
			String queryPassword = props.getProperty("query.datasource.password");
			
			if (StringUtils.isNotBlank(queryPassword)) {
				
				props.setProperty("query.datasource.password", SpecialEnvironmentConfig.getPlainText(queryPassword));
			}
			
			super.processProperties(beanFactory, props);
		} catch (Exception e) {
			
			throw new BeanInitializationException(e.getMessage());
		}
	}
}