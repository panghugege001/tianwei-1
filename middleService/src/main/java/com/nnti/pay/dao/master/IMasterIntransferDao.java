package com.nnti.pay.dao.master;

import com.nnti.pay.model.vo.Intransfer;
import java.util.List;

public interface IMasterIntransferDao {
	
	int insert(Intransfer intransfer);

	int createBatch(List<Intransfer> intransfers);
}