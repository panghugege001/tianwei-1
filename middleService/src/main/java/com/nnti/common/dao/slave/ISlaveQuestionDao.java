package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Question;

public interface ISlaveQuestionDao {

	List<Question> getPlayerQuestion(Map<String, Object> params);
}