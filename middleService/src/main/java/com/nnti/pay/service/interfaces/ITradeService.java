package com.nnti.pay.service.interfaces;

import com.nnti.common.model.vo.User;
import com.nnti.pay.controller.vo.PayFyVo;
import com.nnti.pay.model.vo.MerchantPay;

/**
 * Created by wander on 2017/1/20.
 */
public interface ITradeService {
    void updateCredit(User loginname, Double ordAmt, String code, String orderNo, String remark) throws Exception;

    void doPayFlow(String orderId, String orderAmount, String loginName, MerchantPay merchantPay, String remark,String flag) throws Exception;

    PayFyVo getZfbBanInfo(String loginName, Boolean isImgCode) throws Exception;

    void doPayPointCardFlow(String trade_no, Double money, String loginName, String card_code, MerchantPay mp,String remark,String flag) throws Exception;
    
    PayFyVo createDeposit(String loginName,String mctype, Double money) throws Exception;
}
