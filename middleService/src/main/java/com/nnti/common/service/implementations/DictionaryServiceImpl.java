package com.nnti.common.service.implementations;

import com.nnti.common.dao.master.IMasterDictionaryDao;
import com.nnti.common.dao.slave.ISlaveDictionaryDao;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictionaryServiceImpl implements IDictionaryService {

    @Autowired
    private IMasterDictionaryDao masterDictionaryDao;
    @Autowired
    private ISlaveDictionaryDao slaveDictionaryDao;
    
    @Cacheable(value = "dictionary", key = "'dictionary_'+#id")
	public Dictionary get(Long id) throws Exception {

		Assert.notEmpty(id);

		return slaveDictionaryDao.get(id);
	}
    
    @CacheEvict(value = "dictionary", allEntries = true)
	public Integer add(Dictionary dictionary) throws Exception {

		Assert.notEmpty(dictionary);
		
		return masterDictionaryDao.insert(dictionary);
	}
    
    @CacheEvict(value = "dictionary", allEntries = true)
	public Integer update(Dictionary dictionary) throws Exception {
		
    	Assert.notEmpty(dictionary);
		
    	return masterDictionaryDao.update(dictionary);
	}
    
    @CacheEvict(value = "dictionary", allEntries = true)
	public Integer delete(Long id) throws Exception {
		
    	Assert.notEmpty(id);
    	
		return masterDictionaryDao.delete(id);
	}
    
    @Cacheable(value = "dictionary", condition = "#dictionary!=null", key = "'dictionary_'+#dictionary.dictType+'_'+#dictionary.dictName+'_'+#dictionary.useable")
    public List<Dictionary> findByCondition(Dictionary dictionary) throws Exception {
        
    	return slaveDictionaryDao.findByCondition(dictionary);
    }
    
	public Dictionary findByOne(String type, String name, Integer ueable) throws Exception {
		
		List<Dictionary> dictionaries = slaveDictionaryDao.findByCondition(new Dictionary(type, name, ueable));
		
		for (Dictionary dict : dictionaries) {
			
			return dict;
		}
		
		return null;
	}
}