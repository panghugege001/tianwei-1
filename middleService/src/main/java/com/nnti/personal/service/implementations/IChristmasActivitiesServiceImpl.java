/**
 * 
 */
package com.nnti.personal.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.personal.dao.slave.IChristmasActivitiesDao;
import com.nnti.personal.model.vo.ChristmasActivities;
import com.nnti.personal.service.interfaces.IChristmasActivitiesService;

/**
 * TODO
 * @author Caesar
 * 2017年12月8日上午11:33:34
 */
@Service
public class IChristmasActivitiesServiceImpl implements IChristmasActivitiesService{
	@Autowired
	private IChristmasActivitiesDao christmasActivitiesDao;

	@Override
	public ChristmasActivities getByType(String type) {
		return christmasActivitiesDao.getByType(type);
	}
	
}
