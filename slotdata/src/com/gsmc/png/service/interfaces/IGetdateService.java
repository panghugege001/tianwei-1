package com.gsmc.png.service.interfaces;

import java.util.Date;
import java.util.List;

import com.gsmc.png.model.PlatformData;

public interface IGetdateService {
	
	public List<PlatformData> selectQtData(Date startT, Date endT);
	
	public void updateQtPlatForm(PlatformData data);
	
	public void updateTTGPlatForm(PlatformData data);
}
