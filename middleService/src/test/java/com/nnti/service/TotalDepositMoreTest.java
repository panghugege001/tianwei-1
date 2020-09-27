//package com.nnti.service;
//
//import com.nnti.base.BaseTest;
//import com.nnti.common.exception.BusinessException;
//import com.nnti.common.utils.DateUtil;
//import com.nnti.pay.dao.ITotalDepositDao;
//import com.nnti.pay.model.vo.TotalDeposit;
//import com.nnti.pay.service.interfaces.IBasicService;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * Created by wander on 2017/1/30.
// */
//public class TotalDepositMoreTest extends BaseTest {
//
//    @Autowired
//    private ITotalDepositDao totalDepositDao;
//    @Autowired
//    private IBasicService basicService;
//
//    @Test
//    public void findByLoginName() throws BusinessException {
//        List<TotalDeposit> totalDeposit = totalDepositDao.findByLoginName("test");
//        System.out.println("查下数据：" + totalDeposit.size());
//
//    }
//
//    @Test
//    public void insert() throws BusinessException {
//
//        TotalDeposit totalDeposit = new TotalDeposit();
//        totalDeposit.setLoginname("test4");
//        totalDeposit.setAllDeposit(10.2);
//        totalDeposit.setCreateTime(DateUtil.getCurrentDate());
//        totalDeposit.setUpdateTime(DateUtil.getCurrentDate());
//        Integer result = totalDepositDao.insert(totalDeposit);
//        System.out.println("result=" + result);
//
//    }
//
//    @Test
//    public void update() throws BusinessException {
//        TotalDeposit totalDeposit = new TotalDeposit();
//        totalDeposit.setId(3L);
//        totalDeposit.setAllDeposit(30.3);
//        totalDeposit.setUpdateTime(DateUtil.getCurrentDate());
//        Integer result = totalDepositDao.update(totalDeposit);
//        System.out.println("result=" + result);
//    }
//
//    @Test
//    public void checkAndUpdateDeposit() throws BusinessException {
//        TotalDeposit totalDeposit = basicService.checkAndUpdateDeposit("test4");
//        System.out.println("AllDeposit:" + totalDeposit.getAllDeposit());
//    }
//}
