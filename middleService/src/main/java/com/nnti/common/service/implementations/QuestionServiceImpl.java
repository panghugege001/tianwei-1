package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.common.dao.master.IMasterQuestionDao;
import com.nnti.common.dao.slave.ISlaveQuestionDao;
import com.nnti.common.model.vo.Question;
import com.nnti.common.service.interfaces.IQuestionService;

@Service
public class QuestionServiceImpl implements IQuestionService {

	@Autowired
	private ISlaveQuestionDao slaveQuestionDao;
	
	@Autowired
	private IMasterQuestionDao masterQuestionDao;
	
	public List<Question> getPlayerQuestion(Map<String, Object> params) throws Exception {
		
		return masterQuestionDao.getPlayerQuestion(params);
	}
}