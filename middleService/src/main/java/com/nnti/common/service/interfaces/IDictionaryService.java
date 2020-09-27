package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.Dictionary;
import java.util.List;

public interface IDictionaryService {

	Dictionary get(Long id) throws Exception;

    Integer add(Dictionary dictionary) throws Exception;

    Integer update(Dictionary dictionary) throws Exception;

    Integer delete(Long id) throws Exception;

    List<Dictionary> findByCondition(Dictionary dictionary) throws Exception;

    Dictionary findByOne(String type, String name, Integer ueable) throws Exception;
}