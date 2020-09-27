package com.nnti.common.dao.slave;

import com.nnti.common.model.vo.QuestionStatus;

public interface ISlaveQuestionStatusDao {

	QuestionStatus get(String loginname);
}