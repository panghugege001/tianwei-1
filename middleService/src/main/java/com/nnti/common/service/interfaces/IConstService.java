package com.nnti.common.service.interfaces;

import java.util.List;
import com.nnti.common.model.vo.Const;

public interface IConstService {

	Const get(String id) throws Exception;
	
	List<Const> findConstList(Const con) throws Exception;
}