//package com.nnti.service;
//
//import com.nnti.base.BaseTest;
//import com.nnti.common.exception.BusinessException;
//import com.nnti.common.model.vo.User;
//import com.nnti.pay.service.interfaces.IBasicService;
//import com.nnti.pay.service.interfaces.IKDZFService;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * Created by wander on 2017/1/20.
// */
//public class BasicMoreTest extends BaseTest {
//
//    @Autowired
//    private IBasicService basicService;
//    @Autowired
//    private IKDZFService kdzfService;
//
//    @Test
//    public void createOrderNo() throws BusinessException {
//        String loginName = "woodytest";
//        System.out.println(basicService.createOrderNo(loginName + "_", loginName));
//
//    }
//
//    @Test
//    public void createBillNo() throws BusinessException {
//        String loginName = "woodytest";
//        String billNo = basicService.createBillNo(loginName, "12.0", "ule", "kdzfb", loginName,"_");
//        log.info("billNo=" + billNo);
//    }
//
//    @Test
//    public void update() {
//        User user = new User();
//        user.setLoginName("woodytest");
//        user.setIsCashin(5);
//        kdzfService.update(user);
//    }
//}
