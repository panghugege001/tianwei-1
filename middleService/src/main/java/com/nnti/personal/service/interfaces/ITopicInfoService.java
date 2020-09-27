package com.nnti.personal.service.interfaces;

import java.util.Map;
import com.nnti.common.page.Page;
import com.nnti.personal.model.dto.TopicInfoDTO;
import com.nnti.personal.model.vo.TopicInfo;

public interface ITopicInfoService {
	
	/**
	 * 根据id查询站内信
	 */
	Page<TopicInfo> findTopicInfoByIdPage(Integer pageNum, Integer pageSize, Map<String, Object> params) throws Exception;
	
	/**
	 * 新增玩家发帖
	 */
	String saveTopicInfo(TopicInfoDTO dto) throws Exception;
	
	/**
	 * 新增玩家回帖
	 */
	String saveReplyInfo(TopicInfoDTO dto) throws Exception;
	
	/**
	 * 获取未读站内信数据
	 */
	Integer getUnReadTopicCount(Map<String, Object> params) throws Exception;
	
	/**
	 * 查询玩家的站内信
	 */
	Page<TopicInfo> findTopicByUserNamePage(int pageNum, int pageSize, String loginName) throws Exception;
	
	/**
	 * 查询玩家自己发的站内信
	 */
	Page<TopicInfo> findMySelfTopicPage(int pageNum, int pageSize, String loginName) throws Exception;
	
	/**
	 * 删除topicId
	 */
	String deleteTopicInfo(Map<String, Object> params) throws Exception;
	
	
	/**
	 * 批量删除
	 */
	String deleteTopic(Map<String, Object> params) throws Exception;
	
	/**
	 * 修改未读状态
	 */
	String updateTopicUnRead(Map<String, Object> params) throws Exception;
}