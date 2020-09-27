package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterQuestionStatusDao;
import com.nnti.common.dao.slave.ISlaveQuestionStatusDao;
import com.nnti.common.model.vo.QuestionStatus;
import com.nnti.common.service.interfaces.IQuestionStatusService;

@Service
public class QuestionStatusServiceImpl implements IQuestionStatusService {

	@Autowired
	private IMasterQuestionStatusDao masterQuestionStatusDao;
	@Autowired
	private ISlaveQuestionStatusDao slaveQuestionStatusDao;
	
	public QuestionStatus get(String loginname) throws Exception {
		
		return masterQuestionStatusDao.get(loginname);
	}
	
	public int update(QuestionStatus questionStatus) throws Exception {
		
		return masterQuestionStatusDao.update(questionStatus);
	}
	
	public int save(QuestionStatus questionStatus) throws Exception {
		
		return masterQuestionStatusDao.save(questionStatus);
	}
}