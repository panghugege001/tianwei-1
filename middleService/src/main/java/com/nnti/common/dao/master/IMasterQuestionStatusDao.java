package com.nnti.common.dao.master;

import com.nnti.common.model.vo.QuestionStatus;

public interface IMasterQuestionStatusDao {
	
	QuestionStatus get(String loginname);

	int update(QuestionStatus questionStatus);

	int save(QuestionStatus questionStatus);
}