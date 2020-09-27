package app.model.vo;

import java.util.List;
import app.model.po.LatestPreferential;
import dfh.model.SystemConfig;

public class PreferentialVO {

	// 优惠类型名称集合
	List<SystemConfig> typeData;
	// 优惠类型数据集合
	List<LatestPreferential> preData;
	
	public List<SystemConfig> getTypeData() {
		
		return typeData;
	}

	public void setTypeData(List<SystemConfig> typeData) {
		
		this.typeData = typeData;
	}

	public List<LatestPreferential> getPreData() {
		
		return preData;
	}

	public void setPreData(List<LatestPreferential> preData) {
		
		this.preData = preData;
	}
	
}