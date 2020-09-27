package app.service.interfaces;

import dfh.model.Question;

public interface IQuestionService {

	void saveQuestion(Question question);
	
	Integer countQuestion(String loginName);
	
}
