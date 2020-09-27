package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.QuestionStatus;

public interface IQuestionStatusService {

	QuestionStatus get(String loginname) throws Exception;

	int update(QuestionStatus questionStatus) throws Exception;

	int save(QuestionStatus questionStatus) throws Exception;
}