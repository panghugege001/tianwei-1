package com.gsmc.png.service.implementations;
import java.util.Date;
import java.util.List;

import com.gsmc.png.dao.IGetdataDao;
import com.gsmc.png.model.PlatformData;
import com.gsmc.png.service.interfaces.IGetdateService;

public class GetdataServiceImpl implements IGetdateService{

	private IGetdataDao getDataDao;
	
	@Override
	public List<PlatformData> selectQtData(Date startT, Date endT){
		return getDataDao.selectQtData(startT, endT);
	}

	@Override
	public void updateQtPlatForm(PlatformData data) {
		getDataDao.updateQtPlatForm(data) ;
	}

	public void setGetDataDao(IGetdataDao getDataDao) {
		this.getDataDao = getDataDao;
	}
	
	@Override
	public void updateTTGPlatForm(PlatformData data) {
		getDataDao.updateTTGPlatForm(data) ;
	}
}