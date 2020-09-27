package com.nnti.personal.dao.master;

import java.util.Map;
import com.nnti.personal.model.vo.TopicStatus;

public interface IMasterTopicStatusDao {

	int insert(TopicStatus topicStatus);

	int delete(Integer id);

	int update(Map<String, Object> params);
	
	int deleteTopic(Map<String, Object> params);
}