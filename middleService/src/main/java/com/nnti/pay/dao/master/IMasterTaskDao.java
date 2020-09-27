package com.nnti.pay.dao.master;

import com.nnti.pay.model.vo.Task;
import java.util.List;
import java.util.Map;

public interface IMasterTaskDao {
    
	int createBatch(List<Task> tasks);
	    
   Task get(String pno,String level);
	
	int update(Task task);
		
	List<Task> getTask(Map<String, Object> params);
}