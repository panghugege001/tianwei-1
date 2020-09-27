package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.Question;

public interface IQuestionService {

	List<Question> getPlayerQuestion(Map<String, Object> params) throws Exception;
}