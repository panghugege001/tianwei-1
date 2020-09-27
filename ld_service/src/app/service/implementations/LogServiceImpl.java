package app.service.implementations;

import dfh.model.Creditlogs;
import app.dao.BaseDao;
import app.service.interfaces.ILogService;
import app.util.DateUtil;

public class LogServiceImpl implements ILogService {

	private BaseDao baseDao;
	
	public void addCreditLog(String loginName, String type, Double credit, Double remit, Double newCredit, String remark) {
	
		Creditlogs log = new Creditlogs();
		log.setLoginname(loginName);
		log.setType(type);
		log.setCredit(credit);
		log.setRemit(remit);
		log.setNewCredit(newCredit);
		log.setRemark(remark);
		log.setCreatetime(DateUtil.getCurrentTime());
		
		baseDao.save(log);
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
}