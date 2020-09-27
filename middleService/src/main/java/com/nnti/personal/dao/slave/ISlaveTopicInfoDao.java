package com.nnti.personal.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.page.Page;
import com.nnti.personal.model.vo.TopicInfo;

public interface ISlaveTopicInfoDao {

	Page<TopicInfo> findTopicInfoByIdPage(Integer id);

	TopicInfo findTopicInfoById(Integer id);

	Page<TopicInfo> findTopicByUserNamePage(TopicInfo topicInfo);

	Page<TopicInfo> findMySelfTopicPage(TopicInfo topicInfo);

	List<TopicInfo> findPublicMsg(Map<String, Object> params);
}