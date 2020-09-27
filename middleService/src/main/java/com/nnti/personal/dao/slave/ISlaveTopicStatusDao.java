package com.nnti.personal.dao.slave;

import com.nnti.personal.model.vo.TopicStatus;

public interface ISlaveTopicStatusDao {

	public Integer findUnReadNum(TopicStatus topicStatus);

	TopicStatus findTopicStatusById(Integer id);
}