//package com.nnti.service;
//
//import com.nnti.base.BaseTest;
//import com.nnti.common.exception.BusinessException;
//import com.nnti.pay.dao.IMerchantPayDao;
//import com.nnti.pay.model.vo.MerchantPay;
//import com.nnti.pay.service.interfaces.ITradeService;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * Created by wander on 2017/1/30.
// */
//public class TradeMoreTest extends BaseTest {
//
//    @Autowired
//    private IMerchantPayDao merchantPayDao;
//    @Autowired
//    private ITradeService tradeService;
//
//    @Test
//    public void addPayKdWxZfb() throws BusinessException {
//        String notic = "woodytest";
//        String flag = "0";
//        MerchantPay merchantPay = merchantPayDao.get(36L);
//        tradeService.doPayFlow("ws_kdzfb_woodytest_4000119", "13.00", notic, merchantPay, "");
//    }
//}
