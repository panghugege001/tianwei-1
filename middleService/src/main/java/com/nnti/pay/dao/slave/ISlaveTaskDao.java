package com.nnti.pay.dao.slave;

import java.util.List;
import java.util.Map;

import com.nnti.pay.model.vo.Task;

public interface ISlaveTaskDao {
	
	Task get(String pno,String level);
	
	int update(Task task);
		
	List<Task> getTask(Map<String, Object> params);
	
}