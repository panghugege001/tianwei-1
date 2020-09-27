package app.service.implementations;

import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import dfh.model.Question;
import app.dao.QueryDao;
import app.service.interfaces.IQuestionService;

public class QuestionServiceImpl implements IQuestionService {
	
	private QueryDao queryDao;
	
	public void saveQuestion(Question question) {
		
		question.setDelflag(0);
		question.setRemark(null);
		question.setCreatetime(new Date());
		
		queryDao.save(question);
	}
	
	@SuppressWarnings("unchecked")
	public Integer countQuestion(String loginName) {
		
		Integer count = 0;
		
		DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("delflag", 0));
		
		List<Question> questionList = queryDao.findByCriteria(dc);
		
		if (null != questionList && !questionList.isEmpty()) {
			
			count = questionList.size(); 
		}
		
		return count;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}