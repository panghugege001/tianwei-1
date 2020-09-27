package com.nnti.common.dao.slave;

import com.nnti.common.model.vo.Dictionary;
import java.util.List;

public interface ISlaveDictionaryDao {

	Dictionary get(Long id);

	List<Dictionary> findByCondition(Dictionary dictionary);
}