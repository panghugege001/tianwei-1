package com.nnti.pay.service.interfaces;

import com.nnti.common.model.vo.Proposal;
import com.nnti.pay.model.vo.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by wander on 2017/3/15.
 */
public interface ITaskService {
    int createBatch(List<Task> tasks);
    
    Task get(String pno,String level);
    
    
	List<Task> getTask(Map<String, Object> params);
    
	int update(Task task);
}
