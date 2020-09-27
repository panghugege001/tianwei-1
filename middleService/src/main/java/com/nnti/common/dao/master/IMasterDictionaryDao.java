package com.nnti.common.dao.master;

import com.nnti.common.model.vo.Dictionary;

public interface IMasterDictionaryDao {

	Integer insert(Dictionary dictionary);

	Integer update(Dictionary dictionary);

	Integer delete(Long id);
}