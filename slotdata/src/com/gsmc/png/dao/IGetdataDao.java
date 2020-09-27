package com.gsmc.png.dao;

import java.util.Date;
import java.util.List;

import com.gsmc.png.model.PlatformData;

public interface IGetdataDao extends BaseInterfaceDao {
	public List<PlatformData> selectQtData(Date startT, Date endT);
	
	public boolean updateQtPlatForm(PlatformData data);
	
	public boolean updateTTGPlatForm(PlatformData data);
}