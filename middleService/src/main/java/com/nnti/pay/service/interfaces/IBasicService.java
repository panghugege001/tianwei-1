package com.nnti.pay.service.interfaces;

import com.nnti.common.model.vo.User;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.TotalDeposit;

public interface IBasicService {

    String createOrderNo(String prefix, String loginName) throws Exception;

    String createOrderNoBySeq(String prefix, String loginName);

    User getUser(String loginName) throws Exception;

    String createBillNo(String loginName, String orderAmount, MerchantPay mp, String prefix, String per) throws Exception;
    
    //E宝获取订单号
    String createEbaoBillNo(String loginName, String orderAmount, MerchantPay mp, String prefix, String per,String merNo) throws Exception;

    String generateTransferNo(int no);

    TotalDeposit checkAndUpdateDeposit(String loginname) throws Exception;
}
