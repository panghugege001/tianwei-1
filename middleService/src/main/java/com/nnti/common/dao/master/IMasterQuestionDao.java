package com.nnti.common.dao.master;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Question;

public interface IMasterQuestionDao {

	List<Question> getPlayerQuestion(Map<String, Object> params);
}