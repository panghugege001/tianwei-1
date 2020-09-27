package com.nnti.common.dao.slave;

import java.util.List;
import com.nnti.common.model.vo.Const;

public interface ISlaveConstDao {

	Const get(String id);
	
	List<Const> findConstList(Const con);
}