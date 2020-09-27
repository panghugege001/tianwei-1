package com.nnti.personal.dao.master;

import com.nnti.personal.model.vo.TopicInfo;

public interface IMasterTopicInfoDao {

	Integer insert(TopicInfo topicInfo);

	Integer delete(Integer id);
}