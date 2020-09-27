package com.nnti.common.service.implementations;

import com.nnti.common.dao.slave.ISlaveConstDao;
import com.nnti.common.model.vo.Const;
import com.nnti.common.service.interfaces.IConstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConstServiceImpl implements IConstService {

    @Autowired
	private ISlaveConstDao slaveConstDao;

    public Const get(String id) throws Exception {

        return slaveConstDao.get(id);
    }

    public List<Const> findConstList(Const con) throws Exception {

    	return slaveConstDao.findConstList(con);
    }
}