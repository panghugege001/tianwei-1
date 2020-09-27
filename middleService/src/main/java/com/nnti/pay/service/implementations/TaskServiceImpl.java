package com.nnti.pay.service.implementations;

import com.nnti.pay.dao.master.IMasterTaskDao;
import com.nnti.pay.dao.slave.ISlaveTaskDao;
import com.nnti.pay.model.vo.Task;
import com.nnti.pay.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements ITaskService {

	@Autowired
	private IMasterTaskDao masterTaskDao;
	@Autowired
	private ISlaveTaskDao slaveTaskDao;

	public int createBatch(List<Task> tasks) {
		
		return masterTaskDao.createBatch(tasks);
	}

	@Override
	public Task get(String pno, String level) {
		return slaveTaskDao.get(pno,level);
	}

	@Override
	public List<Task> getTask(Map<String, Object> params) {
		return masterTaskDao.getTask(params);
	}

	@Override
	public int update(Task task) {
		return masterTaskDao.update(task);
	}
}