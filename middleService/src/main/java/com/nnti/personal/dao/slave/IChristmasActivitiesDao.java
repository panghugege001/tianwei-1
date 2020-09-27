/**
 * 
 */
package com.nnti.personal.dao.slave;

import com.nnti.personal.model.vo.ChristmasActivities;

/**
 * TODO
 * @author Caesar
 * 2017年12月8日上午11:15:23
 */
public interface IChristmasActivitiesDao {
	/**
	 * @Description:根据类型获取活动相关信息  
	 * @param  type
	 * @return ChristmasActivities    返回类型  
	 * @date  2017年12月8日上午11:19:34
	 */
	ChristmasActivities getByType(String type);
}
