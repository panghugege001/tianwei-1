/**
 * 
 */
package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.vo.ChristmasActivities;

/**
 * TODO 圣诞节存送活动接口
 * @author Caesar
 * 2017年12月8日上午11:32:50
 */
public interface IChristmasActivitiesService {
	/**
	 * @Description: 根据活动类型获取实体
	 * @param @param type
	 * @return ChristmasActivities    返回类型  
	 * @date  2017年12月8日上午11:35:23
	 */
	ChristmasActivities getByType(String type);
}
