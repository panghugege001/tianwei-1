//package com.nnti.service;
//
//import com.nnti.base.BaseTest;
//import com.nnti.common.exception.BusinessException;
//import com.nnti.common.model.vo.Dictionary;
//import com.nnti.common.service.interfaces.IDictionaryService;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * Created by wander on 2017/2/9.
// */
//public class DictionaryServiceTest extends BaseTest {
//
//    @Autowired
//    private IDictionaryService dictionaryService;
//
//    @Test
//    public void get() throws BusinessException {
//        Long id = 21L;
//        Dictionary d = dictionaryService.get(id);
//        log.info("get()--" + d.getId() + d.getDictType() + d.getDictName() + d.getUseable());
//    }
//
//    @Test
//    public void add() throws BusinessException {
//        Dictionary d = new Dictionary();
//        d.setDictType("point_card_type");
//        d.setDictName("A");
//        d.setDictValue("缓存A");
//        d.setUseable(1);
//        Integer i = dictionaryService.add(d);
//        log.info("i=" + i + ",id=" + d.getId() + d.getDictType() + d.getDictName() + d.getUseable());
//    }
//
//    @Test
//    public void update() throws BusinessException {
//        Dictionary d = new Dictionary();
//        d.setId(21L);
//        d.setDictType("point_card_type");
//        d.setDictName("C");
//        d.setDictValue("缓存C");
//        d.setUseable(1);
//        dictionaryService.update(d);
//        log.info("update()=" + ",id=" + d.getId() + d.getDictType() + d.getDictName() + d.getUseable());
//    }
//
//    @Test
//    public void findByCondition() {
//        Dictionary dictionary = new Dictionary();
//        dictionary.setDictType("point_card_type");
////        dictionary.setDictName("B");
////        dictionary.setUseable(1);
//        dictionary.setOrderBy(1);
//        List<Dictionary> ds = dictionaryService.findByCondition(null);
//        for (Dictionary d : ds) {
//            log.info("findByCondition()--" + d.getId() + d.getDictType() + d.getDictName() + d.getUseable());
//        }
//    }
//}
