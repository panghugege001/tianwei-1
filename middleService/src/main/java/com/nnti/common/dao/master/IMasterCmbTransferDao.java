package com.nnti.common.dao.master;

import java.util.List;
import com.nnti.common.model.vo.CmbTransfer;

public interface IMasterCmbTransferDao {

	int createCmbTransferList(List<CmbTransfer> createList);
}